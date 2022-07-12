package dev.rhyme.gosagip.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Palette = darkColors(
    primary = Color(0xFFD00000),
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color(0xFF011627),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color(0xFFF6F7F8)
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun GoSagipTheme(content: @Composable () -> Unit) {
    val colors = Palette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}