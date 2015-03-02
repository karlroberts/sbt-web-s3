[//]: # (This is the README.md file for au.com.ecetera.sbt sbt-web-s3 plugin)

# sbt-web-s3

## Description

This plugin's main goal is to allow you to publish compressed static website assets to an Amazon AWS S3 bucket.
By default all the files found in the s3wsAssetDir will be uploaded to the specified bucket. Files that can
be compressed will be gzip'ed on upload and the S3 ObjectMetadata (aka the Content-Encoding header) will be set
so that a browser will expand the file on render.

While this plugin is not an sbt-web plugin or pipeline it's default configuration is designed to be used after
running sbt-web's `webStage` task which places all the ready to publish web assets in `target.value/web/stage`.

In order to see the results of using the plugin as a website you should follow the AWS instructions on 
setting up the bucket to serve a static website at http://docs.aws.amazon.com/AmazonS3/latest/dev/WebsiteHosting.html

There are a number or settings that effect what is uploaded and what is compressed.

for this help use:-

    $ sbt s3wsReadme

## To Build

* clone this repo and run sbt
    $ git clone https://github.com/Ecetera/sbt-web-s3.git
    $ ./sbt


## Usage

* add to your project/plugin.sbt the following lines:-

    addSbtPlugin("au.com.ecetera.sbt" % "sbt-web-s3" % "0.1.0-SNAPSHOT")

You will then be able to use the task `s3wsUpload` defined
in the nested object `au.com.ecetera.sbt.S3WebsitePlugin.S3WS`.
All these operations will use HTTPS as a transport protocol.

Please check the Scaladoc API of the `S3WebsitePlugin` object, and of its nested `S3WS` object,
to get additional documentation of the available sbt tasks.

## Setting Descriptions

    s3wsIncremental : Boolean, defaults to true. If true only publishes the files that have changed since last time s3Upload was run.

    s3wsWithCompression : Boolean, defaults to true. If true we compress eligible files.

    s3wsLeaveAsIs : Sequence of Files that we don't want to modify on upload,
                    ie files that would normally be compressed but we don't want them to be, for example if the file
                    is so small that the cost of decompressing is more than the time gained by downloading
                    over the network.

    s3wsAssetDir : Base dir for Web assets to publish.
                   Defaults to the sbt-web stagingDirectory: target.value/web/stage.
                   NB that it doesn't not depend on the sbt-web plugin but is intended to work with it.

    host in s3wsUpload : Host used by the s3wsUpload task, either "mybucket.s3.amazonaws.com" or "mybucket".

    progressBar in s3wsUpload : Boolean, defaults to false. Set to true to get a progress indicator during S3 uploads/downloads.

    credentials : a Seq(Credential(fileLocation)), see Example 1 below for usage.


## Example 1

Here is a complete example:

project/plugin.sbt:

    addSbtPlugin("au.com.ecetera.sbt" % "sbt-web-s3" % "0.1.0-SNAPSHOT")

build.sbt:

    enablePlugins(S3WebsitePlugin)

    import S3WS._

    host in s3wsUpload := "your-bucket.s3.amazonaws.com"

    progressBar in s3wsUpload := true

    credentials += Credentials(Path.userHome / ".s3Credentials")

~/.s3credentials:

    realm=Amazon S3
    host=your-bucket.s3.amazonaws.com
    user=<Access Key ID>
    password=<Secret Access Key>

Just create two sample files called "a" and "b" in the same directory that contains build.sbt, then try:

    $ sbt s3-upload

assuming you have (progressBar in upload) set to true , which is recommended only for testing, you will see progress
on upload.

    $ sbt
    > s3wsUpload
    [==================================================]   100%   index.html
    [=====================================>            ]    74%   /js/myscript.js

For bug fixes or suggestions please use the Github issues link
https://github.com/Ecetera/sbt-web-s3/issues

Have fun.

