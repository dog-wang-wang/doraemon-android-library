package com.doraemon.foundation_ui_view.view

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.*
import kotlin.math.roundToInt

/**
 * 类似layout(l, t, r, b)
 */
fun View.layout(x: Int, y: Int, gravity: Int = Gravity.START) {
    if (!isVisible) return
    if (gravity == Gravity.CENTER_HORIZONTAL) {
        val parentWidth = (parent as? ViewGroup)?.measuredWidth ?: 0
        val left = (parentWidth - measuredWidth) / 2
        layout(left, y, left + measuredWidth, y + measuredHeight)
        return
    }
    if (gravity == Gravity.CENTER_VERTICAL) {
        val parentHeight = (parent as? ViewGroup)?.measuredHeight ?: 0
        val top = (parentHeight - measuredHeight) / 2
        layout(x, top, x + measuredWidth, top + measuredHeight)
        return
    }
    if (gravity == Gravity.CENTER) {
        val parentWidth = (parent as? ViewGroup)?.measuredWidth ?: 0
        val parentHeight = (parent as? ViewGroup)?.measuredHeight ?: 0
        val left = (parentWidth - measuredWidth) / 2
        val top = (parentHeight - measuredHeight) / 2
        layout(left, top, left + measuredWidth, top + measuredHeight)
        return
    }
    val fromRight = if (gravity == Gravity.START) isRtl else !isRtl
    if (!fromRight) {
        layout(x, y, x + measuredWidth, y + measuredHeight)
    } else {
        val right = (parent as? ViewGroup)?.measuredWidth ?: 0
        layout(right - x - measuredWidth, y, right - x, y + measuredHeight)
    }
}

val View.isRtl: Boolean
    get() = context.resources.isRtl

val Resources.isRtl: Boolean
    get() = configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

val View.rtlLeft: Int
    get() = if (isRtl) (((parent as? ViewGroup)?.measuredWidth) ?: right) - right else left

val View.rtlRight: Int
    get() = if (isRtl) (((parent as? ViewGroup)?.measuredWidth) ?: left) - left else right

const val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

const val matchParent = ViewGroup.LayoutParams.MATCH_PARENT

fun marginLayoutParams(width: Int, height: Int): MarginLayoutParams {
    return MarginLayoutParams(width, height)
}

fun marginLayoutParams(width: Int, height: Int, margin: Int): MarginLayoutParams {
    return MarginLayoutParams(width, height).apply { setMargins(margin) }
}

fun marginLayoutParams(
    width: Int,
    height: Int,
    l: Int = 0,
    t: Int = 0,
    r: Int = 0,
    b: Int = 0
): MarginLayoutParams {
    return MarginLayoutParams(width, height).apply { setMargins(l, t, r, b) }
}

fun layoutParams(width: Int, height: Int): ViewGroup.LayoutParams {
    return ViewGroup.LayoutParams(width, height)
}

fun Context.resolveAttr(@AttrRes resId: Int): Int {
    val out = TypedValue()
    theme.resolveAttribute(resId, out, true)
    return out.resourceId
}

/**
 * 批量添加View
 */
fun ViewGroup.addViews(vararg views: View) {
    for (v in views) addView(v)
}

fun ViewGroup.addViewsIfNot(vararg views: View, params: ViewGroup.LayoutParams) {
    views.forEach { item ->
        item.let { it.parent ?: addView(it, params) }
    }
}

fun ViewGroup.addViewIfNotAttached(view: View?) {
    view?.let { it.parent ?: addView(it) }
}

fun ViewGroup.addViewIfNotAttached(view: View?, index: Int) {
    view?.let { it.parent ?: addView(it, index) }
}

fun Collection<View>.allGone() {
    forEach { it.isVisible = false }
}

fun Collection<View>.allVisible() {
    forEach { it.isVisible = true }
}

val View?.widthUsed: Int
    get() {
        this ?: return 0
        if (parent != null && visibility != View.GONE) {
            return marginHorizontal + measuredWidth
        }
        return 0
    }

val View?.heightUsed: Int
    get() {
        this ?: return 0
        if (parent != null && visibility != View.GONE) {
            return marginVertical + measuredHeight
        }
        return 0
    }

val View.marginHorizontal: Int
    get() = marginStart + marginEnd

val View.marginVertical: Int
    get() = marginTop + marginBottom

val View?.visibleWidth: Int
    get() = this?.let { if (it.isVisible) measuredWidth else 0 } ?: 0

val View?.visibleHeight: Int
    get() = this?.let { if (it.isVisible) measuredHeight else 0 } ?: 0

/**
 * 获取View的水平方向的中点坐标
 */
val View.centerX: Int
    get() = (left + right) / 2

/**
 * 获取View的竖直方向的中点坐标
 */
val View.centerY: Int
    get() = (top + bottom) / 2

val View.halfWidth: Int
    get() = measuredWidth / 2

val View.halfHeight: Int
    get() = measuredHeight / 2

val View.paddingHorizontal: Int
    get() = paddingStart + paddingEnd

val View.paddingVertical: Int
    get() = paddingTop + paddingBottom

fun View.getDrawable(@DrawableRes id: Int): Drawable = ContextCompat.getDrawable(context, id)!!

