package au.com.ecetera

import com.amazonaws.services.s3.model.ObjectMetadata

/**
 * Created by robertk on 27/02/15.
 */
package object sbt {
  type MetadataMap = Map[String, ObjectMetadata]
  type Bucket=String
}
