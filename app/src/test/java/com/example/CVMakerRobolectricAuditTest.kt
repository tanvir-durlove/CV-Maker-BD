package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.CVModel
import com.example.model.CVSectionType
import com.example.model.ExperienceItem
import com.example.model.EducationItem
import com.example.util.PdfGenerator
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CVMakerRobolectricAuditTest {

    @Test
    fun testAppResourcesAndIdentity() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("CV Maker", appName)
    }

    @Test
    fun testViewModelInitializationAndNavigation() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = MainViewModel(app)

        // Initial screen is splash
        assertEquals(ScreenType.SPLASH, viewModel.currentScreen.value)

        // After splash complete, navigates to Home and activates interstitial ad
        viewModel.onSplashComplete()
        assertEquals(ScreenType.HOME, viewModel.currentScreen.value)
        assertTrue(viewModel.isInterstitialAdShowing.value)

        viewModel.dismissInterstitialAd()
        assertFalse(viewModel.isInterstitialAdShowing.value)

        // Can navigate to screens
        viewModel.navigateTo(ScreenType.TEMPLATES)
        assertEquals(ScreenType.TEMPLATES, viewModel.currentScreen.value)

        viewModel.navigateTo(ScreenType.MY_CVS)
        assertEquals(ScreenType.MY_CVS, viewModel.currentScreen.value)

        viewModel.navigateTo(ScreenType.SETTINGS)
        assertEquals(ScreenType.SETTINGS, viewModel.currentScreen.value)
    }

    @Test
    fun testSectionDragAndDropReordering() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = MainViewModel(app)

        val initialSections = viewModel.activeCv.value.sectionOrder
        assertEquals(CVSectionType.PERSONAL, initialSections[0])
        assertEquals(CVSectionType.EXPERIENCE, initialSections[1])

        // Reorder index 0 with 1
        viewModel.moveSection(0, 1)
        val updatedSections = viewModel.activeCv.value.sectionOrder
        assertEquals(CVSectionType.EXPERIENCE, updatedSections[0])
        assertEquals(CVSectionType.PERSONAL, updatedSections[1])
    }

    @Test
    fun testRewardedAdUnlockingTemplate() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = MainViewModel(app)

        val clarityTemplate = viewModel.availableTemplates.first { it.id == "clarity" }
        assertFalse(viewModel.unlockedTemplates.value.contains("clarity"))

        // Request select locked template triggers rewarded ad flow
        viewModel.requestSelectTemplate(clarityTemplate)
        assertTrue(viewModel.isRewardedAdShowing.value)
        assertEquals("clarity", viewModel.pendingUnlockTemplateId.value)

        // Complete rewarded ad
        viewModel.completeRewardedAd()
        assertTrue(viewModel.unlockedTemplates.value.contains("clarity"))
        assertFalse(viewModel.isRewardedAdShowing.value)
        assertEquals("clarity", viewModel.activeCv.value.templateId)
    }

    @Test
    fun testRealtimePdfGeneration() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testCv = CVModel(
            title = "Test Resume",
            templateId = "aura",
            fullName = "John Doe",
            professionalTitle = "Staff Engineer",
            email = "john@example.com",
            phone = "+1 555 1234",
            location = "New York, NY",
            website = "johndoe.dev",
            professionalSummary = "Experienced software architect.",
            experiences = listOf(
                ExperienceItem(
                    jobTitle = "Lead Engineer",
                    company = "NextGen Tech",
                    dates = "2021 — 2026",
                    achievements = "Built high-performance Android apps."
                )
            ),
            educations = listOf(
                EducationItem(
                    school = "Tech University",
                    degree = "BS Computer Science",
                    dates = "2017 — 2021"
                )
            ),
            skills = "Kotlin, Jetpack Compose, Coroutines",
            projects = "Open Source Contributions · 2025",
            languages = "English — Fluent"
        )

        val pdfFile = PdfGenerator.generatePdf(context, testCv)
        assertNotNull(pdfFile)
        assertTrue(pdfFile.exists())
        assertTrue(pdfFile.length() > 0)

        // Verify bitmap generation for live preview
        val bitmap = PdfGenerator.generateBitmap(testCv, scale = 0.5f)
        assertNotNull(bitmap)
        assertTrue(bitmap.width > 0)
        assertTrue(bitmap.height > 0)
    }
}
