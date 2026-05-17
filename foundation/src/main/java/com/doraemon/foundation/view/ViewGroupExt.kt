package com.doraemon.foundation.view

//noinspection SuspiciousImport
import android.R
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.doraemon.foundation.utils.dp
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily

/**
 * View的构造方法扩展
 * @param w 宽度，默认wrap_content
 * @param h 高度，默认wrap_content
 * @param attach 是否添加到父布局，默认添加，如果用在RecyclerView的Adapter中就设置为false
 * @param block 用户自定义的设置项
 */
fun ViewGroup.view(
    w: Int = wrapContent,
    h: Int = wrapContent,
    attach: Boolean = true,
    block: (View.() -> Unit)
) = View(context).apply {
    layoutParams = marginLayoutParams(w, h)
    // 注意这里，block的调用一定要放在最后，防止覆盖用户的设置
    block.invoke(this)
    if (attach) addView(this)
}

/**
 * TextView的构造方法扩展
 */
fun ViewGroup.textView(
    w: Int = wrapContent,
    h: Int = wrapContent,
    attach: Boolean = true,
    block: (TextView.() -> Unit)
) = AppCompatTextView(context).apply {
    layoutParams = marginLayoutParams(w, h)
    block.invoke(this)
    if (attach) addView(this)
}

/**
 * ImageView的构造方法扩展
 */
fun ViewGroup.imageView(
    w: Int = wrapContent,
    h: Int = wrapContent,
    attach: Boolean = true,
    block: (ImageView.() -> Unit)
) = AppCompatImageView(context).apply {
    layoutParams = marginLayoutParams(w, h)
    scaleType = ImageView.ScaleType.CENTER
    block.invoke(this)
    if (attach) addView(this)
}

/**
 * ShapeableImageView的构造方法扩展
 */
fun ViewGroup.shapeableImageView(
    w: Int = wrapContent,
    h: Int = wrapContent,
    attach: Boolean = true,
    block: (ShapeableImageView.() -> Unit)
) = ShapeableImageView(context).apply {
    layoutParams = marginLayoutParams(w, h)
    scaleType = ImageView.ScaleType.CENTER
    block.invoke(this)
    if (attach) addView(this)
}

/**
 * EditText的构造方法扩展
 */
fun ViewGroup.editText(
    w: Int = wrapContent,
    h: Int = wrapContent,
    attach: Boolean = true,
    block: (EditText.() -> Unit)
) = AppCompatEditText(context).apply {
    layoutParams = marginLayoutParams(w, h)
    block.invoke(this)
    if (attach) addView(this)
}

/**
 * RecyclerView的构造方法扩展
 */
fun ViewGroup.recyclerView(
    w: Int = matchParent,
    h: Int = matchParent,
    attach: Boolean = true,
    block: (RecyclerView.() -> Unit)
) = RecyclerView(context).apply {
    layoutParams = marginLayoutParams(w, h)
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    block.invoke(this)
    if (attach) this@recyclerView.addView(this)
}

/**
 * ViewPager2 的构造方法扩展
 */
fun ViewGroup.viewPager2(
    w: Int = matchParent,
    h: Int = matchParent,
    attach: Boolean = true,
    block: (ViewPager2.() -> Unit)
) = ViewPager2(context).apply {
    layoutParams = marginLayoutParams(w, h)
    block.invoke(this)
    if (attach) this@viewPager2.addView(this)
}

/**
 * WebView的构造方法扩展
 */
fun ViewGroup.webView(
    w: Int = matchParent,
    h: Int = matchParent,
    attach: Boolean = true,
    block: (WebView.() -> Unit)
) = WebView(context).apply {
    layoutParams = marginLayoutParams(w, h)
    block.invoke(this)
    if (attach) this@webView.addView(this)
}

/**
 * ProgressBar的构造方法扩展
 */
fun ViewGroup.progressBar(
    w: Int = 48.dp,
    h: Int = 48.dp,
    attach: Boolean = true,
    block: (ProgressBar.() -> Unit)
) = ProgressBar(context).apply {
    layoutParams = marginLayoutParams(w, h)
    block.invoke(this)
    if (attach) this@progressBar.addView(this)
}

/**
 * 添加一个圆形的ImageView
 * @param width 图片宽度
 * @param height 图片高度
 * @param strokeColorRes 描边颜色
 * @param strokeWidthInt 描边宽度
 * @param action 自己要进行的操作
 */
fun ViewGroup.circleImageView(
    width: Int,
    height: Int,
    strokeWidthInt: Int = 0,
    @ColorRes strokeColorRes: Int = R.color.holo_blue_bright,
    action: ShapeableImageView.() -> Unit
) = shapeableImageView(width, height) {
    setPadding(strokeWidthInt / 2)
    scaleType = ImageView.ScaleType.FIT_CENTER
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setAllCorners(CornerFamily.ROUNDED, (width - strokeWidthInt) / 2f)
        .build()
    strokeWidth = strokeWidthInt.toFloat()
    strokeColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(getColor(strokeColorRes)))
    action(this)
}