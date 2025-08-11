package com.fesi.flowit.common.cloud.aws

data class AwsS3FileUploadVo(
    val fileName: String,
    val url: String?,
    val isUploaded: Boolean
) {
    companion object {
        fun of(fileName: String, url: String?, isUploaded: Boolean): AwsS3FileUploadVo {
            return AwsS3FileUploadVo(fileName, url, isUploaded)
        }
    }
}