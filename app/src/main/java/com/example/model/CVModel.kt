package com.example.model

enum class CVSectionType(val title: String, val subtitle: String) {
    PERSONAL("Personal details", "Contact and summary"),
    EXPERIENCE("Experience", "Work history and achievements"),
    EDUCATION("Education", "Degrees and institutions"),
    SKILLS("Skills", "Core strengths and competencies"),
    CUSTOM_PROJECTS("Projects & Certifications", "Courses, honors, and projects"),
    LANGUAGES("Languages", "Spoken and written proficiencies")
}

data class ExperienceItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val jobTitle: String = "",
    val company: String = "",
    val dates: String = "",
    val achievements: String = ""
)

data class EducationItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val school: String = "",
    val degree: String = "",
    val dates: String = ""
)

data class CVModel(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "My Modern CV",
    val templateId: String = "aura",
    val accentColorHex: String = "#134E3F",
    val lastEdited: String = "Today",
    // Section order for drag-and-drop reordering
    val sectionOrder: List<CVSectionType> = listOf(
        CVSectionType.PERSONAL,
        CVSectionType.EXPERIENCE,
        CVSectionType.EDUCATION,
        CVSectionType.SKILLS,
        CVSectionType.CUSTOM_PROJECTS,
        CVSectionType.LANGUAGES
    ),
    // Personal details
    val fullName: String = "",
    val professionalTitle: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val website: String = "",
    val professionalSummary: String = "",
    val photoUri: String? = null,
    val showQrCode: Boolean = true,
    // Experiences
    val experiences: List<ExperienceItem> = listOf(
        ExperienceItem(
            jobTitle = "",
            company = "",
            dates = "",
            achievements = ""
        )
    ),
    // Education
    val educations: List<EducationItem> = listOf(
        EducationItem(
            school = "",
            degree = "",
            dates = ""
        )
    ),
    // Skills
    val skills: String = "",
    // Additional sections
    val projects: String = "",
    val languages: String = "",
    val includeSponsorFooter: Boolean = true
)

enum class TemplateTier {
    FREE, REWARDED
}

data class TemplateInfo(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val tier: TemplateTier,
    val category: String, // "Modern", "ATS-friendly", "Regional"
    val defaultAccent: String
)
