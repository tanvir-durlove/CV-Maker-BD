package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TemplateInfo
import com.example.model.TemplateTier
import com.example.ui.theme.*

@Composable
fun TemplateCard(
    template: TemplateInfo,
    isUnlocked: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBg = when (template.id) {
        "aura" -> AuraPinkCard
        "clarity" -> ClarityBlueCard
        "tradition" -> TraditionSandCard
        "continental" -> ContinentalTealCard
        else -> PureWhite
    }

    Surface(
        modifier = modifier
            .testTag("template_card_${template.id}")
            .clip(RoundedCornerShape(20.dp))
            .clickable { onSelect() },
        color = cardBg,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            // Document Graphic representation inside card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PureWhite)
                    .padding(8.dp)
            ) {
                // Tier badge on top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(2.dp)
                ) {
                    if (template.tier == TemplateTier.FREE || isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = FreeBadgeBg,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFF0D4C8))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = FreeBadgeText,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = if (template.tier == TemplateTier.FREE) "Free" else "Unlocked",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FreeBadgeText
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RewardBadgeBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "Reward",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            }
                        }
                    }
                }

                // Mini document wireframe lines mimicking Figma screen
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD6C2B4))
                        )
                        Column {
                            Box(modifier = Modifier.size(width = 46.dp, height = 4.dp).background(Color(0xFF222222), RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.size(width = 32.dp, height = 3.dp).background(Color(0xFF9E9E9E), RoundedCornerShape(1.dp)))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFEEEEEE)))
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Mini sidebar
                        Column(modifier = Modifier.width(36.dp)) {
                            Box(modifier = Modifier.size(24.dp, 3.dp).background(Color(0xFF888888)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.size(30.dp, 2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.size(26.dp, 2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.size(20.dp, 3.dp).background(Color(0xFF888888)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.size(30.dp, 2.dp).background(Color(0xFFCCCCCC)))
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Mini main column
                        Column(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.size(30.dp, 3.dp).background(Color(0xFF555555)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth(0.9f).height(2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.size(36.dp, 3.dp).background(Color(0xFF555555)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth(0.85f).height(2.dp).background(Color(0xFFCCCCCC)))
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(modifier = Modifier.fillMaxWidth(0.6f).height(2.dp).background(Color(0xFFCCCCCC)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Card Bottom info row with action button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = template.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = template.subtitle,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                // Action icon button
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PureWhite.copy(alpha = 0.8f))
                        .clickable { onSelect() },
                    contentAlignment = Alignment.Center
                ) {
                    if (template.tier == TemplateTier.FREE || isUnlocked) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowForward,
                            contentDescription = "Select template",
                            tint = ForestGreenDark,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Watch ad to unlock",
                            tint = ForestGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
