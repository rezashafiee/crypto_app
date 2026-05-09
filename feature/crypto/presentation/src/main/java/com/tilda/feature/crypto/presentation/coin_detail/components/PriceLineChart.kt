package com.tilda.feature.crypto.presentation.coin_detail.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tilda.core.presentation.theme.CryptoTheme
import com.tilda.feature.crypto.domain.model.CoinPrice
import com.tilda.feature.crypto.presentation.models.addCurrencySign
import com.tilda.feature.crypto.presentation.models.toDisplayablePrice
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

private val ChartHeight = 300.dp
private val PlotStartPadding = 72.dp
private val PlotTopPadding = 30.dp
private val PlotEndPadding = 16.dp
private val PlotBottomPadding = 48.dp
private val ChartAxisLabelFontSize = 11.sp
private val ChartMarkerTitleFontSize = 12.sp
private val ChartMarkerSubtitleFontSize = 10.sp
private const val Y_LABEL_COUNT = 6

@Composable
internal fun PriceLineChart(
    history: List<CoinPrice>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) return

    val hapticFeedback = LocalHapticFeedback.current
    val range = remember(history) { history.priceRange() }
    val chartReferenceChange = remember(range) { range.span / (Y_LABEL_COUNT - 1) }
    val labelIndices = remember(history) { history.labelIndices() }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("h a", Locale.getDefault()) }

    val lineColor = if (history.last().closingPrice >= history.first().closingPrice) {
        Color(0xFF10B981)
    } else {
        Color(0xFFEF3358)
    }
    val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.72f)
    val axisColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.42f)
    val labelColor = MaterialTheme.colorScheme.outline
    val mutedLabelColor = MaterialTheme.colorScheme.outline
    val markerSurface = MaterialTheme.colorScheme.surfaceContainerHighest

    var selectedIndex by remember(history) { mutableStateOf<Int?>(null) }
    var chartSize by remember { mutableStateOf(IntSize.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(ChartHeight)
            .onSizeChanged { chartSize = it }
            .pointerInput(history, chartSize) {
                fun selectNearestPoint(position: Offset) {
                    if (chartSize.width == 0 || history.isEmpty()) return

                    val nextIndex = nearestPointIndex(
                        touchX = position.x,
                        pointCount = history.size,
                        chartWidth = chartSize.width.toFloat(),
                        startPadding = PlotStartPadding.toPx(),
                        endPadding = PlotEndPadding.toPx(),
                    )

                    if (selectedIndex != nextIndex) {
                        selectedIndex = nextIndex
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }

                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val pointerId = down.id
                    selectNearestPoint(down.position)

                    do {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == pointerId }

                        if (change != null) {
                            selectNearestPoint(change.position)
                        }
                    } while (event.changes.any { it.id == pointerId && it.pressed })

                    selectedIndex = null
                }
            }
    ) {
        val plot = PlotBounds(
            left = PlotStartPadding.toPx(),
            top = PlotTopPadding.toPx(),
            right = size.width - PlotEndPadding.toPx(),
            bottom = size.height - PlotBottomPadding.toPx(),
        )

        if (plot.width <= 0f || plot.height <= 0f) return@Canvas

        val points = history.mapIndexed { index, price ->
            Offset(
                x = plot.xFor(index, history.size),
                y = plot.yFor(price.closingPrice, range),
            )
        }

        drawChartGrid(
            plot = plot,
            range = range,
            referenceChange = chartReferenceChange,
            gridColor = gridColor,
            axisColor = axisColor,
            labelColor = labelColor,
        )
        drawTimeLabels(
            plot = plot,
            points = points,
            history = history,
            labelIndices = labelIndices,
            labelColor = labelColor,
            timeFormatter = timeFormatter,
        )
        drawLineChart(
            points = points,
            lineColor = lineColor,
        )

        selectedIndex?.let { index ->
            drawSelectedPoint(
                index = index,
                plot = plot,
                referenceChange = chartReferenceChange,
                points = points,
                history = history,
                lineColor = lineColor,
                labelColor = labelColor,
                mutedLabelColor = mutedLabelColor,
                markerSurface = markerSurface,
                timeFormatter = timeFormatter,
            )
        }
    }
}

private data class PriceRange(
    val min: Double,
    val max: Double
) {
    val span: Double = max - min
}

private data class PlotBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) {
    val width: Float = right - left
    val height: Float = bottom - top

    fun xFor(index: Int, pointCount: Int): Float {
        if (pointCount <= 1) return left + width / 2f
        return left + width * index / (pointCount - 1)
    }

    fun yFor(value: Double, range: PriceRange): Float {
        if (range.span == 0.0) return top + height / 2f
        val progress = ((value - range.min) / range.span).toFloat()
        return bottom - height * progress
    }
}

