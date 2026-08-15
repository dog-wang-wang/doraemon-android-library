# Floating navigation bar

`foundation-ui-compose` 的 `ui.navigation.floating` 包提供了一套 Compose 自定义悬浮导航基建，用来构建类似 iOS 胶囊导航栏和毛玻璃导航栏的体验。

它不是 Material3 `NavigationBar` 的二次包装。这个组件只复用 Compose 布局、动画和导航能力，视觉由基础库自己的 style 和策略对象控制。

## Overview

悬浮导航栏适合用于 App 的顶层页面切换，例如首页、地图、内容库、个人中心。它的核心目标是：

- 导航逻辑稳定：选中态来自 `NavController` 当前路由，而不是本地临时状态。
- 视觉能力可扩展：背景、指示器、图标、颜色、动画都可以通过策略替换。
- 摆放位置自由：导航栏和系统导航栏没有隐式关系，可以放在底部、中心或任意业务指定的位置。
- 接入成本低：业务只需要提供 `NavDestinations`、`NavHost` 和可选 style。

## Basic example

下面的示例展示如何使用 `FloatingNavScaffold` 创建一个带悬浮导航栏的首页脚手架。

```kotlin
@Composable
fun TravelHome() {
    val destinations = NavDestinations(
        listOf(
            NavDestinations.Item(
                label = { Text("动态") },
                icon = { Icon(Icons.Default.Route, contentDescription = null) },
                route = "home",
            ),
            NavDestinations.Item(
                label = { Text("地图") },
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                route = "map",
            ),
            NavDestinations.Item(
                label = { Text("文库") },
                icon = { Icon(Icons.Default.Article, contentDescription = null) },
                route = "library",
            ),
            NavDestinations.Item(
                label = { Text("我的") },
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                route = "profile",
            ),
        )
    )

    FloatingNavScaffold(
        destinations = destinations,
        barModifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 24.dp,
        ),
        style = FloatingNavDefaults.style(),
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
- `ui.navigation.core`：共享路由状态计算和 NavController 扩展。
- `ui.navigation.material`：Material3 底部导航实现。
- `ui.navigation.floating`：自定义悬浮导航实现。

`ui.navigation.model.NavDestinations` 描述导航项数据：

```kotlin
class NavDestinations(
    val items: List<Item>,
    private val defaultIndex: Int,
)
```

每个 `Item` 代表一个 tab：

```kotlin
data class Item(
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val selectedIcon: (@Composable () -> Unit)? = null,
    val route: String,
)
```

`ui.navigation.floating.FloatingNavScaffold` 提供首页脚手架：

```kotlin
@Composable
fun FloatingNavScaffold(
    destinations: NavDestinations,
    barModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavDestinations.DEFAULT_INDEX,
    style: FloatingNavStyle = FloatingNavDefaults.style(),
    alignment: Alignment = Alignment.BottomCenter,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit,
)
```

`ui.navigation.floating.FloatingNavBar` 是纯导航栏组件：

```kotlin
@Composable
fun FloatingNavBar(
    controller: NavController,
    destinations: NavDestinations,
    modifier: Modifier = Modifier,
    style: FloatingNavStyle = FloatingNavDefaults.style(),
)
```

`ui.navigation.floating.FloatingNavStyle` 描述视觉配置：

```kotlin
data class FloatingNavStyle(
    val background: NavBackground,
    val shape: Shape,
    val height: Dp,
    val maxWidth: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemContentMode: NavItemContentMode,
    val iconLabelSpacing: Dp,
    val indicatorStrategy: NavIndicatorStrategy,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val itemColorStrategy: NavItemColorStrategy,
    val iconStrategy: NavIconStrategy,
    val tabClickIndication: Indication?,
)
```

## Component responsibilities

navigation 基建的职责边界如下：

- `NavDestinations`：只描述导航数据，包括 tab 顺序、默认 tab、路由、图标和文案；不依赖 Material 或 Floating UI。
- `NavigationState`：只负责根据 `NavDestinations` 和当前路由计算安全首页与选中态；Material 和 Floating 共用这套逻辑。
- `NavigationControllerExt`：只封装 top-level tab 跳转行为，例如 `launchSingleTop`、`restoreState` 和 `popUpTo`。
- `MaterialNavScreen`：创建 `NavController`，使用 `Scaffold.bottomBar` 承载 Material 底栏，并把 `contentPadding` 交给业务页面。
- `MaterialNavBar`：只负责 Material3 底栏的显示、选中态和点击，不自定义浮动视觉。
- `FloatingNavScaffold`：创建 `NavController`，把业务内容和浮动导航栏叠放到同一个容器；当背景需要毛玻璃时，注册 Haze backdrop source。
- `FloatingNavBar`：只负责浮动导航栏的显示、选中态、滑动指示器和 tab 点击；真实模糊配置从背景策略映射到底层 Haze。
- `FloatingNavStyle`：只描述浮动导航栏视觉配置，例如尺寸、形状、内容模式、颜色策略、图标策略、指示器策略和点击反馈。
- `NavBackground`：只描述背景策略；纯色和图片只绘制背景，毛玻璃额外声明 `NavBackdropEffect`。
- `NavItemStyle`：只描述 tab 内容颜色策略和图标策略。

`FloatingNavScaffold` 负责创建并共享 `NavController`，把导航栏覆盖到业务内容之上。

当背景策略需要真实毛玻璃时，`FloatingNavScaffold` 还会把业务内容注册成 Haze 的 backdrop source。
这样导航栏只模糊自己覆盖到的页面区域，不会和系统导航栏产生隐式绑定，也不会要求业务手动传递截图或背景。

`FloatingNavBar` 负责读取当前路由、计算选中项、绘制滑动指示器和处理 tab 点击。

`FloatingNavStyle` 只负责视觉，不负责位置。高度、圆角、背景、颜色、动画都属于 style；底部距离、系统栏避让、居中显示等摆放规则应该通过 `barModifier` 和 `alignment` 控制。

`NavBackground` 负责描述背景策略。毛玻璃策略会额外声明 `NavBackdropEffect`，导航栏内部再把它映射到底层 Haze 配置。
边框是背景材质的边缘收口，因此也作为 background strategy 的参数传入。

## Border

边框是背景材质的边缘收口。不同背景可以使用不同的边框颜色和强度，因此 border 跟随 background strategy，而不是作为 `FloatingNavStyle` 的平级参数。

```kotlin
val style = FloatingNavDefaults.style(
    background = FloatingNavDefaults.frostedGlass(
        border = FloatingNavDefaults.backgroundBorder(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.54f),
        ),
    ),
)
```

需要关闭边框时，可以把宽度设为 `0.dp`，或者把颜色设为 `Color.Transparent`。

## Background strategies

背景使用策略模式。调用方可以选择内置策略，也可以实现自己的 `NavBackground`。

```kotlin
val style = FloatingNavDefaults.style(
    background = FloatingNavDefaults.frostedGlass(
        backgroundColor = Color.White.copy(alpha = 0.24f),
        tintColor = Color.White.copy(alpha = 0.18f),
        highlightColor = Color.White.copy(alpha = 0.30f),
        noiseFactor = 0.08f,
        border = FloatingNavDefaults.backgroundBorder(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.54f),
        ),
    ),
)
```

内置背景策略：

- `solidBackground`：纯色背景，适合品牌色或明确实体底栏。
- `frostedGlassBackground`：毛玻璃背景，强调磨砂、雾化、朦胧感。
- `imageBackground`：图片背景，适合强品牌视觉或节日主题。

## Frosted Glass

毛玻璃策略由两部分组成：

- Haze 负责真实 backdrop blur，并接管毛玻璃的 `backgroundColor`、`tintColor`、`blurRadius` 和 `noiseFactor`。
- `NavBackground` 负责补充导航栏自己的材质层，包括柔光、高光和边框。

`NavBackdropEffect` 是背景策略和底层库之间的映射对象。公开 API 只使用 Compose 基础类型，不把 Haze 类型泄露给业务：

```kotlin
data class NavBackdropEffect(
    val blurRadius: Dp,
    val backgroundColor: Color,
    val tintColor: Color,
    val noiseFactor: Float,
)
```

`FloatingNavScaffold` 会在内部把 `NavBackdropEffect` 和 Haze 需要的状态绑定成 `FloatingNavBackdrop`：

- 业务只需要写 `background = FloatingNavDefaults.frostedGlass()`。
- 基建内部自动创建并复用 Haze 状态。
- 纯色和图片背景不会生成 `FloatingNavBackdrop`，也不会承担真实模糊成本。

最后由内部适配层把绑定对象转换成 Haze 配置：

```kotlin
backgroundColor = backdrop.effect.backgroundColor
tints = listOf(HazeTint(backdrop.effect.tintColor))
blurRadius = backdrop.effect.blurRadius
noiseFactor = backdrop.effect.noiseFactor
```

这样可以保证职责单一：毛玻璃颜色、噪点和 blur 参数交给底层模糊库；基础库自己的背景策略只补充高光、环境光和边框。
毛玻璃策略不会再额外绘制一层 `backgroundColor`，避免 Haze 背景色和材质层重复叠加导致导航栏过于不透明。

当前没有使用 Cloudy。原因是 `com.github.skydoves:cloudy-native:1.0.0-alpha01` 要求依赖方使用 `compileSdk 37`，
而当前示例应用编译在 `android-36.1`。作为基建库，毛玻璃效果不应该强制所有接入方提升 compileSdk 门槛。

## Indicator strategies

选中指示器同样使用策略模式。指示器只负责计算滑动色块的位置和大小，颜色和绘制由导航栏统一处理。

```kotlin
val style = FloatingNavDefaults.style(
    indicatorStrategy = FloatingNavDefaults.iconIndicator(),
)
```

可选策略：

- `iconIndicator()`：指示器只包裹图标区域。
- `iconAndLabelIndicator()`：指示器包裹图标和文字区域。

如果 `itemContentMode = IconOnly`，但外部传入了 `iconAndLabelIndicator()`，组件会自动降级为 `iconIndicator()`，并输出 debug 日志。这里不抛异常，因为这是展示策略受显示模式限制，不是致命配置错误。

## Content mode

使用 `itemContentMode` 控制 tab 内容展示。

```kotlin
val style = FloatingNavDefaults.style(
    itemContentMode = NavItemContentMode.IconAndLabel,
    iconLabelSpacing = 4.dp,
)
```

`NavItemContentMode` 是 sealed 类型。当前内置模式：

- `IconOnly`：只显示图标。适合空间紧张或图标语义足够明确的场景。
- `IconAndLabel`：同时显示图标和文字。适合需要明确表达导航含义的主 App。

`iconLabelSpacing` 只在 `IconAndLabel` 模式下生效。

## Color and icon strategies

选中态可以通过颜色变化表达，也可以通过替换图标表达。

```kotlin
val style = FloatingNavDefaults.style(
    itemColorStrategy = FloatingNavDefaults.itemColors(),
    iconStrategy = FloatingNavDefaults.replaceIconOnSelected(),
)
```

颜色策略返回图标色和文字色：

```kotlin
fun interface NavItemColorStrategy {
    @Composable
    fun colors(selected: Boolean): NavItemColors
}
```

图标策略决定选中时画哪个图标：

- `tintIcon()`：始终使用 `icon`，只改变颜色。
- `replaceIconOnSelected()`：选中时优先使用 `selectedIcon`，没有提供时自动回退到 `icon`。

示例：

```kotlin
NavDestinations.Item(
    label = { Text("地图") },
    icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
    selectedIcon = { Icon(Icons.Filled.Map, contentDescription = null) },
    route = "map",
)
```

## Animation

指示器移动动画由外部控制，并提供默认值。

```kotlin
val style = FloatingNavDefaults.style(
    indicatorAnimationSpec = FloatingNavDefaults.tweenIndicatorAnimation(
        durationMillis = 280,
    ),
)
```

可选动画工厂：

- `springIndicatorAnimation()`：弹簧动画，适合轻微弹性的浮动导航手感。
- `tweenIndicatorAnimation()`：固定时长动画，适合设计稿明确要求毫秒数的场景。

## Click indication

默认情况下，tab 点击 ripple 是关闭的。

```kotlin
val style = FloatingNavDefaults.style(
    tabClickIndication = null,
)
```

原因是悬浮导航已经有滑动指示器作为主反馈。如果同时出现 Material ripple，会产生两套点击反馈，视觉上容易冲突。

如果业务确实需要 ripple，可以显式传入 `Indication`。

## Positioning

导航栏和系统导航栏没有默认关联。基础库不会自动调用 `navigationBarsPadding()`，也不会自动避让系统手势区域。

把导航栏放到底部：

```kotlin
FloatingNavScaffold(
    destinations = destinations,
    alignment = Alignment.BottomCenter,
    barModifier = Modifier.padding(
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
FloatingNavScaffold(
    destinations = destinations,
    alignment = Alignment.Center,
) { navController, startDestination, modifier ->
    // NavHost
}
```

需要避让系统栏时，由业务明确添加：

```kotlin
barModifier = Modifier
    .navigationBarsPadding()
    .padding(bottom = 12.dp)
```

这样做可以保证基建组件本身是纯粹的悬浮导航栏，而不是只服务底部系统栏场景的专用组件。

## Key points

- 不要把系统栏避让、页面边距、对齐位置放进 `FloatingNavStyle`。
- 不要在业务侧二次 `clip` 导航栏胶囊，裁剪应该由 `FloatingNavBar` 内部统一处理。
- 不要用 magic number 调整导航栏内部尺寸，优先扩展 `FloatingNavDefaults` 或业务 token。
- 背景效果使用 `NavBackground` 策略扩展，不要把多种背景逻辑堆进导航栏主体。
- 指示器大小使用 `NavIndicatorStrategy` 扩展，不要在 tab item 中分别绘制选中背景。
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

## Verification

修改导航基建后，优先运行完整 debug 构建：

```bash
./gradlew :travel:assembleDebug
```

`compileDebugKotlin` 只能验证 Kotlin 编译，不能覆盖 AAR metadata、资源合并、manifest 处理和 APK 打包。
引入或替换第三方 Compose 视觉库时，必须用 `assembleDebug` 确认不会出现 compileSdk 要求不匹配或运行时依赖问题。

## Related files

- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/model/NavDestinations.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/core/NavigationState.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/core/NavigationControllerExt.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/material/MaterialNavScreen.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/material/MaterialNavBar.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavScaffold.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavBar.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavBackdrop.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/FloatingNavStyle.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/NavBackground.kt`
- `foundation-ui-compose/src/main/java/com/doraemon/foundation_ui_compose/ui/navigation/floating/NavItemStyle.kt`
