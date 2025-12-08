package com.example.baseproject.activities

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CircularAlphabetGameScreen() {
    // Danh sách chữ cái A-Z
    val alphabet = ('A'..'Z').toList()

    // State lưu trữ các chữ cái đã được chọn (để làm mờ)
    val selectedIndices = remember { mutableStateListOf<Int>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0099CC)) // Màu nền giả lập đại dương
    ) {
        // Vòng tròn chứa các chữ cái
        CircularLayout(
            radius = 140.dp, // Bán kính vòng tròn chữ cái
            modifier = Modifier.align(Alignment.Center)
        ) {
            alphabet.forEachIndexed { index, char ->
                // Kiểm tra xem chữ này đã bấm chưa
                val isSelected = selectedIndices.contains(index)

                LetterItem(
                    char = char,
                    isSelected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            selectedIndices.add(index)
                        } else {
                            // Logic nếu muốn bỏ chọn (reset alpha)
                            // selectedIndices.remove(index)
                        }
                    }
                )
            }
        }

        // Nút GO / TAP ở giữa
        CenterButton(modifier = Modifier.align(Alignment.Center))

        // Nút Reset ở dưới cùng
        ResetButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            onReset = { selectedIndices.clear() }
        )
    }
}

@Composable
fun LetterItem(
    char: Char,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp) // Kích thước mỗi ô chữ
            // Hiệu ứng làm mờ alpha nếu đã chọn
            .alpha(if (isSelected) 0.3f else 1.0f)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Text(
            text = char.toString(),
            color = Color(0xFFD86D2C), // Màu chữ cam nâu
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun CircularLayout(
    radius: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // Đo kích thước các phần tử con (các chữ cái)
        val placeables = measurables.map { it.measure(constraints) }

        // Kích thước của Container (lấy theo bán kính * 2 + padding dư ra chút)
        val diameter = (radius * 2).roundToPx()
        val layoutSize = diameter + 100 // Cộng thêm chút không gian cho item

        layout(layoutSize, layoutSize) {
            val center = layoutSize / 2f
            val radiusPx = radius.toPx()

            val stepAngle = 360f / placeables.size // Góc chia đều cho mỗi chữ

            placeables.forEachIndexed { index, placeable ->
                // Tính góc cho phần tử hiện tại (Bắt đầu từ -90 độ để A nằm trên cùng hoặc điều chỉnh tùy ý)
                // Trong ảnh 'A' nằm ở khoảng 11h, nên ta có thể điều chỉnh góc bắt đầu
                val angleInDegrees = (index * stepAngle) - 105f
                val angleInRadians = Math.toRadians(angleInDegrees.toDouble())

                // Tính toán tọa độ X, Y dựa trên công thức lượng giác
                val x = center + (radiusPx * cos(angleInRadians)).toFloat() - (placeable.width / 2)
                val y = center + (radiusPx * sin(angleInRadians)).toFloat() - (placeable.height / 2)

                // Đặt phần tử vào vị trí và xoay nó hướng ra ngoài tâm
                placeable.placeRelativeWithLayer(x = x.toInt(), y = y.toInt()) {
                    // Xoay item theo góc của nó + 90 độ để đáy chữ hướng về tâm
                    rotationZ = angleInDegrees + 90f
                }
            }
        }
    }
}

// UI giả lập nút giữa
@Composable
fun CenterButton(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(160.dp)
            .background(Color(0xFF4FC3F7).copy(alpha = 0.5f), CircleShape) // Vòng sáng ngoài
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFF039BE5), CircleShape) // Vòng đậm trong
                .border(4.dp, Color(0xFFFFB74D), CircleShape)
        ) {
            Text("GO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

// UI giả lập nút Reset
@Composable
fun ResetButton(modifier: Modifier, onReset: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(200.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(Color(0xFFFFB74D))
            .clickable { onReset() }
    ) {
        Text("RESET", color = Color.White, fontWeight = FontWeight.Bold)
    }
}