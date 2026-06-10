package com.muslima.yya.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun QrScannerButton(modifier: Modifier = Modifier, onQrScanned: (String) -> Unit) {
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            if (result.contents != null) {
                onQrScanned(result.contents)
            }
        }
    )

    Button(
        onClick = {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan the Quiz QR Code 🌸")
            options.setBeepEnabled(false)

            // Force portrait mode using the custom CaptureActivity
            options.setCaptureActivity(PortraitCaptureActivity::class.java)
            options.setOrientationLocked(true)

            scanLauncher.launch(options)
        },
        // Apply the modifier passed from the parent screen (to handle bottom alignment)
        // and append the size requirements for the button itself.
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GlassContainer,
            contentColor = TextPrimary
        ),
        border = BorderStroke(1.dp, GlassBorder),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text("Scan QR Code to Join", fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}