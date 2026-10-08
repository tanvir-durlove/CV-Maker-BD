package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val updateReminders by viewModel.updateReminders.collectAsState()
    var nameInput by remember(userName) { mutableStateOf(userName) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.app_logo_512),
                        contentDescription = "CV Maker Logo",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Column {
                        Text(
                            text = "PREFERENCES",
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
                    text = "Manage your workspace and on-device data.",
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }

            // Main Settings Card
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
                        verticalArrangement = Arrangement.spacedBy(22.dp)
                    ) {
                        // 1. Your name
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
                                }
                                Column {
                                    Text("Your name", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Text("Used to personalize your workspace.", fontSize = 12.sp, color = TextMuted)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = nameInput,
                                onValueChange = {
                                    nameInput = it
                                    viewModel.setUserName(it)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("settings_name_input"),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    unfocusedBorderColor = LightBorder,
                                    focusedContainerColor = PureWhite,
                                    unfocusedContainerColor = PureWhite
                                ),
                                singleLine = true
                            )
                        }

                        Divider(color = LightBorder, thickness = 0.8.dp)

                        // 2. Privacy & data
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("Privacy & data", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "CVs never leave this device. There is no account or cloud sync.",
                                        fontSize = 12.sp,
                                        color = TextMuted,
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MintBadgeBg
                                    ) {
                                        Text(
                                            "Local only",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Divider(color = LightBorder, thickness = 0.8.dp)

                        // 3. Export preference
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Outlined.FileDownload, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("Export preference", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Text("PDF format · A4 paper · Sponsor footer", fontSize = 12.sp, color = TextMuted)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MintBadgeBg
                            ) {
                                Text(
                                    "Default",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Divider(color = LightBorder, thickness = 0.8.dp)

                        // 4. Update reminders toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Notifications, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("Update reminders", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Text("Occasional device reminders to keep your CV current.", fontSize = 12.sp, color = TextMuted)
                                }
                            }

                            Switch(
                                checked = updateReminders,
                                onCheckedChange = { viewModel.setUpdateReminders(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PureWhite,
                                    checkedTrackColor = ForestGreen,
                                    uncheckedThumbColor = TextSubtle,
                                    uncheckedTrackColor = LightBorder
                                ),
                                modifier = Modifier.testTag("update_reminders_switch")
                            )
                        }
                        HorizontalDivider(color = LightBorder, thickness = 0.8.dp)

                        // 5. Privacy Policy Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                }
                                Column {
                                    Text("Privacy Policy", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                    Text("How we handle data, ads, and your privacy.", fontSize = 12.sp, color = TextMuted)
                                }
                            }

                            Button(
                                onClick = { showPrivacyPolicyDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintBadgeBg,
                                    contentColor = ForestGreen
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp).testTag("view_privacy_policy_btn")
                            ) {
                                Text("View", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(color = LightBorder, thickness = 0.8.dp)

                        // 6. Developer Information
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(MintBadgeBg),
                                    contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Developer & Support", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Text("NextGen Tools", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                                Text("nextgentoolbd@gmail.com", fontSize = 12.sp, color = ForestGreen)
                            }
                        }
                    }
                }
            }

            // Reset Vitae Card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
                    shadowElevation = 0.5.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Reset Vitae",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Remove your name, unlocked templates, and every saved CV from this device.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showDeleteConfirmDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFDE8E8),
                                contentColor = Color(0xFFC53030)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("delete_local_data_btn")
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Delete local data", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Footer note: Powered by NextGen Tool bd
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Powered by NextGen Tool bd",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSubtle
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete all local data?") },
            text = { Text("This will permanently remove all CVs and reset unlocked templates on this device.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllData()
                        showDeleteConfirmDialog = false
                    }
                ) {
                    Text("Delete", color = Color(0xFFC53030), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            }
        )
    }
}
