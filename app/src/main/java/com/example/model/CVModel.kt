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
    val fullName: String = "Td",
    val professionalTitle: String = "Product Designer",
    val email: String = "alex@example.com",
    val phone: String = "+1 555 0100",
    val location: String = "Austin, TX",
    val website: String = "portfolio.com",
    val professionalSummary: String = "Product designer with 5+ years crafting cohesive systems and user experiences across mobile and web. Passionate about typography, intuitive interaction, and measurable business impact.",
    // Experiences
    val experiences: List<ExperienceItem> = listOf(
        ExperienceItem(
            jobTitle = "Senior Product Designer",
            company = "Acme Studio",
            dates = "Jan 2022 — Present",
            achievements = "Led the redesign of onboarding, improving activation by 24%. Mentored 4 junior designers and spearheaded design system revamp."
        ),
        ExperienceItem(
            jobTitle = "UI/UX Designer",
            company = "FinTech Labs",
            dates = "2019 — 2021",
            achievements = "Created responsive checkout flows reducing cart drop-off by 18%. Collaborated closely with mobile engineering."
        )
    ),
    // Education
    val educations: List<EducationItem> = listOf(
        EducationItem(
            school = "State University",
            degree = "BA, Interaction Design",
            dates = "2017 — 2021"
        )
    ),
    // Skills
    val skills: String = "Product strategy, Figma, User research, Prototyping, Design systems, Design tokens",
    // Additional sections
    val projects: String = "Google UX Design Certificate · 2024\nDesign System Architecture Workshop · 2023",
    val languages: String = "English — Native, Spanish — Professional",
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
