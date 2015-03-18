/*
 * ====
 *     Copyright 2015 Ecetera Pty Ltd
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 * ====
 *
 * (c) Copyright Ecetera Pty Ltd, 2015
 *
 * Some files contain other unattributed Contributions to the Work; All Contributions
 * received from Contributors under the terms of the Apache License Agreement v 2.0 and
 * re-distributed in accordance with that license.
 */

package au.com.ecetera

import com.amazonaws.services.s3.model.ObjectMetadata

/**
 * Created by robertk on 27/02/15.
 */
package object sbt {

  import com.amazonaws.services.s3.AmazonS3Client
  import com.amazonaws.services.s3.model.{DeleteObjectsResult, S3ObjectSummary}

  import scala.util.Try

  type MetadataMap = Map[String, ObjectMetadata]
  type S3Bucket = String
  type S3Key = String
  type S3Prefix = String


  /** Helper to get all Object Summaries from S3 Java API into Scala collection */
  def s3ObjectSummaries(client: AmazonS3Client, bucket: S3Bucket, prefix: S3Prefix) : Try[List[S3ObjectSummary]] = {
    import com.amazonaws.services.s3.model.ObjectListing

    import scala.annotation.tailrec
    import scala.collection.JavaConverters._

    @tailrec
    def buildFullList(client: AmazonS3Client, previousListing: ObjectListing, acc: List[S3ObjectSummary]): List[S3ObjectSummary] = {
      val ol = client.listNextBatchOfObjects(previousListing)
      val oss = ol.getObjectSummaries.asScala.toList
      oss match {
        case Nil => acc
        case _   => buildFullList(client, ol, acc ::: oss )
      }
    }

    Try{
      val oList = if(prefix.isEmpty) {
        client.listObjects(bucket)
      } else {
        client.listObjects(bucket, prefix)
      }
      val summaries = buildFullList(client, oList, oList.getObjectSummaries.asScala.toList)
      summaries
    }
  }

  /**
   * Helper function to delete a list of S3Keys from a Bucket
   */
  def delKeysFromS3(client: AmazonS3Client, bucket: S3Bucket, s3keys: Set[S3Key]):  Try[DeleteObjectsResult] = {
    import scala.collection.JavaConverters._
    import com.amazonaws.services.s3.model.DeleteObjectsRequest

    Try {
      val x = {
        s3keys map { (k) => import com.amazonaws.services.s3.model.DeleteObjectsRequest
          new DeleteObjectsRequest.KeyVersion(k)
        }
      }.toList.asJava
      client.deleteObjects(new DeleteObjectsRequest(bucket).withQuiet(false).withKeys(x))
    }
  }

  def deleteAllFromS3(client: AmazonS3Client, bucket: S3Bucket, prefix: S3Prefix):  Try[DeleteObjectsResult] = {
    for {
      oss <- s3ObjectSummaries(client, bucket, prefix)
      keys = oss map (os => os.getKey)
      dor <- delKeysFromS3(client, bucket, keys.toSet)
    } yield dor
  }
}


