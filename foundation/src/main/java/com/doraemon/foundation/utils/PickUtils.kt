package com.doraemon.foundation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.doraemon.foundation.R
import com.doraemon.foundation.format.formatDateString

private fun ActivityResultLauncher<Intent>.pickFiles(context: Context, intent: Intent, errorToast: String) {
    intent.withAppExit(context, errorToast) { launch(it) }
}
/**
 * 打开相机
 * @param context 上下文
 * @param dir 图片存储目录
 * @param imageName 图片名称
 * @param onCreateImageUri 得到图片的uri
 */
inline fun ActivityResultLauncher<Intent>.takePhoto(
    context: Context,
    dir: String = "录音茶壶",
    imageName: String? = null,
    onCreateImageUri: (Uri?) -> Unit
) {
    // 1. 生成图片名称和保存路径
    val name = imageName ?: "JPEG_${System.currentTimeMillis().formatDateString()}"
    // 2. 创建图像存储的uri
    val imageUri = context.createFileUri("Pictures/$dir", name, MimeType.IMAGE_JPEG)
    // 4. 返回保存图片的路径
    onCreateImageUri(imageUri)
    // 5. 构建相机Intent且检查是否有相机应用并启动相机应用
    openCamera(context, imageUri)
}

/**
 * 选择打开并选择相机拍摄的图片
 */
fun ActivityResultLauncher<Intent>.openCamera(context: Context, saveUri: Uri?) {
    pickFiles(context, IntentManager.getCameraIntent(saveUri), context.getString(R.string.no_app_camera))
}

/**
 * 打开相册进行选择
 * @param context 上下文信息
 */
fun ActivityResultLauncher<Intent>.openGallery(context: Context) {
    pickFiles(context, IntentManager.getGalleryIntent(), context.getString(R.string.no_app_gallery))
}

/**
 * 打开音频选择器进行选择
 */
fun ActivityResultLauncher<Intent>.openAudioDir(context: Context) {
    pickFiles(context, IntentManager.getAudioIntent(), context.getString(R.string.no_app_audio))
}

/**
 * 打开intent的时候预先检测是否存在activity可以响应这个intent
 * @param context 上下文
 * @param errorString 不存在时的提示
 * @param block 存在时执行的函数
 */
inline fun Intent.withAppExit(context: Context, errorString: String, block: (Intent) -> Unit) {
    if (resolveActivity(context.packageManager) != null) {
        block(this)
    } else {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}