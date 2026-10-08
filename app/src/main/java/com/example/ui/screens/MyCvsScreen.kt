package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CVModel
import com.example.ui.components.TestBannerAd
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType

@Composable
fun MyCvsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val savedCvs by viewModel.savedCvs.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground,
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "EVERYTHING YOU'VE CREATED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtle,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Your CVs",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${savedCvs.size} document${if (savedCvs.size == 1) "" else "s"}, stored only on this device.",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }

                    FloatingActionButton(
                        onClick = { viewModel.createNewCv("aura") },
                        containerColor = ForestGreen,
                        contentColor = PureWhite,
                        shape = CircleShape,
                        modifier = Modifier.size(52.dp).testTag("fab_my_cvs_create")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create CV", modifier = Modifier.size(26.dp))
                    }
                }
            }

            if (savedCvs.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = PureWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No CVs yet",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Create your first professional CV from one of our handcrafted templates.",
                                fontSize = 13.sp,
                                color = TextMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.createNewCv("aura") },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                            ) {
                                Text("Create new CV")
                            }
                        }
                    }
                }
            } else {
                items(savedCvs, key = { it.id }) { cv ->
                    CvDocumentCard(
                        cv = cv,
                        onEdit = { viewModel.editCv(cv) },
                        onExport = {
                            viewModel.editCv(cv)
                            viewModel.navigateTo(ScreenType.EXPORT_PREVIEW)
                        },
                        onDuplicate = { viewModel.duplicateCv(cv) },
                        onDelete = { viewModel.deleteCv(cv.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CvDocumentCard(
    cv: CVModel,
    onEdit: () -> Unit,
    onExport: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cv_card_${cv.id}"),
        shape = RoundedCornerShape(20.dp),
        color = PureWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
        shadowElevation = 0.5.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header info with preview thumbnail
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Document miniature thumbnail
                Box(
                    modifier = Modifier
                        .size(width = 68.dp, height = 80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE4EDE7)),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Surface(
                        modifier = Modifier
                            .size(56.dp, 68.dp)
                            .padding(bottom = 2.dp, end = 2.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = PureWhite,
                        shadowElevation = 3.dp
                    ) {
                        Column(modifier = Modifier.padding(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFCEB8A7)))
                                Spacer(modifier = Modifier.width(3.dp))
                                Box(modifier = Modifier.size(24.dp, 3.dp).background(Color(0xFF2C3531)))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE0E0E0)))
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.size(16.dp, 2.dp).background(Color(0xFF777777)))
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(modifier = Modifier.size(28.dp, 2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.size(20.dp, 2.dp).background(Color(0xFF777777)))
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "MODERN CV",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = cv.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = cv.lastEdited,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action buttons row: [Edit] [Export] [Duplicate] [Delete]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Button
                Button(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("edit_cv_${cv.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintBadgeBg,
                        contentColor = ForestGreen
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                // Export Button
                Button(
                    onClick = onExport,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("export_cv_${cv.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintBadgeBg,
                        contentColor = ForestGreen
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                // Duplicate Button
                Surface(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onDuplicate() }
                        .testTag("duplicate_cv_${cv.id}"),
                    color = MintSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Duplicate",
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Delete Button
                Surface(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onDelete() }
                        .testTag("delete_cv_${cv.id}"),
                    color = Color(0xFFFDE8E8),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = Color(0xFFC53030),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}
