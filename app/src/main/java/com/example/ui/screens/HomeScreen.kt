package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.TemplateCard
import com.example.ui.components.TestBannerAd
import com.example.ui.components.TestInlineSponsorAd
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToTemplates: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val unlockedTemplates by viewModel.unlockedTemplates.collectAsState()
    val templates = viewModel.availableTemplates

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground,
        bottomBar = {
            // Test banner ad as seen in design
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Top user greeting header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "YOUR CAREER, CLEARLY TOLD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtle,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Good afternoon, $userName.",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Turn your experience into your next\nopportunity.",
                            fontSize = 14.sp,
                            color = TextMuted,
                            lineHeight = 19.sp
                        )
                    }

                    // Floating action create button
                    FloatingActionButton(
                        onClick = { viewModel.createNewCv("aura") },
                        containerColor = ForestGreen,
                        contentColor = PureWhite,
                        shape = CircleShape,
                        modifier = Modifier.size(52.dp).testTag("fab_create_cv")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create CV", modifier = Modifier.size(26.dp))
                    }
                }
            }

            // Big Hero Card: "Start with a story worth reading"
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .testTag("hero_start_card"),
                    color = ForestGreen
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFC3DFCF),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "YOUR FIRST CV",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC3DFCF),
                                letterSpacing = 1.1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Start with a story worth\nreading.",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            lineHeight = 30.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Choose a format, add your experience, and\nexport a professional CV in minutes.",
                            fontSize = 13.sp,
                            color = Color(0xFFD7E5DD),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button & Document graphic preview
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Button(
                                onClick = { viewModel.createNewCv("aura") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PureWhite,
                                    contentColor = TextDark
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("create_my_cv_btn")
                            ) {
                                Text(
                                    text = "Create my CV",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Outlined.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            // Minimal overlapping resume graphics
                            Box(
                                modifier = Modifier
                                    .width(110.dp)
                                    .height(70.dp)
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(70.dp, 60.dp)
                                        .align(Alignment.BottomStart),
                                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                                    color = PureWhite,
                                    shadowElevation = 4.dp
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFE0C4B4)))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(modifier = Modifier.size(30.dp, 3.dp).background(Color(0xFF888888)))
                                    }
                                }

                                Surface(
                                    modifier = Modifier
                                        .size(75.dp, 65.dp)
                                        .align(Alignment.BottomEnd),
                                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                                    color = PureWhite,
                                    shadowElevation = 6.dp
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Box(modifier = Modifier.size(36.dp, 4.dp).background(Color(0xFF333333)))
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Box(modifier = Modifier.size(24.dp, 2.dp).background(Color(0xFF999999)))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section Header: "Choose your starting point"
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "BUILT FOR EVERY APPLICATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSubtle,
                            letterSpacing = 1.1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Choose your starting point",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickable { onNavigateToTemplates() }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View all",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = "View all templates",
                            tint = TextDark,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // 2x2 Template Grid on Home
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (templates.size >= 2) {
                            TemplateCard(
                                template = templates[0],
                                isUnlocked = unlockedTemplates.contains(templates[0].id),
                                onSelect = { viewModel.requestSelectTemplate(templates[0]) },
                                modifier = Modifier.weight(1f)
                            )
                            TemplateCard(
                                template = templates[1],
                                isUnlocked = unlockedTemplates.contains(templates[1].id),
                                onSelect = { viewModel.requestSelectTemplate(templates[1]) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (templates.size >= 4) {
                            TemplateCard(
                                template = templates[2],
                                isUnlocked = unlockedTemplates.contains(templates[2].id),
                                onSelect = { viewModel.requestSelectTemplate(templates[2]) },
                                modifier = Modifier.weight(1f)
                            )
                            TemplateCard(
                                template = templates[3],
                                isUnlocked = unlockedTemplates.contains(templates[3].id),
                                onSelect = { viewModel.requestSelectTemplate(templates[3]) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Inline sponsor ad banner
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
