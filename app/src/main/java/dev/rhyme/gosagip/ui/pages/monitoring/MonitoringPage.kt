package dev.rhyme.gosagip.ui.pages.monitoring

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import dev.rhyme.gosagip.ui.pages.CommonPage

@Composable
fun MonitoringPage() {
    CommonPage {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "INCIDENT MONITORING",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            val singapore = LatLng(1.35, 103.87)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(singapore, 10f)
            }

            Spacer(modifier = Modifier.height(16.dp))
            GoogleMap(
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(4.dp)),
                cameraPositionState = cameraPositionState
            )

            Text(
                modifier = Modifier.align(Alignment.End)
                    .padding(end = 24.dp)
                    .padding(vertical = 24.dp),
                text = "STATUS: LIVE",
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            )
        }
    }
}