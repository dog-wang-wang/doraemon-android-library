# Floating navigation bar

`foundation-ui-compose` 的 `ui.navigation.floating` 包提供了一套 Compose 自定义悬浮导航基建，用来构建类似 iOS 胶囊导航栏、毛玻璃导航栏和液态玻璃导航栏的体验。

它不是 Material3 `NavigationBar` 的二次包装。这个组件只复用 Compose 布局、动画和导航能力，视觉由基础库自己的 style 和策略对象控制。

## Overview

悬浮导航栏适合用于 App 的顶层页面切换，例如首页、地图、内容库、个人中心。它的核心目标是：

- 导航逻辑稳定：选中态来自 `NavController` 当前路由，而不是本地临时状态。
- 视觉能力可扩展：背景、指示器、图标、颜色、动画都可以通过策略替换。
- 摆放位置自由：导航栏和系统导航栏没有隐式关系，可以放在底部、中心或任意业务指定的位置。
- 接入成本低：业务只需要提供 `NavigationBarDestinations`、`NavHost` 和可选 style。

## Basic example

下面的示例展示如何使用 `HomeFloatingNavigationScreen` 创建一个带悬浮导航栏的首页脚手架。

```kotlin
@Composable
fun TravelHome() {
    val destinations = NavigationBarDestinations(
        listOf(
            NavigationBarDestinations.ItemDestination(
                label = { Text("动态") },
                icon = { Icon(Icons.Default.Route, contentDescription = null) },
                routerName = "home",
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text("地图") },
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                routerName = "map",
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text("文库") },
                icon = { Icon(Icons.Default.Article, contentDescription = null) },
                routerName = "library",
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text("我的") },
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                routerName = "profile",
            ),
        )
    )

    HomeFloatingNavigationScreen(
        navigationBarDestinations = destinations,
        navigationBarModifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 24.dp,
        ),
        navigationBarStyle = FloatingNavigationBarDefaults.style(),
    ) { navController, startDestination, modifier ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier,
        ) {
            composable("home") { HomeScreen() }
            composable("map") { MapScreen() }
            composable("library") { LibraryScreen() }
            composable("profile") { ProfileScreen() }
        }
    }
}
```

## API surface

悬浮导航基建由四层组成。

当前 navigation 包结构如下：

- `ui.navigation.model`：共享导航数据模型。
- `ui.navigation.core`：共享 NavController 扩展和路由计算。
- `ui.navigation.material`：Material3 底部导航实现。
- `ui.navigation.floating`：自定义悬浮导航实现。

`ui.navigation.model.NavigationBarDestinations` 描述导航项数据：

```kotlin
class NavigationBarDestinations(
    val itemDestinations: List<ItemDestination>,
    private val defaultIndex: Int,
)
```

每个 `ItemDestination` 代表一个 tab：

```kotlin
data class ItemDestination(
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val selectedIcon: (@Composable () -> Unit)? = null,
    val routerName: String,
)
```

`ui.navigation.floating.HomeFloatingNavigationScreen` 提供首页脚手架：

```kotlin
@Composable
fun HomeFloatingNavigationScreen(
    navigationBarDestinations: NavigationBarDestinations,
    navigationBarModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavigationBarDestinations.DEFAULT_INDEX,
    navigationBarStyle: FloatingNavigationBarStyle = FloatingNavigationBarDefaults.style(),
    navigationBarAlignment: Alignment = Alignment.BottomCenter,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit,
)
```

`ui.navigation.floating.FloatingNavigationBar` 是纯导航栏组件：

```kotlin
@Composable
fun FloatingNavigationBar(
    controller: NavController,
    destinations: NavigationBarDestinations,
    modifier: Modifier = Modifier,
    style: FloatingNavigationBarStyle = FloatingNavigationBarDefaults.style(),
)
```

`ui.navigation.floating.FloatingNavigationBarStyle` 描述视觉配置：

