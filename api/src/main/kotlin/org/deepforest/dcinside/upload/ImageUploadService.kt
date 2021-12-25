package org.deepforest.dcinside.upload

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
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
        val fileName = createFileName(path, file.contentType)
        client.putObject(
            PutObjectRequest(bucket, fileName, file.inputStream, null)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return client.getUrl(bucket, fileName).toString()
    }

    fun createFileName(path: FolderName, contentType: String): String {
        val split = StringUtils.split(contentType, "/")
        return StringUtils.joinWith("/", path.name, Date().time.toString() + RandomStringUtils.random(6, true, true)) + "." + split[1]
    }
}
