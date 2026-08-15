package com.dora.travel.ui.trip

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dora.travel.R
import com.dora.travel.ui.theme.DoraTheme
import com.dora.travel.ui.theme.IndustrialContainer
import com.dora.travel.ui.theme.IndustrialContainerHigh
import com.dora.travel.ui.theme.IndustrialContainerLow
import com.dora.travel.ui.theme.IndustrialOnPrimaryContainer
import com.dora.travel.ui.theme.IndustrialOnSurface
import com.dora.travel.ui.theme.IndustrialOnSurfaceVariant
import com.dora.travel.ui.theme.IndustrialOutline
import com.dora.travel.ui.theme.IndustrialPrimary
import com.dora.travel.ui.theme.IndustrialPrimaryContainer
import com.dora.travel.ui.theme.IndustrialSecondary
import com.dora.travel.ui.theme.IndustrialSecondaryContainer
import com.dora.travel.ui.theme.IndustrialSurface
import com.dora.travel.ui.theme.LocalTravelGlass
import com.dora.travel.ui.theme.LocalTravelGradients
import com.dora.travel.ui.theme.LocalTravelRadii
import com.dora.travel.ui.theme.LocalTravelSpacing

private object TripFormSize {
    val TopBarHeight = 64.dp
    val FormFieldHeight = 56.dp
    val LargeFormFieldHeight = 74.dp
    val TextAreaHeight = 128.dp
    val CoverHeight = 202.dp
    val DrawerTopRadius = 48.dp
    val PrimaryButtonHeight = 64.dp
}

/**
 * 节点编辑页。
 *
 * 设计稿表现为“背景内容 + 半透明遮罩 + 底部抽屉”。这里仍然把它做成普通 destination，
 * 是为了复用项目已有底部导航；如果后续接真实交互，只需要把 [NodeEditorDrawer] 放到
 * ModalBottomSheet 或自定义 BottomSheet 容器里，表单内部组件不需要重写。
 */
@Composable
fun NodeEditorScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSurface),
    ) {
        EditorBackgroundContent()

        // 半透明遮罩让用户感知正在编辑当前节点，同时保留底层行程上下文。
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(IndustrialSurface.copy(alpha = 0.64f)),
        )

        NodeEditorDrawer(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

/**
 * 新建行程页。
 *
 * 该页面是一个长表单，因此使用 Column + verticalScroll，而不是 LazyColumn。当前表单项数量
 * 固定且较少，Column 能让底部渐变按钮和顶部 AppBar 的层级更容易控制；后续如果字段变成动态
 * 列表，再替换为 LazyColumn 会更合适。
 */
@Composable
fun CreateTripScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSurface),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = LocalTravelSpacing.current.mobilePadding)
                .padding(top = TripFormSize.TopBarHeight + LocalTravelSpacing.current.stackLarge)
                .padding(bottom = 128.dp),
            verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackLarge),
        ) {
            LabeledInputField(
                labelRes = R.string.travel_create_trip_name,
                textRes = R.string.travel_create_trip_name_hint,
                trailingIcon = Icons.Default.Edit,
                height = TripFormSize.LargeFormFieldHeight,
                textColor = IndustrialOutline,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
                LabeledInputField(
                    labelRes = R.string.travel_create_start_date,
                    textRes = R.string.travel_create_start_date_value,
                    trailingIcon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f),
                    textColor = IndustrialOnSurface,
                )
                LabeledInputField(
                    labelRes = R.string.travel_create_end_date,
                    textRes = R.string.travel_create_end_date_value,
                    trailingIcon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f),
                    textColor = IndustrialOnSurface,
                )
            }

            CoverSelector()
            RoutePlanningSection()
            NodesAndTransportSection()
            PublicTripSection()
        }

        FormTopBar(
            titleRes = R.string.travel_create_title,
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            trailingIcon = Icons.Default.MoreHoriz,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            IndustrialSurface.copy(alpha = 0f),
                            IndustrialSurface.copy(alpha = 0.92f),
                            IndustrialSurface,
                        )
                    )
                )
                .padding(24.dp),
        ) {
            GradientActionButton(
                text = stringResource(R.string.travel_create_action),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/**
 * “我的”页暂时没有对应 Figma 画板。
 *
 * 为了保证底部导航四个目的地都有落点，这里先提供同主题占位页。它不是最终业务页面，
 * 但能验证导航结构、主题和系统栏适配都在同一套架构里工作。
 */
@Composable
fun ProfilePlaceholderScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSurface)
            .padding(LocalTravelSpacing.current.stackMedium),
        contentAlignment = Alignment.Center,
    ) {
        GlassPanel {
            Column(
                modifier = Modifier.padding(LocalTravelSpacing.current.stackLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = IndustrialSecondary,
                    modifier = Modifier.size(40.dp),
                )
                Text(
                    text = stringResource(R.string.travel_profile_title),
                    color = IndustrialOnSurface,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.travel_profile_body),
                    color = IndustrialOnSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun EditorBackgroundContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(27.dp),
        verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackLarge),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.travel_editor_background_title),
                    color = IndustrialOnSurface.copy(alpha = 0.38f),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(R.string.travel_editor_background_subtitle),
                    color = IndustrialOnSurfaceVariant.copy(alpha = 0.35f),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            IconBubbleSurface(icon = Icons.Default.Route, alpha = 0.28f)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackMedium),
        ) {
            BackgroundRouteCard(
                title = stringResource(R.string.travel_create_node_title),
                icon = Icons.Default.Flight,
                progress = 0.34f,
            )
            BackgroundRouteCard(
                title = stringResource(R.string.travel_editor_node_value),
                icon = Icons.Default.Train,
                progress = 0f,
            )
        }
    }
}

