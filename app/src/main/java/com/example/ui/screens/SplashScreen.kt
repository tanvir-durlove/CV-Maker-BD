package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation timers
    var animationProgress by remember { mutableStateOf(0f) }

    val transition = rememberInfiniteTransition(label = "loader_sweep")
    val sweepOffset by transition.animateFloat(
        initialValue = -30f,
        targetValue = 130f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_offset"
    )

    // Entrance animation
    val scaleAnim = remember { Animatable(0.85f) }
    val alphaAnim = remember { Animatable(0f) }
    val fanAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(500))
        scaleAnim.animateTo(1f, animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMedium))
        fanAnim.animateTo(1f, animationSpec = tween(700, easing = FastOutSlowInEasing))

        // Total splash duration before transitioning to interstitial ad: 2.4 seconds
        delay(2400)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FA))
            .testTag("app_splash_screen")
    ) {
        // Decorative background geometric circles matching SVG
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0xFF5566C4).copy(alpha = 0.08f),
                radius = 150.dp.toPx(),
                center = Offset(size.width - 20.dp.toPx(), 70.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = Color(0xFF5566C4).copy(alpha = 0.08f),
                radius = 170.dp.toPx(),
                center = Offset(20.dp.toPx(), size.height - 40.dp.toPx()),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Center visual hero graphic
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fanned CV document composition matching SVG
            Box(
                modifier = Modifier
                    .size(width = 240.dp, height = 210.dp)
                    .scale(scaleAnim.value),
                contentAlignment = Alignment.Center
            ) {
                // Left fanned sheet
                Surface(
                    modifier = Modifier
                        .size(width = 130.dp, height = 168.dp)
                        .offset(x = (-16 * fanAnim.value).dp, y = 4.dp)
                        .rotate(-9f * fanAnim.value),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFD3D9F0)
                ) {}

                // Right fanned sheet
                Surface(
                    modifier = Modifier
                        .size(width = 130.dp, height = 168.dp)
                        .offset(x = (16 * fanAnim.value).dp, y = 4.dp)
                        .rotate(9f * fanAnim.value),
                    shape = RoundedCornerShape(18.dp),
                    color = Color(0xFFE3E7F6)
                ) {}

                // Main foreground white document sheet with drop shadow
                Surface(
                    modifier = Modifier
                        .size(width = 136.dp, height = 175.dp)
                        .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = Color(0xFF2B3A67), spotColor = Color(0xFF2B3A67)),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            // Left sidebar of the document
                            Box(
                                modifier = Modifier
                                    .width(44.dp)
                                    .fillMaxHeight()
                                    .background(Color(0xFFECEFFA))
                                    .padding(start = 10.dp, top = 16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Profile circle avatar
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF5566C4))
                                    )
                                    Box(modifier = Modifier.size(width = 24.dp, height = 4.dp).background(Color(0xFFC4CCEE), RoundedCornerShape(2.dp)))
                                    Box(modifier = Modifier.size(width = 20.dp, height = 4.dp).background(Color(0xFFC4CCEE), RoundedCornerShape(2.dp)))
                                    Box(modifier = Modifier.size(width = 24.dp, height = 4.dp).background(Color(0xFFC4CCEE), RoundedCornerShape(2.dp)))
                                }
                            }

                            // Right main content area of the document
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp, top = 16.dp, end = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Full Name header line
                                Box(modifier = Modifier.size(width = 38.dp, height = 8.dp).background(Color(0xFF1B2340), RoundedCornerShape(4.dp)))
                                // Title subtitle line
                                Box(modifier = Modifier.size(width = 32.dp, height = 5.dp).background(Color(0xFF5566C4), RoundedCornerShape(2.5.dp)))
                                Spacer(modifier = Modifier.height(10.dp))
                                // Body placeholder text lines
                                Box(modifier = Modifier.fillMaxWidth(0.9f).height(5.dp).background(Color(0xFFE2E6F3), RoundedCornerShape(2.5.dp)))
                                Box(modifier = Modifier.fillMaxWidth(0.8f).height(5.dp).background(Color(0xFFE2E6F3), RoundedCornerShape(2.5.dp)))
                                Box(modifier = Modifier.fillMaxWidth(0.85f).height(5.dp).background(Color(0xFFE2E6F3), RoundedCornerShape(2.5.dp)))
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(modifier = Modifier.size(width = 26.dp, height = 6.dp).background(Color(0xFF1B2340), RoundedCornerShape(3.dp)))
                                Box(modifier = Modifier.fillMaxWidth(0.85f).height(5.dp).background(Color(0xFFE2E6F3), RoundedCornerShape(2.5.dp)))
                            }
                        }

                        // Top right folded corner visual
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .clip(RoundedCornerShape(bottomStart = 8.dp))
                                .background(Color(0xFFDDE2F5))
                        )

                        // Yellow download badge pill on bottom right
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 8.dp, y = 8.dp)
                                .size(36.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF4C25B),
                            border = androidx.compose.foundation.BorderStroke(3.dp, Color.White),
                            shadowElevation = 6.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = "Download badge",
                                    tint = Color(0xFF1B2340),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // App Name matching SVG: "CV Maker" (bold 800) and "Resume Builder BD"
            Text(
                text = "CV Maker",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1B2340),
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Resume Builder BD",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B7390),
                letterSpacing = 0.3.sp
            )
        }

        // Bottom section with animated progress track and footer tag
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Track loader bar with animated sweeping pill
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF5566C4).copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .offset(x = sweepOffset.dp)
                        .size(width = 44.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF5566C4))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Footer label: "Powered by NextGen Tool bd"
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Powered by ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7390)
                )
                Text(
                    text = "NextGen Tool bd",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2340)
                )
            }
        }
    }
}
