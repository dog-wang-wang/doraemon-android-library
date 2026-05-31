package com.doraemon.foundation.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresExtension

class IntentManager {

    companion object {
        /**
         * 获取相册的Intent
         */
        fun getGalleryIntent() = Intent(MediaStore.ACTION_PICK_IMAGES).apply { type = "image/*" }

        /**
         * 获取相机的Intent
         */
        fun getCameraIntent(saveUri: Uri?) = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            // 声明权限
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            // 指定存储路径
            putExtra(MediaStore.EXTRA_OUTPUT, saveUri)
        }

        /**
         * 获取文件管理器的Intent
         */
        fun getAudioIntent() = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)

        fun getFileManagerIntent() = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // 指定MIME类型，如“*/*”表示所有文件
        }
    }
}