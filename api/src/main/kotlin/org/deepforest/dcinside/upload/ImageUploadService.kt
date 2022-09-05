package org.deepforest.dcinside.upload

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class ImageUploadService(
    private val client: AmazonS3,

    @Value("\${aws.s3.bucket}")
    private val bucket: String
) {

    @Throws(IOException::class)
    fun upload(path: FolderName, file: MultipartFile): String {
        val fileName = createFileName(path, file.contentType!!)
        return client.run {
            putObject(
                PutObjectRequest(bucket, fileName, file.inputStream, getObjectMetadata(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
            this.getUrl(bucket, fileName).toString()
        }
    }

    private fun getObjectMetadata(file: MultipartFile): ObjectMetadata {
        return ObjectMetadata().apply {
            this.contentType = file.contentType
        }
    }

    fun createFileName(path: FolderName, contentType: String): String {
        val extension = contentType.split("/").last()
        return "${path.name}/${Date().time}-${RandomStringUtils.random(6, true, true)}.${extension}"
    }
}
