package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CVModel
import com.example.model.CVSectionType
import com.example.model.EducationItem
import com.example.model.ExperienceItem
import com.example.model.TemplateInfo
import com.example.model.TemplateTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScreenType {
    SPLASH,
    HOME,
    TEMPLATES,
    MY_CVS,
    SETTINGS,
    EDITOR,
    EXPORT_PREVIEW
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Available Templates exactly as in Figma design
    val availableTemplates = listOf(
        TemplateInfo(
            id = "aura",
            name = "Aura",
            subtitle = "Modern CV",
            description = "Expressive, polished, and easy to scan.",
            tier = TemplateTier.FREE,
            category = "Modern",
            defaultAccent = "#134E3F"
        ),
        TemplateInfo(
            id = "clarity",
            name = "Clarity",
            subtitle = "ATS Resume",
            description = "Simple structure built for applicant systems.",
            tier = TemplateTier.REWARDED,
            category = "ATS-friendly",
            defaultAccent = "#235384"
        ),
        TemplateInfo(
            id = "tradition",
            name = "Tradition",
            subtitle = "Biodata",
            description = "A formal personal and professional profile.",
            tier = TemplateTier.REWARDED,
            category = "Regional",
            defaultAccent = "#8B5E34"
        ),
        TemplateInfo(
            id = "continental",
            name = "Continental",
            subtitle = "Europass CV",
            description = "A familiar format for European applications.",
            tier = TemplateTier.REWARDED,
            category = "Modern",
            defaultAccent = "#1B6B56"
        )
    )

    // Current navigation state
    private val _currentScreen = MutableStateFlow(ScreenType.SPLASH)
    val currentScreen: StateFlow<ScreenType> = _currentScreen.asStateFlow()

    // Test Interstitial Ad state (shows only once right after splash screen)
    private val _isInterstitialAdShowing = MutableStateFlow(false)
    val isInterstitialAdShowing: StateFlow<Boolean> = _isInterstitialAdShowing.asStateFlow()

    private val _interstitialAdCountdown = MutableStateFlow(3)
    val interstitialAdCountdown: StateFlow<Int> = _interstitialAdCountdown.asStateFlow()

    // Editor current step index (0: Personal, 1: Experience, 2: Education, 3: Skills, 4: Finishing/Sections)
    private val _editorStep = MutableStateFlow(0)
    val editorStep: StateFlow<Int> = _editorStep.asStateFlow()

    // User settings
    private val _userName = MutableStateFlow("Td")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _updateReminders = MutableStateFlow(true)
    val updateReminders: StateFlow<Boolean> = _updateReminders.asStateFlow()

    // Unlocked templates (Aura is free by default)
    private val _unlockedTemplates = MutableStateFlow(setOf("aura"))
    val unlockedTemplates: StateFlow<Set<String>> = _unlockedTemplates.asStateFlow()

    // List of user CVs
    private val _savedCvs = MutableStateFlow<List<CVModel>>(
        listOf(
            CVModel(
                id = "demo_cv_1",
                title = "My Modern CV",
                templateId = "aura",
                accentColorHex = "#134E3F",
                lastEdited = "Oct 7, 2026 · Stored on device"
            )
        )
    )
    val savedCvs: StateFlow<List<CVModel>> = _savedCvs.asStateFlow()

    // Current CV being created or edited
    private val _activeCv = MutableStateFlow(
        _savedCvs.value.firstOrNull() ?: CVModel()
    )
    val activeCv: StateFlow<CVModel> = _activeCv.asStateFlow()

    // Test Ad unit state
    private val _isRewardedAdShowing = MutableStateFlow(false)
    val isRewardedAdShowing: StateFlow<Boolean> = _isRewardedAdShowing.asStateFlow()

    private val _pendingUnlockTemplateId = MutableStateFlow<String?>(null)
    val pendingUnlockTemplateId: StateFlow<String?> = _pendingUnlockTemplateId.asStateFlow()

    private val _rewardedAdCountdown = MutableStateFlow(5)
    val rewardedAdCountdown: StateFlow<Int> = _rewardedAdCountdown.asStateFlow()

    fun navigateTo(screen: ScreenType) {
        _currentScreen.value = screen
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setUpdateReminders(enabled: Boolean) {
        _updateReminders.value = enabled
    }

    fun resetAllData() {
        _userName.value = "User"
        _unlockedTemplates.value = setOf("aura")
        _savedCvs.value = emptyList()
        _activeCv.value = CVModel(fullName = "User")
        _currentScreen.value = ScreenType.HOME
    }

    fun createNewCv(templateId: String = "aura") {
        val tmpl = availableTemplates.find { it.id == templateId }
        val newCv = CVModel(
            title = "My ${tmpl?.name ?: "Modern"} CV",
            templateId = templateId,
            accentColorHex = tmpl?.defaultAccent ?: "#134E3F",
            fullName = _userName.value,
            lastEdited = "Today · Stored on device"
        )
        _activeCv.value = newCv
        _editorStep.value = 0
        _currentScreen.value = ScreenType.EDITOR
    }

    fun editCv(cv: CVModel) {
        _activeCv.value = cv
        _editorStep.value = 0
        _currentScreen.value = ScreenType.EDITOR
    }

    fun duplicateCv(cv: CVModel) {
        val copy = cv.copy(
            id = java.util.UUID.randomUUID().toString(),
            title = "${cv.title} (Copy)",
            lastEdited = "Today · Stored on device"
        )
        _savedCvs.value = listOf(copy) + _savedCvs.value
    }

    fun deleteCv(cvId: String) {
        _savedCvs.value = _savedCvs.value.filter { it.id != cvId }
        if (_activeCv.value.id == cvId) {
            _activeCv.value = _savedCvs.value.firstOrNull() ?: CVModel()
        }
    }

    fun setEditorStep(step: Int) {
        _editorStep.value = step.coerceIn(0, 4)
    }

    fun nextEditorStep() {
        if (_editorStep.value < 4) {
            _editorStep.value += 1
        } else {
            // Save and go to Export preview
            saveActiveCv()
            _currentScreen.value = ScreenType.EXPORT_PREVIEW
        }
    }

    fun prevEditorStep() {
        if (_editorStep.value > 0) {
            _editorStep.value -= 1
        } else {
            _currentScreen.value = ScreenType.HOME
        }
    }

    fun updateActiveCv(updater: (CVModel) -> CVModel) {
        val updated = updater(_activeCv.value)
        _activeCv.value = updated
        // Keep updated in list if it exists
        _savedCvs.value = _savedCvs.value.map {
            if (it.id == updated.id) updated else it
        }
    }

    fun saveActiveCv() {
        val current = _activeCv.value
        val exists = _savedCvs.value.any { it.id == current.id }
        if (exists) {
            _savedCvs.value = _savedCvs.value.map { if (it.id == current.id) current else it }
        } else {
            _savedCvs.value = listOf(current) + _savedCvs.value
        }
    }

    // Drag and Drop reordering of sections
    fun moveSection(fromIndex: Int, toIndex: Int) {
        val currentOrder = _activeCv.value.sectionOrder.toMutableList()
        if (fromIndex in currentOrder.indices && toIndex in currentOrder.indices) {
            val item = currentOrder.removeAt(fromIndex)
            currentOrder.add(toIndex, item)
            updateActiveCv { it.copy(sectionOrder = currentOrder) }
        }
    }

    // Experience helpers
    fun updateExperience(index: Int, item: ExperienceItem) {
        val list = _activeCv.value.experiences.toMutableList()
        if (index in list.indices) {
            list[index] = item
            updateActiveCv { it.copy(experiences = list) }
        }
    }

    fun addExperience() {
        val list = _activeCv.value.experiences.toMutableList()
        list.add(ExperienceItem())
        updateActiveCv { it.copy(experiences = list) }
    }

    fun removeExperience(index: Int) {
        val list = _activeCv.value.experiences.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            updateActiveCv { it.copy(experiences = list) }
        }
    }

    // Education helpers
    fun updateEducation(index: Int, item: EducationItem) {
        val list = _activeCv.value.educations.toMutableList()
        if (index in list.indices) {
            list[index] = item
            updateActiveCv { it.copy(educations = list) }
        }
    }

    fun addEducation() {
        val list = _activeCv.value.educations.toMutableList()
        list.add(EducationItem())
        updateActiveCv { it.copy(educations = list) }
    }

    fun removeEducation(index: Int) {
        val list = _activeCv.value.educations.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            updateActiveCv { it.copy(educations = list) }
        }
    }

    // Test Ad Unit Flow
    fun requestSelectTemplate(template: TemplateInfo) {
        if (template.tier == TemplateTier.FREE || _unlockedTemplates.value.contains(template.id)) {
            createNewCv(template.id)
        } else {
            // Trigger test rewarded ad flow
            _pendingUnlockTemplateId.value = template.id
            _isRewardedAdShowing.value = true
            _rewardedAdCountdown.value = 4
            viewModelScope.launch {
                for (i in 3 downTo 0) {
                    kotlinx.coroutines.delay(1000)
                    _rewardedAdCountdown.value = i
                }
            }
        }
    }

    fun completeRewardedAd() {
        val tmplId = _pendingUnlockTemplateId.value
        if (tmplId != null) {
            _unlockedTemplates.value = _unlockedTemplates.value + tmplId
            _pendingUnlockTemplateId.value = null
            _isRewardedAdShowing.value = false
            createNewCv(tmplId)
        } else {
            _isRewardedAdShowing.value = false
        }
    }

    fun dismissRewardedAd() {
        _isRewardedAdShowing.value = false
        _pendingUnlockTemplateId.value = null
    }

    // Called when Splash Screen finishes
    fun onSplashComplete() {
        _currentScreen.value = ScreenType.HOME
        // Show interstitial ad only after splash
        _isInterstitialAdShowing.value = true
        _interstitialAdCountdown.value = 3
        viewModelScope.launch {
            for (i in 2 downTo 0) {
                kotlinx.coroutines.delay(1000)
                _interstitialAdCountdown.value = i
            }
        }
    }

    fun dismissInterstitialAd() {
        _isInterstitialAdShowing.value = false
    }
}
