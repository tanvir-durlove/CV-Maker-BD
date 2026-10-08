package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.ScreenType

@Composable
fun AppBottomNavigation(
    currentScreen: ScreenType,
    onNavigate: (ScreenType) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_bottom_nav"),
        color = PureWhite,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                label = "Home",
                selected = currentScreen == ScreenType.HOME,
                onClick = { onNavigate(ScreenType.HOME) },
                testTag = "nav_home"
            )
            BottomNavItem(
                icon = Icons.Outlined.GridView,
                selectedIcon = Icons.Filled.GridView,
                label = "Templates",
                selected = currentScreen == ScreenType.TEMPLATES,
                onClick = { onNavigate(ScreenType.TEMPLATES) },
                testTag = "nav_templates"
            )
            BottomNavItem(
                icon = Icons.Outlined.Description,
                selectedIcon = Icons.Filled.Description,
                label = "My CVs",
                selected = currentScreen == ScreenType.MY_CVS,
                onClick = { onNavigate(ScreenType.MY_CVS) },
                testTag = "nav_my_cvs"
            )
            BottomNavItem(
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings,
                label = "Settings",
                selected = currentScreen == ScreenType.SETTINGS,
                onClick = { onNavigate(ScreenType.SETTINGS) },
                testTag = "nav_settings"
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    selectedIcon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(height = 30.dp, width = 56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (selected) MintBadgeBg else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) selectedIcon else icon,
                contentDescription = label,
                tint = if (selected) ForestGreen else TextSubtle,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) ForestGreen else TextSubtle
        )
    }
}