```kotlin
data class FloatingNavigationBarStyle(
    val background: FloatingNavigationBarBackground,
    val shape: Shape,
    val height: Dp,
    val maxWidth: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemContentMode: FloatingNavigationBarItemContentMode,
    val iconLabelSpacing: Dp,
    val indicatorStrategy: FloatingNavigationBarIndicatorStrategy,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val itemColorStrategy: FloatingNavigationBarItemColorStrategy,
    val iconStrategy: FloatingNavigationBarIconStrategy,
    val tabClickIndication: Indication?,
)
```

## Component responsibilities

`HomeFloatingNavigationScreen` 负责创建并共享 `NavController`，把导航栏覆盖到业务内容之上。

`FloatingNavigationBar` 负责读取当前路由、计算选中项、绘制滑动指示器和处理 tab 点击。

`FloatingNavigationBarStyle` 只负责视觉，不负责位置。高度、圆角、背景、颜色、动画都属于 style；底部距离、系统栏避让、居中显示等摆放规则应该通过 `navigationBarModifier` 和 `navigationBarAlignment` 控制。

`FloatingNavigationBarBackground` 只负责背景绘制。新增一种背景效果时，不需要改导航栏主体，只需要新增一个背景策略。

## Background strategies

背景使用策略模式。调用方可以选择内置策略，也可以实现自己的 `FloatingNavigationBarBackground`。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    background = liquidGlassNavigationBarBackground(
        tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.70f),
        highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f),
        refractionColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
        innerShadowColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.14f),
        borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.54f),
    ),
)
```

内置背景策略：

- `solidNavigationBarBackground`：纯色背景，适合品牌色或明确实体底栏。
- `translucentNavigationBarBackground`：轻量半透明背景，适合简单玻璃感。
- `glassNavigationBarBackground`：基础玻璃背景，适合弱质感场景。
- `frostedGlassNavigationBarBackground`：毛玻璃背景，强调磨砂、雾化、朦胧感。
- `liquidGlassNavigationBarBackground`：液态玻璃背景，强调高光、折射和柔和流动质感。
- `imageNavigationBarBackground`：图片背景，适合强品牌视觉或节日主题。

## Indicator strategies

选中指示器同样使用策略模式。指示器只负责计算滑动色块的位置和大小，颜色和绘制由导航栏统一处理。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    indicatorStrategy = FloatingNavigationBarDefaults.iconIndicator(),
)
```

可选策略：

- `iconIndicator()`：指示器只包裹图标区域。
- `iconAndLabelIndicator()`：指示器包裹图标和文字区域。

如果 `itemContentMode = IconOnly`，但外部传入了 `iconAndLabelIndicator()`，组件会自动降级为 `iconIndicator()`，并输出 debug 日志。这里不抛异常，因为这是展示策略受显示模式限制，不是致命配置错误。

## Content mode

使用 `itemContentMode` 控制 tab 内容展示。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    itemContentMode = FloatingNavigationBarItemContentMode.IconAndLabel,
    iconLabelSpacing = 4.dp,
)
```

可选模式：

- `IconOnly`：只显示图标。适合空间紧张或图标语义足够明确的场景。
- `IconAndLabel`：同时显示图标和文字。适合需要明确表达导航含义的主 App。

`iconLabelSpacing` 只在 `IconAndLabel` 模式下生效。

## Color and icon strategies

选中态可以通过颜色变化表达，也可以通过替换图标表达。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    itemColorStrategy = FloatingNavigationBarDefaults.itemColors(),
    iconStrategy = FloatingNavigationBarDefaults.replaceIconOnSelected(),
)
```

颜色策略返回图标色和文字色：

```kotlin
fun interface FloatingNavigationBarItemColorStrategy {
    @Composable
    fun colors(selected: Boolean): FloatingNavigationBarItemColors
}
```

图标策略决定选中时画哪个图标：

