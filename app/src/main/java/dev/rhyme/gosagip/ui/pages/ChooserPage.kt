package dev.rhyme.gosagip.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.gosagip.data.model.UserType
import dev.rhyme.gosagip.ui.pages.destinations.LoginPageDestination

@Destination
@Composable
fun ChooserPage(
    navigator: DestinationsNavigator
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigator.navigate(LoginPageDestination(UserType.RIDER))
                }
            ) {
                Text(text = "RIDER")
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navigator.navigate(LoginPageDestination(UserType.AMBULANCE))
                }
            ) {
                Text(text = "AMBULANCE")
            }
        }
    }
}