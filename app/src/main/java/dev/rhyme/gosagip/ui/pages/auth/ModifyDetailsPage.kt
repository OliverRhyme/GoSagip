package dev.rhyme.gosagip.ui.pages.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.gosagip.data.model.BloodType
import dev.rhyme.gosagip.data.model.Rider
import dev.rhyme.gosagip.ui.pages.CommonPage
import kotlinx.coroutines.launch

@Destination
@Composable
fun ModifyDetailsPage(
    navigator: DestinationsNavigator,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        viewModel.errorState
            .collect {
                Toast.makeText(context, it.ifBlank { "Unknown Error" }, Toast.LENGTH_LONG).show()
            }
    }

    val scope = rememberCoroutineScope()

    val currentUser by viewModel.currentUser.collectAsState()
    val rider = currentUser as? Rider

    val username: MutableState<String> = remember { mutableStateOf(rider?.username ?: "") }
    val fullName: MutableState<String> = remember { mutableStateOf(rider?.name ?: "") }
    val address: MutableState<String> = remember { mutableStateOf(rider?.address ?: "") }
    val plateNumber: MutableState<String> = remember { mutableStateOf(rider?.plateNumber ?: "") }
    val bloodType: MutableState<BloodType?> = remember { mutableStateOf(rider?.bloodType) }
    val emergencyContact: MutableState<String> =
        remember { mutableStateOf(rider?.emergencyContact ?: "") }
    val emergencyContactNumber: MutableState<String> =
        remember { mutableStateOf(rider?.emergencyContactNumber ?: "") }

    CommonPage(
        modifier = Modifier.verticalScroll(
            state = rememberScrollState()
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Edit Information",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        DetailsForm(
            username = username,
            fullName = fullName,
            address = address,
            plateNumber = plateNumber,
            bloodType = bloodType,
            emergencyContact = emergencyContact,
            emergencyContactNumber = emergencyContactNumber,
            submitText = "Update",
        ) {
            scope.launch {
                val success = viewModel.updateRider(
                    username = username.value,
                    fullName = fullName.value,
                    address = address.value,
                    plateNumber = plateNumber.value,
                    bloodType = bloodType.value!!,
                    emergencyContact = emergencyContact.value,
                    emergencyContactNumber = emergencyContactNumber.value
                )
                if (success) {
                    Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}