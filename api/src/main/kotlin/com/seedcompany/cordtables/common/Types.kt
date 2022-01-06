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
    InputMissingVersion,
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
    MissingTicketId,
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

enum class MimeTypes(val value: String) {
  ApplicationMsWord("application/msword"),
  ApplicationPdf("application/pdf"),
  ApplicationPostscript("application/postscript"),
  ApplicationRtf("application/rtf"),
  ApplicationVndMsExcel("application/vnd.ms-excel"),
  ApplicationVndMsExcelSheetMacroEnabled12("application/vnd.ms-excel.sheet.macroEnabled.12"),
  ApplicationVndMsExcelSheetBinaryMacroEnabled12("application/vnd.ms-excel.sheet.binary.macroEnabled.12"),
  ApplicationVndMsOutlook("application/vnd.ms-outlook"),
  ApplicationOctetStream("application/octet-stream"),
  ApplicationVndMsPowerpoint("application/vnd.ms-powerpoint"),
  ApplicationVndMsProject("application/vnd.ms-project"),
  ApplicationVndOasisOpenDocumentChart("application/vnd.oasis.opendocument.chart"),
  ApplicationVndOasisOpenDocumentChartTemplate("application/vnd.oasis.opendocument.chart-template"),
  ApplicationVndOasisOpenDocumentDatabase("application/vnd.oasis.opendocument.database"),
  ApplicationVndOasisOpenDocumentGraphics("application/vnd.oasis.opendocument.graphics"),
  ApplicationVndOasisOpenDocumentGraphicsTemplate("application/vnd.oasis.opendocument.graphics-template"),
  ApplicationVndOasisOpenDocumentImage("application/vnd.oasis.opendocument.image"),
  ApplicationVndOasisOpenDocumentImageTemplate("application/vnd.oasis.opendocument.image-template"),
  ApplicationVndOasisOpenDocumentPresentation("application/vnd.oasis.opendocument.presentation"),
  ApplicationVndOasisOpenDocumentPresentationTemplate("application/vnd.oasis.opendocument.presentation-template"),
  ApplicationVndOasisOpenDocumentSpreadsheet("application/vnd.oasis.opendocument.spreadsheet"),
  ApplicationVndOasisOpenDocumentSpreadsheetTemplate("application/vnd.oasis.opendocument.spreadsheet-template"),
  ApplicationVndOasisOpenDocumentText("application/vnd.oasis.opendocument.text"),
  ApplicationVndOasisOpenDocumentTextMaster("application/vnd.oasis.opendocument.text-master"),
  ApplicationVndOasisOpenDocumentTextTemplate("application/vnd.oasis.opendocument.text-template"),
  ApplicationVndOasisOpenDocumentTextWeb("application/vnd.oasis.opendocument.text-web"),
  ApplicationVndOpenXMLFormatsOfficeDocumentPresentationmlSlide("application/vnd.openxmlformats-officedocument.presentationml.slide"),
  ApplicationVndOpenXMLFormatsOfficeDocumentPresentationmlSlideshow("application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
  ApplicationVndOpenXMLFormatsOfficeDocumentPresentationmlTemplate("application/vnd.openxmlformats-officedocument.presentationml.template"),
  ApplicationVndOpenXMLFormatsOfficeDocumentSpreadsheetmlSheet("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
  ApplicationVndOpenXMLFormatsOfficeDocumentSpreadsheetmlTemplate("application/vnd.openxmlformats-officedocument.spreadsheetml.template"),
  ApplicationVndOpenXMLFormatsOfficeDocumentWordprocessingmlDocument("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
  ApplicationVndOpenXMLFormatsOfficeDocumentWordprocessingmlTemplate("application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
  ApplicationVndVisio("application/vnd.visio"),
  ApplicationVndWordPerfect("application/vnd.wordperfect"),
  ApplicationXFontGhostscript("application/x-font-ghostscript"),
  ApplicationXFontLinuxPsf("application/x-font-linux-psf"),
  ApplicationXFontPcf("application/x-font-pcf"),
  ApplicationXFontSnf("application/x-font-snf"),
  ApplicationXFontType1("application/x-font-type1"),
  ApplicationXGtar("application/x-gtar"),
  ApplicationXIso9660Image("application/x-iso9660-image"),
  ApplicationXMsWmd("application/x-ms-wmd"),
  ApplicationXMsAccess("application/x-msaccess"),
  ApplicationXMsPublisher("application/x-mspublisher"),
  ApplicationXMsWrite("application/x-mswrite"),
  ApplicationXTar("application/x-tar"),
  ApplicationXTex("application/x-tex"),
  ApplicationXTexTfm("application/x-tex-tfm"),
  ApplicationXTexInfo("application/x-texinfo"),
  ApplicationXZipCompressed("application/x-zip-compressed"),
  ApplicationZip("application/zip"),
  AudioAdpcm("audio/adpcm"),
  AudioBasic("audio/basic"),
  AudioMidi("audio/midi"),
  AudioMp4("audio/mp4"),
  AudioMpeg("audio/mpeg"),
  AudioOgg("audio/ogg"),
  AudioS3m("audio/s3m"),
  AudioSilk("audio/silk"),
  AudioVndRip("audio/vnd.rip"),
  AudioWebm("audio/webm"),
  AudioXAac("audio/x-aac"),
  AudioXAiff("audio/x-aiff"),
  AudioXCaf("audio/x-caf"),
  AudioXFlac("audio/x-flac"),
  AudioXMatroska("audio/x-matroska"),
  AudioXMpegurl("audio/x-mpegurl"),
  AudioXMsWax("audio/x-ms-wax"),
  AudioXMsWma("audio/x-ms-wma"),
  AudioXpnRealaudio("audio/xpn-realaudio"),
  AudioXWav("audio/x-wav"),
  AudioXm("audio/xm"),
  FontOtf("font/otf"),
  FontTtf("font/ttf"),
  FontWoff("font/woff"),
  FontWoff2("font/woff2"),
  ImageBmp("image/bmp"),
  ImageCgm("image/cgm"),
  ImageG3fax("image/g3fax"),
  ImageGif("image/gif"),
  ImageIef("image/ief"),
  ImageJpeg("image/jpeg"),
  ImageKtx("image/ktx"),
  ImagePng("image/png"),
  ImageSgi("image/sgi"),
  ImageSvgXml("image/svg+xml"),
  ImageTiff("image/tiff"),
  ImageVndAdobePhotoshop("image/vnd.adobe.photoshop"),
  ImageVndDwg("image/vnd.dwg"),
  ImageVndDxf("image/vnd.dxf"),
  ImageX3ds("image/x-3ds"),
  ImageXCmuRaster("image/x-cmu-raster"),
  ImageXCmx("image/x-cmx"),
  ImageXFreehand("image/x-freehand"),
  ImageXIcon("image/x-icon"),
  ImageXMrsidImage("image/x-mrsid-image"),
  ImageXPcx("image/x-pcx"),
  ImageXPict("image/x-pict"),
  ImageXPortableAnymap("image/x-portable-anymap"),
  ImageXPortableBitmap("image/x-portable-bitmap"),
  ImageXPortableGraymap("image/x-portable-graymap"),
  ImageXPortablePixmap("image/x-portable-pixmap"),
  ImageXRgb("image/x-rgb"),
  ImageXTga("image/x-tga"),
  ImageXBitmap("image/x-xbitmap"),
  ImageXXpixmap("image/x-xpixmap"),
  ImageXwindowdump("image/xwindowdump"),
  MessageRfc822("message/rfc822"),
  TextCalendar("text/calendar"),
  TextCss("text/css"),
  TextCsv("text/csv"),
  TextHtml("text/html"),
  TextPlain("text/plain"),
  TextRichtext("text/richtext"),
  TextRtf("text/rtf"),
  TextSgml("text/sgml"),
  TextTabSeparatedValues("text/tab-separated-values"),
  Video3gpp("video/3gpp"),
  Video3gp2("video/3gp2"),
  VideoH261("video/h261"),
  VideoH263("video/h263"),
  VideoH264("video/h264"),
  VideoJpeg("video/jpeg"),
  VideoJpm("video/jpm"),
  VideoMj2("video/mj2"),
  VideoMp4("video/mp4"),
  VideoMpeg("video/mpeg"),
  VideoOgg("video/ogg"),
  VideoQuicktime("video/quicktime"),
  VideoVndMpegurl("video/vnd.mpegurl"),
  VideoVndVivo("video/vnd.vivo"),
  VideoWebm("video/webm"),
  VideoXF4v("video/x-f4v"),
  VideoXFli("video/x-fli"),
  VideoXFlv("video/x-flv"),
  VideoXM4v("video/x-m4v"),
  VideoXMatroska("video/x-matroska"),
  VideoXMng("video/x-mng"),
  VideoXMsAsf("video/x-ms-asf"),
  VideoXMsVob("video/x-ms-vob"),
  VideoXMsWm("video/x-ms-wm"),
  VideoXMsWmv("video/x-ms-wmv"),
  VideoXMsWmx("video/x-ms-wmx"),
  VideoXMsWvx("video/x-ms-wvx"),
  VideoXMsVideo("video/x-msvideo"),
  VideoXSgiMovie("video/x-sgi-movie"),
  VideoXSmv("video/x-smv")
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

data class PeopleDetails(
  val id: Int,
  val public_first_name: String?,
  val public_last_name: String?
)
