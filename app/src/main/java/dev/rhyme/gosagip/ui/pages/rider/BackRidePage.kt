package dev.rhyme.gosagip.ui.pages.rider

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import dev.rhyme.gosagip.ui.pages.CommonPage
import dev.rhyme.gosagip.ui.theme.GoSagipTheme

@Destination
@Composable
fun BackRidePage() {
    CommonPage(
        icon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Edit Info")
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Do you have a backride?",
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp
            )
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()

            ) {
                BackRideChooserButton(
                    modifier = Modifier.weight(1f),
                    text = "No",
                    selected = false,
                    onSelect = {}
                )
                BackRideChooserButton(
                    modifier = Modifier.weight(1f),
                    text = "Yes",
                    selected = true,
                    onSelect = {}
                )
            }
        }
    }
}

@Composable
private fun BackRideChooserButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelect: (Boolean) -> Unit
) {

    val selectedAlpha by animateFloatAsState(targetValue = if (selected) 1.0f else 0f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0.3f to Color(0xFF011627),
                    0.9f to Color(0x800973C8)
                ),
                alpha = selectedAlpha
            )
            .clickable { onSelect(!selected) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp
        )
    }
}

@Preview
@Composable
fun BackRideChooserButtonPreview() {
    GoSagipTheme {
        Surface(color = MaterialTheme.colors.background) {
            BackRideChooserButton(
                text = "Yes",
                selected = true,
                onSelect = {}
            )
        }
    }
}

@Preview
@Composable
fun BackRidePagePreview() {
    GoSagipTheme {
        Surface(color = MaterialTheme.colors.background) {
            BackRidePage()
        }
    }
}