fun View.getColor(@ColorRes id: Int) = ContextCompat.getColor(context, id)

fun View.getDimen(@DimenRes id: Int): Int = resources.getDimension(id).roundToInt()

fun View.getString(@StringRes id: Int) = resources.getString(id)

/**
 * 批量展示View
 */
fun show(vararg views: View?) {
    for (v in views) {
        v?.isVisible = true
    }
}

/**
 * 批量隐藏View
 */
fun hide(vararg views: View?) {
    for (v in views) {
        v?.isVisible = false
    }
}

fun View.applySystemWindowInsetsMargin(
    applyLeft: Boolean = false,
    applyTop: Boolean = false,
    applyRight: Boolean = false,
    applyBottom: Boolean = false
) {
    doOnApplyWindowInsets { view, insets, _, margin ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.updateLayoutParams<MarginLayoutParams> {
            leftMargin = margin.left + left
            topMargin = margin.top + top
            rightMargin = margin.right + right
            bottomMargin = margin.bottom + bottom
        }
    }
}

fun View.applySystemWindowInsetsPadding(
    applyLeft: Boolean = false,
    applyTop: Boolean = false,
    applyRight: Boolean = false,
    applyBottom: Boolean = false
) {
    doOnApplyWindowInsets { view, insets, padding, _ ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )
    }
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsets, InitialPadding, InitialMargin) -> Unit) {
    // Create a snapshot of the view's padding & margin states
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding & margin states
    setOnApplyWindowInsetsListener { v, insets ->
        block(v, insets, initialPadding, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View): InitialMargin {
    val lp = view.layoutParams as? MarginLayoutParams
        ?: throw IllegalArgumentException("Invalid view layout params $view")
    return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun ViewGroup.measureChildWithMargins2(
    parentWidthMeasureSpec: Int, widthUsed: Int,
    parentHeightMeasureSpec: Int, heightUsed: Int
) {
    children.forEach { child ->
        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(
            parentWidthMeasureSpec,
            paddingStart + paddingEnd + lp.leftMargin + lp.rightMargin
                    + widthUsed, lp.width
        )
        val childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(
            parentHeightMeasureSpec,
            (paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin
                    + heightUsed), lp.height
        )
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }
}


/**
 * 弹起软键盘
 */
fun View.showInputKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    post {
        requestFocus()
        imm.showSoftInput(this, 0)
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var counter = 3 // 最多调用3次
            override fun onGlobalLayout() {
                if (imm.isActive(this@showInputKeyboard) || counter <= 0) { // 判断键盘是否弹起
                    // 如果键盘已经弹起，就不再监听
                    this@showInputKeyboard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    imm.showSoftInput(this@showInputKeyboard, 0)
                }
                counter--
            }
        })
    }
}

/**
 * 收起软键盘
 */
fun View.hideInputKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    post {
        clearFocus()
        imm.hideSoftInputFromWindow(windowToken, 0)
        var counter = 3 // 最多调用3次
        while (counter-- > 0) {
            if (!imm.isActive(this)) {
                imm.hideSoftInputFromWindow(this.windowToken, 0)
            }
        }
    }
}

/**
 * view的长宽拉伸动画
 */
fun View.createAnimator(property: String, value: Int, duration: Long): ObjectAnimator {
    val viewWrapper = ViewWrapper(this)
    return ObjectAnimator.ofInt(viewWrapper, property, value).setDuration(duration)
}

fun TextView.adjustCenterIcon() {
    val innerWidth = measuredWidth - layout.getLineWidth(0) - compoundDrawablePadding
    // left icon
    compoundDrawables[0]?.run {
        val offset = (innerWidth - intrinsicWidth) / 2
        bounds.offset(offset.toInt(), 0)
    }
    // right icon
    compoundDrawables[2]?.run {
        val offset = (innerWidth - intrinsicWidth) / 2
        bounds.offset(-offset.toInt(), 0)
    }
}

fun relayoutMargins(sequence: Sequence<View>, h: Int, scaleLarge: Boolean = true) {
    val bodyHeight = sequence.sumOf { it.measuredHeight }
    val remaining = h - bodyHeight
    val marginSum = sequence.sumOf { it.marginVertical }
    if (marginSum == 0) return
    val ratio = 1.0f * remaining / marginSum
    if (!scaleLarge && ratio > 1.0f) return
    sequence.forEach { v ->
        v.setMargins(
            v.marginLeft,
            (v.marginTop * ratio).roundToInt(),
            v.marginRight,
            (v.marginBottom * ratio).roundToInt()
        )
    }
}

fun ViewGroup.layoutChildrenVertical() {
    var layoutTop = 0
    children.forEach { v ->
        v.run { layout(marginLeft, layoutTop + marginTop) }
        layoutTop = v.bottom
    }
}

/**
 * 将View裁剪成圆角
 */
fun View.clipRoundCorner(radius: Float) {
    outlineProvider = RoundRectOutlineProvider(radius)
    clipToOutline = true
}

class RoundRectOutlineProvider(private val radius: Float) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, radius)
    }
}

fun View.toastLong(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}

fun View.toastShort(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}