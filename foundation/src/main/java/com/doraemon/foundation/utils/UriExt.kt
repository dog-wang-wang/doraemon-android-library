package com.doraemon.foundation.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 通过Uri找到它对应的文件并且使用
 * @param context 上下文
 * @param block 操作
 */
inline fun Uri.useAsFile(context: Context, block: (File?) -> Unit) {
    useAsFile(context, "${System.currentTimeMillis()}", block)
}

/**
 * 通过Uri找到它对应的文件并且使用
 * @param context 上下文
 * @param tempFileName Uri对应的文件所创建的临时文件的名称
 * @param block 操作
 */
inline fun Uri.useAsFile(context: Context, tempFileName: String, block: (File?) -> Unit) {
    val tempFile = getTempFile(context, tempFileName)
    block(tempFile)
    tempFile?.delete()
}

/**
 * 通过Uri获取他所指向的Image
 * @param context 上下文
 * @param tempFileName 临时文件名
 * @return 返回临时文件
 * @describe 发生错误的时候会返回空，其余时候不为空，因为已经创建出来临时文件了
 */
fun Uri.getTempFile(context: Context, tempFileName: String) = tryCatching(
    onError = {
        // 获取临时文件
        val temp = File(context.cacheDir, tempFileName)
        // 临时文件存在就给他删掉
        if (temp.exists()) temp.delete()
    },
    block = {
        // 创建临时文件
        val tempFile = File(context.cacheDir, tempFileName)
        // 拿到uri对应文件的流
        val uriInputStream = context.contentResolver.openInputStream(this) ?: return@tryCatching null
        // 写入uri输入流-> 文件输出流 -> File对象
        uriInputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
                output.flush()
            }
        }
        // 返回临时文件
        tempFile
    }
)

/**
 * 核心方法：通过 Uri 获取文件的字节数组（即文件原始内容）
 * @param context 上下文
 * @return 文件的字节数组（可直接上传/解析），失败返回 null
 * 一次性读取所有字节（小文件推荐）
 */
fun Uri.getByteArray(context: Context) = useInputStream(context) { it.readBytes() }


/**
 * 获取Uri的MIME类型
 */
fun Uri.getMimeType(context: Context): String? = tryCatching { context.contentResolver.getType(this) }

/**
 * 获取Uri的显示名称（文件名）
 */
fun Uri.getDisplayName(context: Context): String? = tryCatching {
    val projection = arrayOf(MediaStore.Downloads.DISPLAY_NAME)
    context.contentResolver.query(this, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow(projection.first()))
        } else {
            null
        }
    }
}

/**
 * 获取Uri的文件大小
 */
fun Uri.getFileSize(context: Context): Long? = tryCatching {
    val projection = arrayOf(MediaStore.Downloads.SIZE)
    context.contentResolver.query(this, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            cursor.getLong(cursor.getColumnIndexOrThrow(projection.first()))
        } else {
            null
        }
    }
}

/**
 * 使用Uri对应文件的InputSteam
 * @param context 上下文
 * @param onFailure 错误回调
 * @param block 使用的方法
 */
inline fun <T> Uri.useInputStream(context: Context, onFailure: ((Exception) -> Unit) = {}, block: (InputStream) -> T): T? = tryCatching(onError = onFailure) {
    val inputStream = context.contentResolver.openInputStream(this) ?: return@tryCatching null
    inputStream.use { return@tryCatching block(it) }
}
