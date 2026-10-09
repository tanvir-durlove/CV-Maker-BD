package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userName by viewModel.userName.collectAsState()
    val savedCvs by viewModel.savedCvs.collectAsState()
    val unlockedTemplates by viewModel.unlockedTemplates.collectAsState()
    val updateReminders by viewModel.updateReminders.collectAsState()

    var nameInput by remember(userName) { mutableStateOf(userName) }
    var isEditingName by remember { mutableStateOf(false) }

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showExportSummaryDialog by remember { mutableStateOf(false) }
    var showFaqDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Title & Subtitle
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo_512),
                        contentDescription = "CV Maker Logo",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Column {
                        Text(
                            text = "PREFERENCES & STORAGE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtle,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Settings",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Manage your profile, preferences, and offline data.",
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }

            // SECTION 1: USER PROFILE & WORKSPACE
            item {
                SettingsSectionHeader(title = "PROFILE & PERSONALIZATION")
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // User Profile Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (userName.isNotBlank()) userName else "Tap to set your name",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = "${savedCvs.size} saved CV${if (savedCvs.size == 1) "" else "s"} · ${unlockedTemplates.size} unlocked template${if (unlockedTemplates.size == 1) "" else "s"}",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Button(
                                onClick = { isEditingName = !isEditingName },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isEditingName) ForestGreen else MintBadgeBg,
                                    contentColor = if (isEditingName) PureWhite else ForestGreen
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp).testTag("edit_name_toggle_btn")
                            ) {
                                Text(
                                    text = if (isEditingName) "Done" else "Edit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Expandable inline edit box
                        AnimatedVisibility(visible = isEditingName) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MintSurface, RoundedCornerShape(12.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "Full name for new CVs",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextDark
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = nameInput,
                                    onValueChange = {
                                        nameInput = it
                                        viewModel.setUserName(it)
                                    },
                                    placeholder = { Text("Enter your name", color = TextSubtle, fontSize = 13.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("settings_name_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForestGreen,
                                        unfocusedBorderColor = LightBorder,
                                        focusedContainerColor = PureWhite,
                                        unfocusedContainerColor = PureWhite
                                    ),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Automatically used as the default author name when creating new CVs.",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 2: DOCUMENT & EXPORT PREFERENCES
            item {
                SettingsSectionHeader(title = "EXPORT & DOCUMENT SETUP")
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                    ) {
                        SettingsNavRow(
                            icon = Icons.Outlined.FileDownload,
                            title = "Export format & standards",
                            subtitle = "ISO 216 A4 (595 × 842 pt) · Vector PDF",
                            badgeText = "A4 Standard",
                            onClick = { showExportSummaryDialog = true }
                        )

                        HorizontalDivider(color = LightBorder, thickness = 0.8.dp)

                        SettingsToggleRow(
                            icon = Icons.Default.Notifications,
                            title = "Update reminders",
                            subtitle = "Gentle local notifications to keep your career history fresh",
                            checked = updateReminders,
                            testTag = "update_reminders_switch",
                            onCheckedChange = { viewModel.setUpdateReminders(it) }
                        )
                    }
                }
            }

            // SECTION 3: PRIVACY, DATA & SECURITY
            item {
                SettingsSectionHeader(title = "DATA PRIVACY & LOCAL STORAGE")
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                    ) {
                        SettingsNavRow(
                            icon = Icons.Outlined.Shield,
                            title = "Privacy policy & safety",
                            subtitle = "Zero cloud uploads · 100% on-device sandboxed storage",
                            badgeText = "Offline First",
                            onClick = { showPrivacyPolicyDialog = true }
                        )

                        HorizontalDivider(color = LightBorder, thickness = 0.8.dp)

                        SettingsNavRow(
                            icon = Icons.AutoMirrored.Outlined.HelpOutline,
                            title = "Frequently asked questions",
                            subtitle = "ATS compatibility, font rendering & storage tips",
                            onClick = { showFaqDialog = true }
                        )
                    }
                }
            }

            // SECTION 4: SUPPORT & ABOUT
            item {
                SettingsSectionHeader(title = "SUPPORT & ABOUT")
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("Developer & Support", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Text("NextGen Tools · nextgentoolbd@gmail.com", fontSize = 12.sp, color = TextMuted)
                                }
                            }

                            Button(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:nextgentoolbd@gmail.com")
                                            putExtra(Intent.EXTRA_SUBJECT, "CV Maker - Support Request")
                                        }
                                        context.startActivity(intent)
                                    } catch (_: Exception) {
                                        Toast.makeText(context, "Email support: nextgentoolbd@gmail.com", Toast.LENGTH_LONG).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintBadgeBg,
                                    contentColor = ForestGreen
                                ),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp).testTag("contact_support_btn")
                            ) {
                                Text("Contact", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(color = LightBorder, thickness = 0.8.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("App version", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark)
                                Text("Version 1.0.0 (Production Release)", fontSize = 11.sp, color = TextMuted)
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MintBadgeBg
                            ) {
                                Text("Up to date", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }
                    }
                }
            }

            // SECTION 5: DANGER ZONE (DATA RESET)
            item {
                SettingsSectionHeader(title = "DANGER ZONE")
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFEF2F2),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFB91C1C), modifier = Modifier.size(22.dp))
                            Text(
                                text = "Reset Application Data",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Permanently removes all saved CV documents (${savedCvs.size}), resets unlocked templates, and clears your stored workspace name from this phone.",
                            fontSize = 12.sp,
                            color = Color(0xFF7F1D1D),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { showDeleteConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFDC2626),
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("delete_local_data_btn")
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Erase all local data", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Footer note
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Powered by NextGen Tool bd · Built with Kotlin & Compose",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSubtle
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // 1. DELETE CONFIRMATION DIALOG
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
                    Text("Delete all local data?", fontWeight = FontWeight.Bold, color = TextDark)
                }
            },
            text = {
                Text(
                    "Are you sure? This will delete all ${savedCvs.size} saved CVs, your photo avatars, and reset templates. This cannot be undone.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllData()
                        showDeleteConfirmDialog = false
                        Toast.makeText(context, "All local data has been erased.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Delete Everything", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = TextDark)
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 2. EXPORT PREFERENCES SUMMARY DIALOG
    if (showExportSummaryDialog) {
        AlertDialog(
            onDismissRequest = { showExportSummaryDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.FileDownload, contentDescription = null, tint = ForestGreen)
                    Text("Export Standards", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Paper Size: International A4 (595 pt × 842 pt)", fontSize = 13.sp, color = TextDark)
                    Text("• Typography: Scaled for high DPI print with ATS safety", fontSize = 13.sp, color = TextDark)
                    Text("• Document Security: Rendered on-device without cloud transmission", fontSize = 13.sp, color = TextDark)
                    Text("• Destination: Device Downloads, Android Print Spooler, or Direct Share", fontSize = 13.sp, color = TextDark)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showExportSummaryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Got it")
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 3. PRIVACY POLICY DIALOG
    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = ForestGreen)
                    Text("Privacy Policy", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "NextGen Tools · CV Maker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = ForestGreen
                    )
                    Text(
                        text = "1. Data Handling & Security\n" +
                               "All resume information (contact info, work history, education, skills, and photos) is processed and stored strictly on your local device. We operate no remote databases and never read, copy, or share your resume data.\n\n" +
                               "2. Document Generation & Export\n" +
                               "PDF generation runs entirely offline using native Android print and graphics engines. Exported files remain on-device unless you choose to share them via system dialogs.\n\n" +
                               "3. Third-Party Ad Services (Google AdMob)\n" +
                               "To offer free templates and tools, this app uses Google AdMob. Ad networks may collect non-personal device identifiers and telemetry to serve ads in compliance with Google Play policies.\n\n" +
                               "4. Developer & Support Contact\n" +
                               "Organization: NextGen Tools\n" +
                               "Contact Email: nextgentoolbd@gmail.com",
                        fontSize = 12.sp,
                        color = TextDark,
                        lineHeight = 17.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Got it")
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 4. FAQ DIALOG
    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, tint = ForestGreen)
                    Text("FAQ & Guidance", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Q: Are these templates ATS-friendly?", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                    Text("A: Yes! The 'Clarity' and 'Summit' templates are built specifically for Applicant Tracking Systems with linear text flows and standard section hierarchies.", fontSize = 12.sp, color = TextMuted)

                    Text("Q: Where are my PDF files stored?", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                    Text("A: When you click 'Save PDF to Phone Storage' on the Export page, you choose the exact folder (Downloads, Drive, Documents) via Android's file manager.", fontSize = 12.sp, color = TextMuted)

                    Text("Q: Can I reorder resume sections?", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                    Text("A: Absolutely. In Step 5 (More sections) of the editor, use the Up and Down arrow buttons to arrange sections in any order you prefer.", fontSize = 12.sp, color = TextMuted)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFaqDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Close")
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = TextSubtle,
        letterSpacing = 1.1.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun SettingsNavRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(19.dp))
            }
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(subtitle, fontSize = 11.5.sp, color = TextMuted, lineHeight = 15.sp)
            }
        }

        if (badgeText != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MintBadgeBg
            ) {
                Text(
                    text = badgeText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        } else {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSubtle, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(19.dp))
            }
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(subtitle, fontSize = 11.5.sp, color = TextMuted, lineHeight = 15.sp)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PureWhite,
                checkedTrackColor = ForestGreen,
                uncheckedThumbColor = TextSubtle,
                uncheckedTrackColor = LightBorder
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
