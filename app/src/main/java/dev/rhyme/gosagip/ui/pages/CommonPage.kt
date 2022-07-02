package dev.rhyme.gosagip.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.rhyme.gosagip.ui.components.AppTitle

@Composable
fun CommonPage(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppTitle(
            modifier = Modifier.padding(24.dp),
            fontSize = 24.sp
        )
        content()
    }
}