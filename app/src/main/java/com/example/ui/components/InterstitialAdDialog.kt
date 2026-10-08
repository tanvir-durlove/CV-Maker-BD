package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.*

@Composable
fun TestInterstitialAdDialog(
    countdown: Int,
    onAdDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {
            if (countdown <= 0) onAdDismissed()
        },
        properties = DialogProperties(
            dismissOnBackPress = countdown <= 0,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF0F1523).copy(alpha = 0.96f))
                .padding(20.dp)
                .testTag("test_interstitial_ad_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // Header with Ad Tag and Close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF1B2340)
                            ) {
                                Text(
                                    text = "TEST INTERSTITIAL AD",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                            Text(
                                text = "Sponsored",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }

                        if (countdown > 0) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MintBadgeBg
                            ) {
                                Text(
                                    text = "Skip in ${countdown}s",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            IconButton(
                                onClick = onAdDismissed,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("interstitial_close_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Ad",
                                    tint = TextDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Ad Creatives Card: Career Pro Studio / NextGen Interview Prep
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1A2340)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo_512),
                                contentDescription = null,
                                modifier = Modifier.size(60.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Master Your Next Job Interview",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "AI mock interviews & salary negotiation guides",
                                fontSize = 12.sp,
                                color = Color(0xFFC4CCEE)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "InterviewPro by NextGen",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = "Free 7-day career accelerator pass",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }

                        Button(
                            onClick = onAdDismissed,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen,
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("interstitial_cta_btn")
                        ) {
                            Text("Install", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }

                    if (countdown <= 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        TextButton(
                            onClick = onAdDismissed,
                            modifier = Modifier.fillMaxWidth().testTag("interstitial_continue_app_btn")
                        ) {
                            Text(
                                text = "Continue to CV Maker",
                                color = TextMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