private fun List<CoinPrice>.priceRange(): PriceRange {
    val minPrice = minOf { it.closingPrice }
    val maxPrice = maxOf { it.closingPrice }
    val padding = if (minPrice == maxPrice) {
        max(abs(maxPrice) * 0.02, 1.0)
    } else {
        (maxPrice - minPrice) * 0.08
    }

    return PriceRange(
        min = minPrice - padding,
        max = maxPrice + padding,
    )
}

private fun List<CoinPrice>.labelIndices(maxLabels: Int = 6): List<Int> {
    if (isEmpty()) return emptyList()
    if (size <= maxLabels) return indices.toList()

    val steps = maxLabels - 1
    return (0..steps)
        .map { step -> (lastIndex * step.toFloat() / steps).roundToInt() }
        .distinct()
}

private fun nearestPointIndex(
    touchX: Float,
    pointCount: Int,
    chartWidth: Float,
    startPadding: Float,
    endPadding: Float
): Int {
    if (pointCount <= 1) return 0

    val plotWidth = chartWidth - startPadding - endPadding
    if (plotWidth <= 0f) return 0

    val progress = ((touchX - startPadding) / plotWidth).coerceIn(0f, 1f)
    return (progress * (pointCount - 1)).roundToInt().coerceIn(0, pointCount - 1)
}

private fun DrawScope.drawChartGrid(
    plot: PlotBounds,
    range: PriceRange,
    referenceChange: Double,
    gridColor: Color,
    axisColor: Color,
    labelColor: Color
) {
    val dash = PathEffect.dashPathEffect(
        intervals = floatArrayOf(12.dp.toPx(), 8.dp.toPx())
    )

    repeat(Y_LABEL_COUNT) { index ->
        val progress = index / (Y_LABEL_COUNT - 1).toFloat()
        val y = plot.top + plot.height * progress
        val value = range.max - range.span * progress

        drawLine(
            color = gridColor,
            start = Offset(plot.left, y),
            end = Offset(plot.right, y),
            strokeWidth = 1.dp.toPx(),
            pathEffect = dash,
        )
        drawTextLabel(
            text = value.toPriceLabel(referenceChange = referenceChange),
            x = 12.dp.toPx(),
            baseline = y + 5.dp.toPx(),
            color = labelColor,
            fontSize = ChartAxisLabelFontSize,
        )
    }

    drawLine(
        color = axisColor,
        start = Offset(plot.left, plot.top),
        end = Offset(plot.left, plot.bottom),
        strokeWidth = 1.dp.toPx(),
    )
    drawLine(
        color = axisColor,
        start = Offset(plot.left, plot.bottom),
        end = Offset(plot.right, plot.bottom),
        strokeWidth = 1.dp.toPx(),
    )
}

private fun DrawScope.drawTimeLabels(
    plot: PlotBounds,
    points: List<Offset>,
    history: List<CoinPrice>,
    labelIndices: List<Int>,
    labelColor: Color,
    timeFormatter: DateTimeFormatter
) {
    labelIndices.forEach { index ->
        val point = points.getOrNull(index) ?: return@forEach
        drawLine(
            color = labelColor.copy(alpha = 0.28f),
            start = Offset(point.x, plot.bottom),
            end = Offset(point.x, plot.bottom + 8.dp.toPx()),
            strokeWidth = 1.dp.toPx(),
        )
        drawTextLabel(
            text = history[index].dateTime.format(timeFormatter),
            x = point.x,
            baseline = size.height - 14.dp.toPx(),
            color = labelColor,
            fontSize = ChartAxisLabelFontSize,
            align = Paint.Align.CENTER,
        )
    }
}

private fun DrawScope.drawLineChart(
    points: List<Offset>,
    lineColor: Color
) {
    if (points.size == 1) {
        drawCircle(
            color = lineColor,
            radius = 5.dp.toPx(),
            center = points.first(),
        )
        return
    }

    val path = Path().apply {
        points.forEachIndexed { index, point ->
            if (index == 0) {
                moveTo(point.x, point.y)
            } else {
                lineTo(point.x, point.y)
            }
        }
    }

    drawPath(
        path = path,
        color = lineColor.copy(alpha = 0.18f),
        style = Stroke(
            width = 10.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        ),
    )
    drawPath(
        path = path,
        color = lineColor,
        style = Stroke(
            width = 3.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        ),
    )
}

private fun DrawScope.drawSelectedPoint(
    index: Int,
    plot: PlotBounds,
    referenceChange: Double,
    points: List<Offset>,
    history: List<CoinPrice>,
    lineColor: Color,
    labelColor: Color,
    mutedLabelColor: Color,
    markerSurface: Color,
    timeFormatter: DateTimeFormatter
) {
    val point = points.getOrNull(index) ?: return
    val dash = PathEffect.dashPathEffect(
        intervals = floatArrayOf(8.dp.toPx(), 8.dp.toPx())
    )

    drawLine(
        color = lineColor.copy(alpha = 0.4f),
        start = Offset(point.x, plot.top),
        end = Offset(point.x, plot.bottom),
        strokeWidth = 1.dp.toPx(),
        pathEffect = dash,
    )
    drawCircle(
        color = lineColor.copy(alpha = 0.12f),
        radius = 32.dp.toPx(),
        center = point,
    )
    drawCircle(
        color = lineColor.copy(alpha = 0.22f),
        radius = 20.dp.toPx(),
        center = point,
    )
    drawCircle(
        color = lineColor,
        radius = 7.dp.toPx(),
        center = point,
    )
    drawCircle(
        color = markerSurface,
        radius = 3.dp.toPx(),
        center = point,
    )

    drawMarkerLabel(
        point = point,
        text = history[index].closingPrice.toPriceLabel(referenceChange = referenceChange),
        subtext = history[index].dateTime.format(timeFormatter),
        labelColor = labelColor,
        mutedLabelColor = mutedLabelColor,
        markerSurface = markerSurface,
        plot = plot,
    )
}

