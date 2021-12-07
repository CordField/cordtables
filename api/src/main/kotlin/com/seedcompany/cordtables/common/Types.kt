package com.seedcompany.cordtables.common

class Types {
}

enum class ErrorType {
    NoError,
    AdminOnly,
    UnknownError,
    DoesNotHaveCreatePermission,
    BadCredentials,
    TokenNotFound,
    EmailIsBlocked,
    SessionNotFound,
    InvalidEmail,
    InvalidToken,
    PasswordTooShort,
    PasswordTooLong,
    DuplicateEmail,
    UserNotFound,
    SQLInsertError,
    SQLReadError,
    SQLUpdateError,
    SQLDeleteError,
    InputMissingName,
    InputMissingGroup,
    InputMissingPerson,
    InputMissingTableName,
    InputMissingRow,
    MembershipAlreadyExists,
    NameTooShort,
    NameTooLong,
    NameAlreadyExists,
    MissingId,
    UserTokenNotFound,
    emptyReadResult,
    CannotUpdateAdminGroup,
    DoesNotHaveUpdatePermission,
    DoesNotHaveDeletePermission,
    InputMissingUrl,
    UrlTooShort,
    UrlTooLong,
    UrlAlreadyExists,
    InputMissingSecret,
    SecretNotValid,
    PeerAlreadyExists,
    PeerFailedToInitialize,
    PeerNotPresent,
    PeerFailedToConfirm,
    PeerFailedToLogin,
    InputMissingToken,
    InputMissingTable,
    InputMissingColumn,
    ValueDoesNotMap,
}


enum class CommonSensitivity {
    Low,
    Medium,
    High,
}

enum class CommonTicketFeedbackOptions {
    Satisfied,
    Unsatisfied,
}

enum class CommonTicketStatus{
    Open,
    Blocked,
    Closed
}

data class GenericResponse (
    val error: ErrorType,
)

enum class LocationType {
    City,
    County,
    State,
    Country,
    CrossBorderArea,
}

enum class MimeTypes{
    A,
    B,
    C,
}

enum class PeopleToOrgRelationshipType{
    Vendor,
    Customer,
    Investor,
    Associate,
    Employee,
    Member,
    Executive,
    PresidentCEO,
    BoardOfDirectors,
    Retired,
    Other,
}

enum class InvolvementOptions{
    CIT,
    Engagements
}

enum class GlobalPartnerRoles{
    A,
    B
}


enum class TableNames(val value: String)  {
    AdminDatabaseVersionControl("admin.database_version_control"),
    AdminEmailTokens("admin.email_tokens"),
    AdminGroupMemberships("admin.group_memberships"),
    AdminGroupRowAccess("admin.group_row_access"),
    AdminGroups("admin.groups"),
    AdminPeers("admin.peers"),
    AdminPeople("admin.people"),
    AdminRoleColumnGrants("admin.role_column_grants"),
    AdminRoleMemberships("admin.role_memberships"),
    AdminRoleTablePermissions("admin.role_table_permissions"),
    AdminRoles("admin.roles"),
    AdminTokens("admin.tokens"),
    AdminUsers("admin.users"),
    CommonBlogs("common.blogs"),
    CommonBlogPosts("common.blog_posts"),
    CommonCellChannels("common.cell_channels"),
    CommonCoalitionMemberships("common.coalition_memberships"),
    CommonCoalitions("common.coalitions"),
    CommonDirectories("common.directories"),
    CommonDiscussionChannels("common.discussion_channels"),
    CommonEducationByPerson("common.education_by_person"),
    CommonEducationEntries("common.education_entries"),
    CommonFileVersions("common.file_versions"),
    CommonFiles("common.files"),
    CommonLocations("common.locations"),
    CommonNotes("common.notes"),
    CommonOrganizations("common.organizations"),
    CommonPeopleGraph("common.people_graph"),
    CommonPeopleToOrgRelationships("common.people_to_org_relationships"),
    CommonPosts("common.posts"),
    CommonScriptureReferences("common.scripture_references"),
    CommonSiteText("common.site_text"),
    CommonStageGraph("common.stage_graph"),
    CommonStageNotifications("common.stage_notifications"),
    CommonStageRoleColumnGrants("common.stage_role_column_grants"),
    CommonStages("common.stages"),
    CommonThreads("common.threads"),
    CommonTicketAssignments("common.ticket_assignments"),
    CommonTicketFeedback("common.ticket_feedback"),
    CommonTicketGraph("common.ticket_graph"),
    CommonTickets("common.tickets"),
    CommonWorkEstimates("common.work_estimates"),
    CommonWorkRecords("common.work_records"),
    CommonWorkflows("common.workflows"),

    SilCountryCodes("sil.country_codes"),
    SilLanguageCodes("sil.language_codes"),
    SilLanguageIndex("sil.language_index"),
    ScEthnologue("sc.ethnologue"),

    ScBudgetRecords("sc.budget_records"),
    ScBudgets("sc.budgets"),
    ScCeremonies("sc.ceremonies"),
    ScChangeToPlans("sc.change_to_plans"),
    ScFieldRegions ("sc.field_regions"),
    ScFieldZones("sc.field_zones"),
    ScFundingAccounts("sc.funding_accounts"),
    ScGlobalPartnerAssessments("sc.global_partner_assessments"),
    ScGlobalPartnerEngagements("sc.global_partner_engagements"),
    ScGlobalPartnerEngagementPeople("sc.global_partner_engagement_people"),
    ScGlobalPartnerPerformance("sc.global_partner_performance"),
    ScInternshipEngagements("sc.internship_engagements"),
    ScKnownLanguagesByPerson("sc.known_languages_by_person"),
    ScLanguageEngagements("sc.language_engagements"),
    ScLanguages("sc.languages"),
    ScLocations("sc.locations"),
    ScOrganizationLocations("sc.organization_locations"),
    ScOrganizations("sc.organizations"),
    ScPartners("sc.partners"),
    ScPartnerships("sc.partnerships"),
    ScPeople("sc.people"),
    ScPeriodicReports("sc.periodic_reports"),
    ScPersonUnavailabilities("sc.person_unavailabilities"),
    ScPinnedProjects("sc.pinned_projects"),
    ScPosts("sc.posts"),
    ScProductScriptureReferences("sc.product_scripture_references"),
    ScProducts("sc.products"),
    ScProjectLocations("sc.project_locations"),
    ScProjectMembers("sc.project_members"),
    ScProjects("sc.projects")
}

enum class AccessLevels{
    Read,
    Write,
}