- `tintIcon()`：始终使用 `icon`，只改变颜色。
- `replaceIconOnSelected()`：选中时优先使用 `selectedIcon`，没有提供时自动回退到 `icon`。

示例：

```kotlin
NavigationBarDestinations.ItemDestination(
    label = { Text("地图") },
    icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
    selectedIcon = { Icon(Icons.Filled.Map, contentDescription = null) },
    routerName = "map",
)
```

## Animation

指示器移动动画由外部控制，并提供默认值。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    indicatorAnimationSpec = FloatingNavigationBarDefaults.tweenIndicatorAnimation(
        durationMillis = 280,
    ),
)
```

可选动画工厂：

- `springIndicatorAnimation()`：弹簧动画，适合轻微弹性的液态玻璃手感。
- `tweenIndicatorAnimation()`：固定时长动画，适合设计稿明确要求毫秒数的场景。

## Click indication

默认情况下，tab 点击 ripple 是关闭的。

```kotlin
val style = FloatingNavigationBarDefaults.style(
    tabClickIndication = null,
)
```

原因是悬浮导航已经有滑动指示器作为主反馈。如果同时出现 Material ripple，会产生两套点击反馈，视觉上容易冲突。

如果业务确实需要 ripple，可以显式传入 `Indication`。

## Positioning

导航栏和系统导航栏没有默认关联。基础库不会自动调用 `navigationBarsPadding()`，也不会自动避让系统手势区域。

把导航栏放到底部：

```kotlin
HomeFloatingNavigationScreen(
    navigationBarDestinations = destinations,
    navigationBarAlignment = Alignment.BottomCenter,
    navigationBarModifier = Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        bottom = 24.dp,
    ),
) { navController, startDestination, modifier ->
    // NavHost
}
```

把导航栏放到屏幕中心：

```kotlin
HomeFloatingNavigationScreen(
    navigationBarDestinations = destinations,
    navigationBarAlignment = Alignment.Center,
) { navController, startDestination, modifier ->
    // NavHost
}
```

需要避让系统栏时，由业务明确添加：

```kotlin
navigationBarModifier = Modifier
    .navigationBarsPadding()
    .padding(bottom = 12.dp)
```

这样做可以保证基建组件本身是纯粹的悬浮导航栏，而不是只服务底部系统栏场景的专用组件。

## Key points

- 不要把系统栏避让、页面边距、对齐位置放进 `FloatingNavigationBarStyle`。
- 不要在业务侧二次 `clip` 导航栏胶囊，裁剪应该由 `FloatingNavigationBar` 内部统一处理。
- 不要用 magic number 调整导航栏内部尺寸，优先扩展 `FloatingNavigationBarDefaults` 或业务 token。
- 背景效果使用 `FloatingNavigationBarBackground` 策略扩展，不要把多种背景逻辑堆进导航栏主体。
- 指示器大小使用 `FloatingNavigationBarIndicatorStrategy` 扩展，不要在 tab item 中分别绘制选中背景。
- 选中态以 `NavController.currentBackStackEntryAsState()` 为准，不要额外维护一份 selected index。
- `IconOnly` 模式下不支持包裹文字的指示器，组件会自动降级并记录 debug 日志。

## Result

默认配置会生成一个浮动胶囊导航栏：

- 背景为毛玻璃策略。
- 形状为 `RoundedCornerShape(percent = 50)`，始终保持胶囊形。
- 高度为 `64.dp`。
- tab 同时显示图标和文字。
- 选中指示器在 tab 之间平滑滑动。
- 选中项图标和文字颜色会跟随 `MaterialTheme.colorScheme` 变化。
- 点击 ripple 默认关闭。

## Related files

- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/model/NavigationBarDestinations.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/core/NavigationControllerExt.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/material/MaterialNavigationScreen.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/material/DoraemonNavigationBar.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/HomeFloatingNavigationScreen.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavigationBar.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavigationBarStyle.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavigationBarBackground.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavigationBarItemStyle.kt`
