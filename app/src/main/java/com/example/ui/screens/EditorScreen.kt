package com.example.ui.screens

import android.graphics.Color as AndroidColor
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AutoAwesome
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
import com.example.model.CVSectionType
import com.example.model.EducationItem
import com.example.model.ExperienceItem
import com.example.ui.components.TestBannerAd
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.ScreenType

@Composable
fun EditorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeCv by viewModel.activeCv.collectAsState()
    val currentStep by viewModel.editorStep.collectAsState()

    BackHandler {
        if (currentStep > 0) {
            viewModel.prevEditorStep()
        } else {
            viewModel.navigateTo(ScreenType.HOME)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize().background(MintBackground),
        containerColor = MintBackground,
        topBar = {
            // Top App Bar with back button, CV title and Export button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = MintBackground
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (currentStep > 0) viewModel.prevEditorStep()
                                    else viewModel.navigateTo(ScreenType.HOME)
                                }
                                .testTag("editor_back_btn"),
                            color = PureWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = activeCv.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    // Direct "Export" button in top bar
                    Button(
                        onClick = {
                            viewModel.saveActiveCv()
                            viewModel.navigateTo(ScreenType.EXPORT_PREVIEW)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("top_bar_export_btn")
                    ) {
                        Text("Export", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                TestBannerAd(
                    sponsorName = "Land your next interview",
                    actionLabel = "Learn more"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Horizontal Step Indicators matching Figma UI: [Personal details] [Experience] [Education] [Skills] [More sections]
            StepIndicatorRow(
                currentStep = currentStep,
                onSelectStep = { viewModel.setEditorStep(it) }
            )

            // Step Content scrollable area
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                }

                when (currentStep) {
                    0 -> item { StepPersonalDetails(viewModel) }
                    1 -> item { StepExperience(viewModel) }
                    2 -> item { StepEducation(viewModel) }
                    3 -> item { StepSkills(viewModel) }
                    4 -> item { StepFinishingAndReorder(viewModel) }
                }

                // Bottom Action buttons row: [Back] [Save & continue ->]
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.prevEditorStep() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MintBadgeBg,
                                contentColor = ForestGreen
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                            modifier = Modifier.testTag("editor_bottom_back_btn")
                        ) {
                            Text("Back", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Button(
                            onClick = { viewModel.nextEditorStep() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen,
                                contentColor = PureWhite
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                            modifier = Modifier.testTag("editor_save_continue_btn")
                        ) {
                            Text(
                                text = if (currentStep == 4) "Review & export" else "Save & continue",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicatorRow(
    currentStep: Int,
    onSelectStep: (Int) -> Unit
) {
    val stepTitles = listOf("Personal details", "Experience", "Education", "Skills", "More sections")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(stepTitles) { index, title ->
            val isActive = currentStep == index
            val isCompleted = currentStep > index

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelectStep(index) }
                    .testTag("step_pill_$index"),
                shape = RoundedCornerShape(20.dp),
                color = if (isActive) MintBadgeBg else PureWhite,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isActive) MintBadgeBorder else LightBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isCompleted || isActive) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(ForestGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = PureWhite, modifier = Modifier.size(12.dp))
                            } else {
                                Text("${index + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PureWhite)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(MintSurface)
                                .border(1.dp, LightBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${index + 1}", fontSize = 10.sp, color = TextMuted)
                        }
                    }

                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                        color = if (isActive) ForestGreen else TextMuted
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 1: Personal Details (Figma screen 01: "Let's start with you")
// -------------------------------------------------------------
@Composable
private fun StepPersonalDetails(viewModel: MainViewModel) {
    val cv by viewModel.activeCv.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Step Title header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text("01", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            Column {
                Text(
                    text = "Let's start with you",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "This information appears at the top of your CV.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        EditorFormField(
            label = "Full name",
            value = cv.fullName,
            placeholder = "Alex Morgan",
            testTag = "input_full_name",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(fullName = it) } }
        )

        EditorFormField(
            label = "Professional title",
            value = cv.professionalTitle,
            placeholder = "Product Designer",
            testTag = "input_professional_title",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(professionalTitle = it) } }
        )

        EditorFormField(
            label = "Email",
            value = cv.email,
            placeholder = "alex@example.com",
            testTag = "input_email",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(email = it) } }
        )

        EditorFormField(
            label = "Phone",
            value = cv.phone,
            placeholder = "+1 555 0100",
            testTag = "input_phone",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(phone = it) } }
        )

        EditorFormField(
            label = "Location",
            value = cv.location,
            placeholder = "Austin, TX",
            testTag = "input_location",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(location = it) } }
        )

        EditorFormField(
            label = "Website or portfolio",
            value = cv.website,
            placeholder = "portfolio.com",
            testTag = "input_website",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(website = it) } }
        )

        EditorFormField(
            label = "Professional summary",
            value = cv.professionalSummary,
            placeholder = "Describe your strengths, experience, and goals in 2–4 sentences.",
            testTag = "input_summary",
            singleLine = false,
            minLines = 3,
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(professionalSummary = it) } }
        )
    }
}

