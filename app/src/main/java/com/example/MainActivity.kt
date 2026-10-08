package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.AppBottomNavigation
import com.example.ui.components.TestInterstitialAdDialog
import com.example.ui.components.TestRewardedAdDialog
import com.example.ui.screens.*
import com.example.ui.theme.CVMakerTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CVMakerTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isRewardedAdShowing by viewModel.isRewardedAdShowing.collectAsState()
    val rewardedCountdown by viewModel.rewardedAdCountdown.collectAsState()
    val pendingTemplateId by viewModel.pendingUnlockTemplateId.collectAsState()

    val isInterstitialAdShowing by viewModel.isInterstitialAdShowing.collectAsState()
    val interstitialCountdown by viewModel.interstitialAdCountdown.collectAsState()

    // Find template name for rewarded ad dialog
    val pendingTemplate = viewModel.availableTemplates.find { it.id == pendingTemplateId }

    val showBottomNav = currentScreen in listOf(
        ScreenType.HOME,
        ScreenType.TEMPLATES,
        ScreenType.MY_CVS,
        ScreenType.SETTINGS
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                AppBottomNavigation(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (currentScreen == ScreenType.SPLASH) androidx.compose.foundation.layout.PaddingValues() else innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    ScreenType.SPLASH -> SplashScreen(
                        onSplashFinished = { viewModel.onSplashComplete() }
                    )
                    ScreenType.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToTemplates = { viewModel.navigateTo(ScreenType.TEMPLATES) }
                    )
                    ScreenType.TEMPLATES -> TemplatesScreen(viewModel = viewModel)
                    ScreenType.MY_CVS -> MyCvsScreen(viewModel = viewModel)
                    ScreenType.SETTINGS -> SettingsScreen(viewModel = viewModel)
                    ScreenType.EDITOR -> EditorScreen(viewModel = viewModel)
                    ScreenType.EXPORT_PREVIEW -> ExportPreviewScreen(viewModel = viewModel)
                }
            }

            // Test Interstitial Ad (Only shown right after splash)
            if (isInterstitialAdShowing) {
                TestInterstitialAdDialog(
                    countdown = interstitialCountdown,
                    onAdDismissed = { viewModel.dismissInterstitialAd() }
                )
            }

            // Test Rewarded Ad Unit Dialog
            if (isRewardedAdShowing && pendingTemplate != null) {
                TestRewardedAdDialog(
                    templateName = pendingTemplate.name,
                    countdown = rewardedCountdown,
                    onRewardEarned = { viewModel.completeRewardedAd() },
                    onDismiss = { viewModel.dismissRewardedAd() }
                )
            }
        }
    }
}