private fun DrawScope.drawMarkerLabel(
    point: Offset,
    text: String,
    subtext: String,
    labelColor: Color,
    mutedLabelColor: Color,
    markerSurface: Color,
    plot: PlotBounds
) {
    val titlePaint = textPaint(
        color = labelColor,
        fontSize = ChartMarkerTitleFontSize,
        isBold = true,
    )
    val subtitlePaint = textPaint(
        color = mutedLabelColor,
        fontSize = ChartMarkerSubtitleFontSize,
    )
    val horizontalPadding = 12.dp.toPx()
    val verticalPadding = 8.dp.toPx()
    val labelGap = 4.dp.toPx()
    val bubbleWidth = max(
        titlePaint.measureText(text),
        subtitlePaint.measureText(subtext),
    ) + horizontalPadding * 2f
    val bubbleHeight = 42.dp.toPx()
    val maxBubbleLeft = max(plot.left, plot.right - bubbleWidth)
    val bubbleLeft = (point.x - bubbleWidth / 2f)
        .coerceIn(plot.left, maxBubbleLeft)
    val bubbleTop = if (point.y - bubbleHeight - 18.dp.toPx() > plot.top) {
        point.y - bubbleHeight - 18.dp.toPx()
    } else {
        point.y + 18.dp.toPx()
    }.coerceIn(plot.top, plot.bottom - bubbleHeight)

    drawRoundRect(
        color = markerSurface.copy(alpha = 0.96f),
        topLeft = Offset(bubbleLeft, bubbleTop),
        size = Size(bubbleWidth, bubbleHeight),
        cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
    )
    drawContext.canvas.nativeCanvas.drawText(
        text,
        bubbleLeft + horizontalPadding,
        bubbleTop + verticalPadding + ChartMarkerTitleFontSize.toPx(),
        titlePaint,
    )
    drawContext.canvas.nativeCanvas.drawText(
        subtext,
        bubbleLeft + horizontalPadding,
        bubbleTop + verticalPadding +
            ChartMarkerTitleFontSize.toPx() +
            ChartMarkerSubtitleFontSize.toPx() +
            labelGap,
        subtitlePaint,
    )
}

private fun DrawScope.drawTextLabel(
    text: String,
    x: Float,
    baseline: Float,
    color: Color,
    fontSize: TextUnit,
    align: Paint.Align = Paint.Align.LEFT,
    isBold: Boolean = false
) {
    drawContext.canvas.nativeCanvas.drawText(
        text,
        x,
        baseline,
        textPaint(
            color = color,
            fontSize = fontSize,
            align = align,
            isBold = isBold,
        ),
    )
}

private fun DrawScope.textPaint(
    color: Color,
    fontSize: TextUnit,
    align: Paint.Align = Paint.Align.LEFT,
    isBold: Boolean = false
) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.color = color.toArgb()
    textSize = fontSize.toPx()
    textAlign = align
    typeface = if (isBold) {
        Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    } else {
        Typeface.DEFAULT
    }
}

private fun Double.toPriceLabel(referenceChange: Double = this): String {
    return toDisplayablePrice(referenceChange = referenceChange).addCurrencySign().formatted
}

internal val previewLineChartHistory: List<CoinPrice> = listOf(
    120210.0,
    120620.0,
    120160.0,
    120280.0,
    120070.0,
    119980.0,
    119760.0,
    120120.0,
    120390.0,
    120520.0,
    120430.0,
    120480.0,
    120470.0,
    120990.0,
    120180.0,
    119820.0,
    119990.0,
    120610.0,
    120900.0,
    120705.0,
    120210.0,
    120330.0,
    120610.0,
).mapIndexed { index, closingPrice ->
    CoinPrice(
        openingPrice = closingPrice - 40.0,
        highestPrice = closingPrice + 120.0,
        lowestPrice = closingPrice - 120.0,
        closingPrice = closingPrice,
        dateTime = ZonedDateTime.now().minusHours((22 - index).toLong()),
        volume = 10_000.0 + index * 120.0,
    )
}

@PreviewLightDark
@Composable
private fun PriceLineChartPreview() {
    CryptoTheme {
        PriceLineChart(
            history = previewLineChartHistory
        )
    }
}
