[//]: # (This is the README.md file for au.com.ecetera.sbt sbt-web-s3 plugin)

# sbt-web-s3

### Status
currently a 0.1.0-SNAPSHOT build and has not been release to a repo yet.
Therefore not ready for production.
It does publish to S3 but Incremental mode is not yet active.

## Description

This AutoPlugin's main goal is to allow you to publish compressed static website assets to an Amazon AWS S3 bucket.
By default all the files found in the s3wsAssetDir will be uploaded to the specified bucket. Files that can
be compressed will be gzip'ed on upload and the S3 ObjectMetadata (aka the Content-Encoding header) will be set
so that a browser will expand the file on render.

### [sbt-web](https://github.com/sbt/sbt-web) interaction
While this plugin is not an sbt-web pipeline plugin its default configuration is designed to work with sbt-web piplines
For example the default location it searches for web assets is the default `webStage` directory ie `./target/web/stage`.
So a typical usecase would be to run sbt-web's `webStage` task which places all the ready to publish web assets in `target/web/stage`
and then run `s3wsUpload` to push the contents to the S3 bucket.

### Other info
* The plugin is an AutoPlugin, so it is designed to be used by sbt 0.13.5 or above. If you're not sure what version od sbt you are using
 run
    $ sbt sbtVersion
 You could also copy the ./sbt script to the root of your project and use it to run sbt.
* In order to see the results of using the plugin as a website you should follow the AWS instructions on
[setting up the bucket to serve a static website at](http://docs.aws.amazon.com/AmazonS3/latest/dev/WebsiteHosting.html)
* You will also need to [get your amazon credentials and secret key](http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html)
 in order to let the plugin connect and upload files into your bucket. See the example below to see how to use these credentials.

For this help use:-

    $ ./sbt s3wsReadme

## Usage

* add to your project/plugin.sbt the following line:-

    addSbtPlugin("au.com.ecetera.sbt" %% "sbt-web-s3" % "0.1.0-SNAPSHOT")

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

    credentials += Credentials(Path.userHome / ".s3credentials")

~/.s3credentials:

    realm=Amazon S3
    host=your-bucket.s3.amazonaws.com
    user=<Access Key ID>
    password=<Secret Access Key>

Just create two sample files called "index.html" and "./js/myscript,js" in the s3wsAssetDir directory (ie ./target/web/stage), then try:

    $ sbt s3wsUpload

assuming you have (progressBar in upload) set to true , which is recommended only for testing, you will see progress
on upload.

    $ sbt
    > s3wsUpload
    [==================================================]   100%   index.html
    [=====================================>            ]    74%   /js/myscript.js

## Example 2
If you are not using sbt-web plugin and want to change the default s3wsAssetDir to ./web, say, then you can add this to your build.sbt

build.sbt:

    s3wsAssetDir := baseDirectory.value / "web"


## Bug fixes
For bug fixes or suggestions please use the [Github issues link](https://github.com/Ecetera/sbt-web-s3/issues)

## Develop
Clone this repo.
    $ git clone https://github.com/Ecetera/sbt-web-s3.git

To Build.
    $ ./sbt

Have fun.

