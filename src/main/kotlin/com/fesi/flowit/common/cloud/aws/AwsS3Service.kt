package com.fesi.flowit.common.cloud.aws

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.lang.Exception
import com.fesi.flowit.common.logging.loggerFor

private val log = loggerFor<AwsS3Service>()
@Service
class AwsS3Service(
    private val s3Client: S3Client
) {
    @Value("\${cloud.aws.s3.bucket-name}")
    private lateinit var bucketName: String

    fun uploadFile(directory: String, uniqueKey: String, file: MultipartFile): AwsS3FileUploadVo {
        val fileName = file.originalFilename ?: file.name
        val key = "${directory}/${uniqueKey}"

        val putRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(file.contentType)
            .contentLength(file.size)
            .build()

        return try {
            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.inputStream, file.size))

            AwsS3FileUploadVo.of(fileName, getUrl(key), true)
        } catch (e: Exception) {
            log.warn("File material upload is failed.. ${e}")
            AwsS3FileUploadVo.of(fileName, null, false)
        }
    }

    private fun getUrl(key: String): String {
        return "https://${bucketName}.s3.${s3Client.serviceClientConfiguration().region()}.amazonaws.com/${key}"
    }
}