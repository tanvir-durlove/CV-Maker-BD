package com.example.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CVModel
import com.example.ui.components.TestBannerAd
import com.example.ui.theme.*
import com.example.util.PdfGenerator
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ExportPreviewScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeCv by viewModel.activeCv.collectAsState()

    var selectedFormat by remember { mutableStateOf("PDF") } // "PDF" or "Print"
    var previewBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isGeneratingPdf by remember { mutableStateOf(false) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val pdfFile = PdfGenerator.generatePdf(context, activeCv)
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    pdfFile.inputStream().use { input -> input.copyTo(out) }
                }
                Toast.makeText(context, "Saved to your device storage!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Save error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    BackHandler {
        viewModel.navigateTo(ScreenType.EDITOR)
    }

    // Generate real-time live preview bitmap of the exact A4 CV
    LaunchedEffect(activeCv) {
        withContext(Dispatchers.Default) {
            val bmp = PdfGenerator.generateBitmap(activeCv, scale = 1.0f)
            withContext(Dispatchers.Main) {
                previewBitmap = bmp
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(ScreenType.EDITOR) }
                        .testTag("export_back_button"),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                TestBannerAd(
                    sponsorName = "Land your next interview",
                    actionLabel = "Learn more"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "FINAL CHECK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSubtle,
                        letterSpacing = 1.1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Your CV is ready",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Review the final document before saving it to your device.",
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                }
            }

            // Ready to share card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
                    shadowElevation = 0.5.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Success circle check
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MintBadgeBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Ready to share",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your information is complete enough for a strong first draft.",
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }

                        // Info item chips
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MintSurface
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Outlined.Description, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                                    Text(activeCv.title, fontSize = 13.sp, color = TextDark)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                                    Text("Stored on device", fontSize = 13.sp, color = TextDark)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Outlined.Info, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                                    Text("Includes a small sponsor footer", fontSize = 13.sp, color = TextDark)
                                }
                            }
                        }

                        Text(
                            text = "File format",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        // Format selection: PDF vs Print
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // PDF Option
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedFormat = "PDF" },
                                shape = RoundedCornerShape(12.dp),
                                color = PureWhite,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selectedFormat == "PDF") ForestGreen else LightBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = TextDark, modifier = Modifier.size(20.dp))
                                        Column {
                                            Text("PDF", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                            Text("Best for email and applications", fontSize = 11.sp, color = TextMuted)
                                        }
                                    }

                                    if (selectedFormat == "PDF") {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(ForestGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }

                            // Print Option
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedFormat = "Print" },
                                shape = RoundedCornerShape(12.dp),
                                color = PureWhite,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (selectedFormat == "Print") ForestGreen else LightBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(Icons.Default.Print, contentDescription = null, tint = TextDark, modifier = Modifier.size(20.dp))
                                        Column {
                                            Text("Print", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                            Text("Use your device print dialog", fontSize = 11.sp, color = TextMuted)
                                        }
                                    }

                                    if (selectedFormat == "Print") {
                                        Box(
                                            modifier = Modifier
                                                .size(22.dp)
                                                .clip(CircleShape)
                                                .background(ForestGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = PureWhite, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }

                        // Save / Export Action Buttons Row
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    isGeneratingPdf = true
                                    try {
                                        val pdfFile = PdfGenerator.generatePdf(context, activeCv)
                                        if (selectedFormat == "Print") {
                                            PdfGenerator.printPdf(context, pdfFile)
                                        } else {
                                            PdfGenerator.sharePdf(context, pdfFile)
                                            Toast.makeText(context, "PDF generated successfully: ${pdfFile.name}", Toast.LENGTH_LONG).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Export error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isGeneratingPdf = false
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("save_as_pdf_action_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ForestGreen,
                                    contentColor = PureWhite
                                )
                            ) {
                                Icon(
                                    imageVector = if (selectedFormat == "Print") Icons.Default.Print else Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedFormat == "Print") "Print Document" else "Share / Open PDF",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val safeFileName = "${activeCv.title.ifBlank { "My_CV" }.replace("\\s+".toRegex(), "_")}.pdf"
                                    createDocumentLauncher.launch(safeFileName)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("save_to_downloads_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = ForestGreen
                                )
                            ) {
                                Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Save PDF to Phone Storage", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(
                            text = "Nothing is uploaded. Export happens through your device's print tools.",
                            fontSize = 11.sp,
                            color = TextSubtle,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Real-time Visual Document Preview Section
            item {
                Text(
                    text = "DOCUMENT PREVIEW",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSubtle,
                    letterSpacing = 1.1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .testTag("realtime_pdf_preview_container"),
                    color = PureWhite
                ) {
                    if (previewBitmap != null) {
                        Image(
                            bitmap = previewBitmap!!.asImageBitmap(),
                            contentDescription = "Real-time PDF Preview",
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = ForestGreen)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
