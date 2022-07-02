package dev.rhyme.gosagip.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun AppTitle(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(
        modifier = modifier,
        fontSize = fontSize,
        text = buildAnnotatedString {
            append("GO")
            withStyle(
                style = SpanStyle(
                    color = Color(0xFFD00000),
                    fontWeight = FontWeight.ExtraBold
                )
            ) {
                append("SAGIP")
            }
        },
        style = TextStyle(fontWeight = FontWeight.Bold),
    )
}