// -------------------------------------------------------------
// STEP 2: Experience (Figma screen 02: "Show your experience")
// -------------------------------------------------------------
@Composable
private fun StepExperience(viewModel: MainViewModel) {
    val cv by viewModel.activeCv.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text("02", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            Column {
                Text(
                    text = "Show your experience",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Focus on outcomes, not only responsibilities.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        cv.experiences.forEachIndexed { index, exp ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = PureWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Role #${index + 1}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        if (cv.experiences.size > 1) {
                            IconButton(onClick = { viewModel.removeExperience(index) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove role", tint = Color(0xFFC53030), modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    EditorFormField(
                        label = "Job title",
                        value = exp.jobTitle,
                        placeholder = "Senior Product Designer",
                        testTag = "input_job_title_$index",
                        onValueChange = { viewModel.updateExperience(index, exp.copy(jobTitle = it)) }
                    )

                    EditorFormField(
                        label = "Company",
                        value = exp.company,
                        placeholder = "Acme Studio",
                        testTag = "input_company_$index",
                        onValueChange = { viewModel.updateExperience(index, exp.copy(company = it)) }
                    )

                    EditorFormField(
                        label = "Dates",
                        value = exp.dates,
                        placeholder = "Jan 2022 — Present",
                        testTag = "input_dates_$index",
                        onValueChange = { viewModel.updateExperience(index, exp.copy(dates = it)) }
                    )

                    EditorFormField(
                        label = "Achievements",
                        value = exp.achievements,
                        placeholder = "Led the redesign of onboarding, improving activation by 24%.",
                        singleLine = false,
                        minLines = 3,
                        testTag = "input_achievements_$index",
                        onValueChange = { viewModel.updateExperience(index, exp.copy(achievements = it)) }
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.addExperience() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add another position", fontWeight = FontWeight.Bold)
        }

        // Tip banner matching Figma: "Make it memorable"
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MintBadgeBg
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = ForestGreen,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "Make it memorable",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Start with an action verb, explain what you did, then add the result. Numbers make achievements easier to understand.",
                        fontSize = 12.sp,
                        color = TextDark,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 3: Education (Figma screen 03: "Add your education")
// -------------------------------------------------------------
@Composable
private fun StepEducation(viewModel: MainViewModel) {
    val cv by viewModel.activeCv.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text("03", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            Column {
                Text(
                    text = "Add your education",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Include your most relevant qualification first.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        cv.educations.forEachIndexed { index, edu ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = PureWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Education #${index + 1}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                        if (cv.educations.size > 1) {
                            IconButton(onClick = { viewModel.removeEducation(index) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Color(0xFFC53030), modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    EditorFormField(
                        label = "School or institution",
                        value = edu.school,
                        placeholder = "State University",
                        testTag = "input_school_$index",
                        onValueChange = { viewModel.updateEducation(index, edu.copy(school = it)) }
                    )

                    EditorFormField(
                        label = "Degree or qualification",
                        value = edu.degree,
                        placeholder = "BA, Interaction Design",
                        testTag = "input_degree_$index",
                        onValueChange = { viewModel.updateEducation(index, edu.copy(degree = it)) }
                    )

                    EditorFormField(
                        label = "Dates",
                        value = edu.dates,
                        placeholder = "2017 — 2021",
                        testTag = "input_edu_dates_$index",
                        onValueChange = { viewModel.updateEducation(index, edu.copy(dates = it)) }
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { viewModel.addEducation() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Add another qualification", fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// STEP 4: Skills (Figma screen 04: "Highlight your strengths")
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepSkills(viewModel: MainViewModel) {
    val cv by viewModel.activeCv.collectAsState()
    val suggestions = listOf("Communication", "Leadership", "Problem solving", "Project management", "Figma", "Data Analysis")

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text("04", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            Column {
                Text(
                    text = "Highlight your strengths",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Use role-specific keywords, separated by commas.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        EditorFormField(
            label = "Skills",
            value = cv.skills,
            placeholder = "Product strategy, Figma, User research, Prototyping",
            singleLine = false,
            minLines = 4,
            testTag = "input_skills",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(skills = it) } }
        )

        Text(
            text = "Suggestions",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextSubtle
        )

        // Suggestion pills matching Figma
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            suggestions.forEach { suggestion ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            val current = cv.skills.trim()
                            val updated = if (current.isEmpty()) suggestion else "$current, $suggestion"
                            viewModel.updateActiveCv { c -> c.copy(skills = updated) }
                        }
                        .testTag("suggestion_pill_$suggestion"),
                    shape = RoundedCornerShape(20.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = TextMuted, modifier = Modifier.size(13.dp))
                        Text(suggestion, fontSize = 12.sp, color = TextDark)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 5: More Sections & Drag-and-Drop Reordering (Figma screen 05)
// -------------------------------------------------------------
@Composable
private fun StepFinishingAndReorder(viewModel: MainViewModel) {
    val cv by viewModel.activeCv.collectAsState()
    val accentColors = listOf(
        AccentForest,
        AccentNavy,
        AccentBurgundy,
        AccentPurple,
        AccentRust
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MintBadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Text("05", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }

            Column {
                Text(
                    text = "Add the finishing touches",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Optional details can make your application more relevant.",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }
        }

        EditorFormField(
            label = "Projects, courses & certifications",
            value = cv.projects,
            placeholder = "Google UX Design Certificate · 2024",
            singleLine = false,
            minLines = 3,
            testTag = "input_projects",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(projects = it) } }
        )

        EditorFormField(
            label = "Languages",
            value = cv.languages,
            placeholder = "English — Native, Spanish — Professional",
            testTag = "input_languages",
            onValueChange = { viewModel.updateActiveCv { c -> c.copy(languages = it) } }
        )

        // Accent Color Selection matching Figma
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = TextDark, modifier = Modifier.size(18.dp))
                Text("Accent color", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                accentColors.forEach { color ->
                    val hexStr = String.format("#%06X", 0xFFFFFF and color.value.toInt())
                    val isSelected = cv.accentColorHex.equals(hexStr, ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) ForestGreen else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                viewModel.updateActiveCv { c -> c.copy(accentColorHex = hexStr) }
                            }
                            .testTag("color_picker_$hexStr"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = PureWhite, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        Divider(color = LightBorder, thickness = 0.8.dp)

        // Drag-and-Drop / Interactive Section Reordering Feature
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CV Section Order",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Reorder sections using the arrows to adjust document hierarchy",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MintBadgeBg
                ) {
                    Text(
                        "Drag & Reorder",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            cv.sectionOrder.forEachIndexed { index, section ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("section_reorder_item_${section.name}"),
                    shape = RoundedCornerShape(12.dp),
                    color = PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LightBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DragHandle,
                                contentDescription = "Drag Handle",
                                tint = TextSubtle,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = section.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = section.subtitle,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        // Up / Down reordering controls
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    if (index > 0) viewModel.moveSection(index, index - 1)
                                },
                                enabled = index > 0,
                                modifier = Modifier.size(32.dp).testTag("move_up_${section.name}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Move Up",
                                    tint = if (index > 0) ForestGreen else LightBorder
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (index < cv.sectionOrder.size - 1) viewModel.moveSection(index, index + 1)
                                },
                                enabled = index < cv.sectionOrder.size - 1,
                                modifier = Modifier.size(32.dp).testTag("move_down_${section.name}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Move Down",
                                    tint = if (index < cv.sectionOrder.size - 1) ForestGreen else LightBorder
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorFormField(
    label: String,
    value: String,
    placeholder: String,
    testTag: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextSubtle, fontSize = 13.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ForestGreen,
                unfocusedBorderColor = LightBorder,
                focusedContainerColor = PureWhite,
                unfocusedContainerColor = PureWhite
            ),
            singleLine = singleLine,
            minLines = minLines
        )
    }
}
