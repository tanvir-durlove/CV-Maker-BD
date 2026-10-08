package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TemplateInfo
import com.example.ui.components.TemplateCard
import com.example.ui.components.TestBannerAd
import com.example.ui.components.TestInlineSponsorAd
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@Composable
fun TemplatesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val unlockedTemplates by viewModel.unlockedTemplates.collectAsState()
    val allTemplates = viewModel.availableTemplates

    var selectedCategory by remember { mutableStateOf("All formats") }
    val categories = listOf("All formats", "Modern", "ATS-friendly", "Regional")

    val filteredTemplates = remember(selectedCategory, allTemplates) {
        if (selectedCategory == "All formats") {
            allTemplates
        } else {
            allTemplates.filter { it.category == selectedCategory }
        }
    }

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
                            text = "START WITH CONFIDENCE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtle,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Choose your format",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pick a structure that fits your application.",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }

                    FloatingActionButton(
                        onClick = { viewModel.createNewCv("aura") },
                        containerColor = ForestGreen,
                        contentColor = PureWhite,
                        shape = CircleShape,
                        modifier = Modifier.size(52.dp).testTag("fab_templates_create")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create CV", modifier = Modifier.size(26.dp))
                    }
                }
            }

            // Category Filter Pills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategory
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedCategory = cat }
                                .testTag("category_pill_$cat"),
                            color = if (isSelected) ForestGreen else PureWhite,
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, LightBorder) else null,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = cat,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PureWhite else TextMuted,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }

            // Templates list with detailed features (A4 ready, Editable)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    val chunkedTemplates = filteredTemplates.chunked(2)
                    for (rowItems in chunkedTemplates) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            for (tmpl in rowItems) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    TemplateCard(
                                        template = tmpl,
                                        isUnlocked = unlockedTemplates.contains(tmpl.id),
                                        onSelect = { viewModel.requestSelectTemplate(tmpl) },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = tmpl.description,
                                        fontSize = 12.sp,
                                        color = TextMuted,
                                        lineHeight = 16.sp,
                                        minLines = 2
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = TextSubtle,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text("A4 ready", fontSize = 11.sp, color = TextSubtle)
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = TextSubtle,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text("Editable", fontSize = 11.sp, color = TextSubtle)
                                        }
                                    }
                                }
                            }
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                TestInlineSponsorAd(
                    title = "Prepare smarter with Learnly",
                    actionText = "Try free"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
