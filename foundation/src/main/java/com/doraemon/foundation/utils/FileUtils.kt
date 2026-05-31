package com.doraemon.foundation.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.doraemon.foundation.R
import java.io.InputStream
import java.io.OutputStream

/**
 * 文件操作工具扩展
 * 
 * 核心设计思想：
 * 1. 资源化：默认目录名从 strings.xml 中读取
 * 2. 管道化：核心逻辑基于 OutputStream，支持 String, ByteArray, InputStream
 * 3. 规范化：使用项目统一的异常处理工具 [tryCatching]
 */

/**
 * 获取默认的存储子目录名（从资源文件获取）
 */
private fun Context.getDefaultSubDir(): String {
    return tryCatching { getString(R.string.default_dir_name) } ?: "Doraemon"
}

/**
 * 保存文本到公共下载目录
 *
 * @param fileName 文件名（需包含扩展名）
 * @param content 文本内容
 * @param subDir 子目录名，默认为资源文件中的定义
 * @return 成功则返回文件的 Uri，失败返回 null
 */
fun Context.saveTextToDownloads(
    fileName: String,
    content: String,
    subDir: String = getDefaultSubDir()
): Uri? {
    return saveBytesToDownloads(fileName, content.toByteArray(Charsets.UTF_8), subDir)
}

/**
 * 保存二进制数据到公共下载目录
 */
fun Context.saveBytesToDownloads(
    fileName: String,
    bytes: ByteArray,
    subDir: String = getDefaultSubDir()
): Uri? {
    val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/$subDir"
    return saveToMediaStore(
        fileName = fileName,
        mimeType = MimeType.fromFileName(fileName),
        relativePath = relativePath,
        collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    ) { it.write(bytes) }
}

/**
 * 将输入流保存到公共下载目录
 */
fun Context.saveStreamToDownloads(
    fileName: String,
    inputStream: InputStream,
    subDir: String = getDefaultSubDir()
): Uri? {
    val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/$subDir"
    return saveToMediaStore(
        fileName = fileName,
        mimeType = MimeType.fromFileName(fileName),
        relativePath = relativePath,
        collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
    ) { output ->
        inputStream.use { it.copyTo(output) }
    }
}

/**
 * 核心方法：执行 MediaStore 插入并写出数据
 */
private fun Context.saveToMediaStore(
    fileName: String,
    mimeType: String,
    relativePath: String,
    collection: Uri,
    writeBlock: (OutputStream) -> Unit
): Uri? {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    }

    return tryCatching(onError = { errorWithEvent(R.string.event_save_file, "文件保存失败: ${it.message}", throwable = it) }) {
        val uri = contentResolver.insert(collection, values) ?: return@tryCatching null
        contentResolver.openOutputStream(uri)?.use { 
            writeBlock(it)
            it.flush()
        }
        debugWithEvent(R.string.event_save_file, "文件保存成功: $uri")
        uri
    }
}

/**
 * 创建一个文件的uri
 * @param dir 文件目录
 * @param fileName 文件名
 * @param mimeType 存储文件的格式
 */
fun Context.createFileUri(dir: String, fileName: String, mimeType: String): Uri? {
    // 创建文件的Uri
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.RELATIVE_PATH, dir)
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, mimeType)
    }
    // 插入文件信息进入外存
    return tryCatching { contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) }
}

/**
 * 常用 MIME 类型常量池
 */
object MimeType {
    const val TEXT_PLAIN = "text/plain"
    const val TEXT_HTML = "text/html"
    const val IMAGE_JPEG = "image/jpeg"
    const val IMAGE_PNG = "image/png"
    const val APPLICATION_JSON = "application/json"
    const val APPLICATION_PDF = "application/pdf"
    const val APPLICATION_ZIP = "application/zip"

    /**
     * 根据文件名后缀猜测 MIME 类型
     */
    fun fromFileName(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "txt" -> TEXT_PLAIN
            "html", "htm" -> TEXT_HTML
            "jpg", "jpeg" -> IMAGE_JPEG
            "png" -> IMAGE_PNG
            "json" -> APPLICATION_JSON
            "pdf" -> APPLICATION_PDF
            "zip" -> APPLICATION_ZIP
            else -> "*/*"
        }
    }
}