@Composable
private fun NodeEditorDrawer(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = IndustrialContainer.copy(alpha = LocalTravelGlass.current.elevatedCardAlpha),
        shape = RoundedCornerShape(
            topStart = TripFormSize.DrawerTopRadius,
            topEnd = TripFormSize.DrawerTopRadius,
        ),
        border = BorderStroke(1.dp, IndustrialPrimary.copy(alpha = LocalTravelGlass.current.borderAlpha)),
        shadowElevation = 30.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 33.dp, vertical = 33.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackLarge),
        ) {
            Box(
                modifier = Modifier
                    .size(width = 48.dp, height = 6.dp)
                    .clip(RoundedCornerShape(LocalTravelRadii.current.full))
                    .background(IndustrialOutline.copy(alpha = 0.5f)),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.travel_editor_title),
                        color = IndustrialOnSurface,
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = stringResource(R.string.travel_editor_subtitle),
                        color = IndustrialOnSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = IndustrialOnSurface)
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackLarge),
            ) {
                LabeledInputField(
                    labelRes = R.string.travel_editor_node_name,
                    textRes = R.string.travel_editor_node_value,
                    trailingIcon = Icons.Default.LocationOn,
                    textColor = IndustrialOnSurface,
                )
                TransportSegmentedControl(selectedIndex = 1)
                LabeledInputField(
                    labelRes = R.string.travel_editor_notes,
                    textRes = R.string.travel_editor_notes_hint,
                    height = TripFormSize.TextAreaHeight,
                    textColor = IndustrialOutline,
                    singleLine = false,
                )
                GradientActionButton(
                    text = stringResource(R.string.travel_editor_save),
                    modifier = Modifier.fillMaxWidth(),
                    height = TripFormSize.PrimaryButtonHeight,
                    textStyle = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}

/**
 * 带标签的输入展示组件。
 *
 * 当前页面还没有接入真实 TextField 编辑逻辑，所以这里先把它做成“可点击输入容器”。
 * 后续接 ViewModel 时可以把 textRes 换成 String，把 onClick 或 onValueChange 暴露出去，
 * 视觉结构仍然可以保持不变。
 */
@Composable
private fun LabeledInputField(
    @StringRes labelRes: Int,
    @StringRes textRes: Int,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    height: Dp = TripFormSize.FormFieldHeight,
    textColor: Color = IndustrialOnSurface,
    singleLine: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
    ) {
        Text(
            text = stringResource(labelRes),
            modifier = Modifier.padding(horizontal = LocalTravelSpacing.current.base),
            color = IndustrialPrimary,
            style = MaterialTheme.typography.labelSmall,
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            color = IndustrialContainerLow,
            shape = RoundedCornerShape(if (height > TripFormSize.FormFieldHeight) LocalTravelRadii.current.large else LocalTravelRadii.current.default),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = LocalTravelSpacing.current.stackMedium),
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
            ) {
                Text(
                    text = stringResource(textRes),
                    modifier = Modifier
                        .weight(1f)
                        .then(if (singleLine) Modifier else Modifier.padding(top = LocalTravelSpacing.current.stackSmall)),
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (singleLine) 1 else 4,
                    overflow = TextOverflow.Ellipsis,
                )
                trailingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = IndustrialPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

/**
 * 交通方式分段控件。
 *
 * 这里用数据列表驱动 UI，后续如果新增“巴士/轮渡”等交通方式，只需要追加一个
 * [TransportOption]，布局和选中态逻辑都不需要复制。
 */
@Composable
private fun TransportSegmentedControl(selectedIndex: Int) {
    val options = listOf(
        TransportOption(R.string.travel_transport_flight, Icons.Default.Flight),
        TransportOption(R.string.travel_transport_train, Icons.Default.Train),
        TransportOption(R.string.travel_transport_subway, Icons.Default.Train),
        TransportOption(R.string.travel_transport_walk, Icons.AutoMirrored.Filled.DirectionsWalk),
    )

    Column(verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
        Text(
            text = stringResource(R.string.travel_editor_transport),
            modifier = Modifier.padding(horizontal = LocalTravelSpacing.current.base),
            color = IndustrialPrimary,
            style = MaterialTheme.typography.labelSmall,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LocalTravelRadii.current.large))
                .background(IndustrialContainer.copy(alpha = 0.5f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
        ) {
            options.forEachIndexed { index, option ->
                val selected = index == selectedIndex
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(71.dp)
                        .clip(RoundedCornerShape(LocalTravelRadii.current.small))
                        .background(if (selected) IndustrialSecondary.copy(alpha = 0.15f) else Color.Transparent)
                        .border(
                            width = if (selected) 1.dp else 0.dp,
                            color = if (selected) IndustrialSecondary else Color.Transparent,
                            shape = RoundedCornerShape(LocalTravelRadii.current.small),
                        )
                        .clickable { },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = if (selected) IndustrialSecondary else IndustrialOnSurface,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.height(LocalTravelSpacing.current.base))
                    Text(
                        text = stringResource(option.labelRes),
                        color = if (selected) IndustrialSecondary else IndustrialOnSurface,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun CoverSelector() {
    val gridLineColor = IndustrialPrimary.copy(alpha = 0.04f)

    Column(verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
        SectionLabel(R.string.travel_create_cover)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(TripFormSize.CoverHeight)
                .clip(RoundedCornerShape(LocalTravelRadii.current.extraLarge))
                .background(IndustrialContainer.copy(alpha = 0.3f))
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(LocalTravelRadii.current.extraLarge),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                repeat(9) { index ->
                    val y = size.height * index / 8f
                    drawLine(
                        color = gridLineColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, size.height - y),
                    )
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(IndustrialPrimary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Image, contentDescription = null, tint = IndustrialPrimary)
                }
                Spacer(modifier = Modifier.height(LocalTravelSpacing.current.stackSmall))
                Text(
                    text = stringResource(R.string.travel_create_cover_action),
                    color = IndustrialOnSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun RoutePlanningSection() {
    Column(verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
        SectionLabel(R.string.travel_create_route)
        IconInputRow(R.string.travel_create_origin_hint, Icons.Default.LocationOn, IndustrialPrimary)
        IconInputRow(R.string.travel_create_destination_hint, Icons.Default.LocationOn, IndustrialSecondary)
    }
}

@Composable
private fun NodesAndTransportSection() {
    Column(verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
        SectionLabel(R.string.travel_create_nodes)
        GlassPanel(borderColor = IndustrialPrimary.copy(alpha = 0.8f), startAccent = true) {
            Row(
                modifier = Modifier.padding(21.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconBubbleSurface(icon = Icons.Default.Flight)
                Spacer(modifier = Modifier.width(LocalTravelSpacing.current.stackSmall))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.travel_create_node_title),
                        color = IndustrialOnSurface,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = stringResource(R.string.travel_create_node_subtitle),
                        color = IndustrialOnSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = IndustrialPrimary)
                }
            }
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            color = Color.Transparent,
            shape = RoundedCornerShape(LocalTravelRadii.current.extraLarge),
            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.1f)),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = IndustrialOnSurfaceVariant)
                Spacer(modifier = Modifier.width(LocalTravelSpacing.current.base))
                Text(
                    text = stringResource(R.string.travel_create_add_node),
                    color = IndustrialOnSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun PublicTripSection() {
    GlassPanel {
        Row(
            modifier = Modifier.padding(LocalTravelSpacing.current.stackMedium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
        ) {
            IconBubbleSurface(icon = Icons.Default.Public, tone = IndustrialSecondaryContainer)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.travel_create_public_title),
                    color = IndustrialOnSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = stringResource(R.string.travel_create_public_body),
                    color = IndustrialOnSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Switch(
                checked = false,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = IndustrialContainerHigh,
                    uncheckedThumbColor = Color.White,
                ),
            )
        }
    }
}

@Composable
private fun FormTopBar(
    @StringRes titleRes: Int,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(TripFormSize.TopBarHeight),
        color = IndustrialSurface.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = LocalTravelGlass.current.borderAlpha)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LocalTravelSpacing.current.mobilePadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { }) {
                Icon(leadingIcon, contentDescription = null, tint = IndustrialPrimary)
            }
            Text(
                text = stringResource(titleRes),
                color = IndustrialOnSurface,
                style = MaterialTheme.typography.headlineMedium,
            )
            IconButton(onClick = { }) {
                Icon(trailingIcon, contentDescription = null, tint = IndustrialPrimary)
            }
        }
    }
}

@Composable
private fun GradientActionButton(
    text: String,
    modifier: Modifier = Modifier,
    height: Dp = TripFormSize.PrimaryButtonHeight,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Box(
        modifier = modifier
            .height(height)
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(LocalTravelRadii.current.full),
                ambientColor = IndustrialPrimaryContainer.copy(alpha = 0.3f),
                spotColor = IndustrialPrimaryContainer.copy(alpha = 0.3f),
            )
            .clip(RoundedCornerShape(LocalTravelRadii.current.full))
            .background(Brush.horizontalGradient(LocalTravelGradients.current.roaming))
            .clickable { },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = IndustrialOnPrimaryContainer,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun IconInputRow(@StringRes textRes: Int, icon: ImageVector, tint: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        color = IndustrialContainerLow,
        shape = RoundedCornerShape(LocalTravelRadii.current.extraLarge),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LocalTravelSpacing.current.stackSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
            Text(
                text = stringResource(textRes),
                color = IndustrialOutline,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SectionLabel(@StringRes labelRes: Int) {
    Text(
        text = stringResource(labelRes),
        modifier = Modifier.padding(horizontal = LocalTravelSpacing.current.base),
        color = IndustrialPrimary,
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
private fun GlassPanel(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = LocalTravelGlass.current.borderAlpha),
    startAccent: Boolean = false,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (startAccent) {
                    Modifier.border(
                        width = 0.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(LocalTravelRadii.current.large),
                    )
                } else {
                    Modifier
                }
            ),
        color = IndustrialContainer.copy(alpha = LocalTravelGlass.current.cardAlpha),
        shape = RoundedCornerShape(LocalTravelRadii.current.large),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Box {
            if (startAccent) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(4.dp)
                        .heightIn(min = 80.dp)
                        .background(IndustrialPrimary),
                )
            }
            content()
        }
    }
}

@Composable
private fun IconBubbleSurface(
    icon: ImageVector,
    tone: Color = IndustrialContainerHigh,
    alpha: Float = 1f,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(tone.copy(alpha = 0.5f * alpha), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = null, tint = IndustrialPrimary.copy(alpha = alpha))
    }
}

@Composable
private fun BackgroundRouteCard(title: String, icon: ImageVector, progress: Float) {
    GlassPanel(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(LocalTravelSpacing.current.stackMedium),
            verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = IndustrialPrimary.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.width(LocalTravelSpacing.current.stackSmall))
                Text(
                    text = title,
                    color = IndustrialOnSurface.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (progress > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(IndustrialContainerHigh, RoundedCornerShape(LocalTravelRadii.current.full)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .background(
                                Brush.horizontalGradient(listOf(IndustrialPrimary, IndustrialSecondary)),
                                RoundedCornerShape(LocalTravelRadii.current.full),
                            ),
                    )
                }
            }
        }
    }
}

private data class TransportOption(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
)

@Preview
@Composable
private fun NodeEditorScreenPreview() {
    DoraTheme {
        NodeEditorScreen()
    }
}

@Preview
@Composable
private fun CreateTripScreenPreview() {
    DoraTheme {
        CreateTripScreen()
    }
}
