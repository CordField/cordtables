package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.Create
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource
import kotlin.math.ceil

data class BaseNodeCreate(
  val baseNode: String,
  val tableName: String,
)

data class BaseNodePairCreate(
  val baseNode: String,
  val secondBaseNode: String,
  val type: String,
  val column: String,
  val secondColumn: String,
  val tableName: String
)

data class RelationshipCreate(
  val fromBaseNode: String,
  val toBaseNode: String,
  val type: String,
  val table: String,
  val column: String,
  val baseNodeToPlaceInColumn: String,
)

data class PropertyCreate(
  val baseNode: String,
  val property: String,
  val table: String,
  val column: String,
  val enum: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("Neo4jMigration2")
class Neo4j2(
  @Autowired
  val util: Utility,

  @Autowired
  val neo4j: Driver,

  @Autowired
  val ds: DataSource,

  @Autowired
  val update: Update,

  @Autowired
  val read: Read,

  @Autowired
  val users: Create,
) {
  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

  var baseNodesMigrated = AtomicInteger()
  var relationshipsMigrated = AtomicInteger()
  var propertiesMigrated = AtomicInteger()

  var adminPersonId: String? = null
  var adminGroupId: String? = null

  @PostMapping("migrate/neo4j2")
  @ResponseBody
  suspend fun createHandler(@RequestBody req: Neo4jMigrationRequest): Neo4jMigrationResponse {

    baseNodesMigrated.set(0)
    relationshipsMigrated.set(0)
    propertiesMigrated.set(0)

    adminPersonId = jdbcTemplate.queryForObject(
      """
      select id from admin.people order by created_at asc offset 0 limit 1;
    """.trimIndent(),
      String::class.java
    )

    adminGroupId = jdbcTemplate.queryForObject(
      """
      select id from admin.groups order by created_at asc offset 0 limit 1;
    """.trimIndent(),
      String::class.java
    )

    if (adminPersonId != null && adminGroupId != null) {
      migrateBaseNodes()
      migrateBaseNodePairs()
      migrateRelationships()
      migrateProperties()
    }

    return Neo4jMigrationResponse(ErrorType.NoError)
  }

  suspend fun migrateProperties() {
    val queue = ConcurrentLinkedQueue<PropertyCreate>()

    queue.offer(
      PropertyCreate(
        baseNode = "Budget",
        property = "universalTemplateFile",
        table = "sc.budgets",
        column = "universal_template_file_url"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Budget",
        property = "status",
        table = "sc.budgets",
        column = "status",
        enum = "common.budget_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "BudgetRecord",
        property = "fiscalYear",
        table = "sc.budget_records",
        column = "fiscal_year"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "BudgetRecord",
        property = "amount",
        table = "sc.budget_records",
        column = "amount"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Ceremony",
        property = "type",
        table = "sc.ceremonies",
        column = "type",
        enum = "common.ceremony_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Ceremony",
        property = "actualDate",
        table = "sc.ceremonies",
        column = "actual_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Ceremony",
        property = "estimatedDate",
        table = "sc.ceremonies",
        column = "estimated_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Ceremony",
        property = "planned",
        table = "sc.ceremonies",
        column = "is_planned"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Directory",
        property = "name",
        table = "common.directories",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Education",
        property = "major",
        table = "common.education_entries",
        column = "major"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Education",
        property = "institution",
        table = "common.education_entries",
        column = "institution"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Education",
        property = "degree",
        table = "common.education_entries",
        column = "degree",
        enum = "common.education_degree"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "EthnologueLanguage",
        property = "name",
        table = "sc.ethnologue",
        column = "language_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "EthnologueLanguage",
        property = "code",
        table = "sc.ethnologue",
        column = "code"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "EthnologueLanguage",
        property = "provisionalCode",
        table = "sc.ethnologue",
        column = "provisional_code"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "EthnologueLanguage",
        property = "population",
        table = "sc.ethnologue",
        column = "population"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FieldRegion",
        property = "name",
        table = "sc.field_regions",
        column = "name",
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FieldZone",
        property = "name",
        table = "sc.field_zones",
        column = "name",
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "File",
        property = "name",
        table = "common.files",
        column = "name",
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FileVersion",
        property = "name",
        table = "common.file_versions",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FileVersion",
        property = "size",
        table = "common.file_versions",
        column = "file_size"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FileVersion",
        property = "mimeType",
        table = "common.file_versions",
        column = "mime_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FundingAccount",
        property = "accountNumber",
        table = "sc.funding_accounts",
        column = "account_number"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "FundingAccount",
        property = "name",
        table = "sc.funding_accounts",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "completeDate",
        table = "sc.internship_engagements",
        column = "complete_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "disbursementCompleteDate",
        table = "sc.internship_engagements",
        column = "disbursement_complete_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "endDateOverride",
        table = "sc.internship_engagements",
        column = "end_date_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "initialEndDate",
        table = "sc.internship_engagements",
        column = "initial_end_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "lastReactivatedAt",
        table = "sc.internship_engagements",
        column = "last_reactivated_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "lastSuspendedAt",
        table = "sc.internship_engagements",
        column = "last_suspended_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "methodologies",
        table = "sc.internship_engagements",
        column = "methodologies",
        enum = "common.product_methodologies[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "position",
        table = "sc.internship_engagements",
        column = "position",
        enum = "common.internship_position"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "startDateOverride",
        table = "sc.internship_engagements",
        column = "start_date_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "status",
        table = "sc.internship_engagements",
        column = "status",
        enum = "common.engagement_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "InternshipEngagement",
        property = "statusModifiedAt",
        table = "sc.internship_engagements",
        column = "status_modified_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "name",
        table = "sc.languages",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "displayName",
        table = "sc.languages",
        column = "display_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "displayNamePronunciation",
        table = "sc.languages",
        column = "display_name_pronunciation"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "tags",
        table = "sc.languages",
        column = "tags"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "isDialect",
        table = "sc.languages",
        column = "is_dialect"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "isSignLanguage",
        table = "sc.languages",
        column = "is_sign_language"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "leastOfThese",
        table = "sc.languages",
        column = "is_least_of_these"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "leastOfTheseReason",
        table = "sc.languages",
        column = "least_of_these_reason"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "populationOverride",
        table = "sc.languages",
        column = "population_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "registryOfDialectsCode",
        table = "sc.languages",
        column = "registry_of_dialects_code"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "signLanguageCode",
        table = "sc.languages",
        column = "sign_language_code"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "hasExternalFirstScripture",
        table = "sc.languages",
        column = "has_external_first_scripture"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "sensitivity",
        table = "sc.languages",
        column = "sensitivity",
        enum = "common.sensitivity"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Language",
        property = "sponsorEstimatedEndDate",
        table = "sc.languages",
        column = "sponsor_estimated_end_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "openToInvestorVisit",
        table = "sc.language_engagements",
        column = "is_open_to_investor_visit"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "completeDate",
        table = "sc.language_engagements",
        column = "complete_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "disbursementCompleteDate",
        table = "sc.language_engagements",
        column = "disbursement_complete_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "endDateOverride",
        table = "sc.language_engagements",
        column = "end_date_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "initialEndDate",
        table = "sc.language_engagements",
        column = "initial_end_date"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "firstScripture",
        table = "sc.language_engagements",
        column = "is_first_scripture"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "lukePartnership",
        table = "sc.language_engagements",
        column = "is_luke_partnership"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "lastSuspendedAt",
        table = "sc.language_engagements",
        column = "last_suspended_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "lastReactivatedAt",
        table = "sc.language_engagements",
        column = "last_reactivated_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "paratextRegistryId",
        table = "sc.language_engagements",
        column = "paratext_registry"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "pnp",
        table = "sc.language_engagements",
        column = "pnp"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "startDateOverride",
        table = "sc.language_engagements",
        column = "start_date_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "status",
        table = "sc.language_engagements",
        column = "status",
        enum = "common.engagement_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "statusModifiedAt",
        table = "sc.language_engagements",
        column = "status_modified_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "LanguageEngagement",
        property = "historicGoal",
        table = "sc.language_engagements",
        column = "historic_goal"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "name",
        table = "common.locations",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "isoAlpha3",
        table = "common.locations",
        column = "iso_alpha3"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "type",
        table = "common.locations",
        column = "type",
        enum = "common.location_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "name",
        table = "sc.locations",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "isoAlpha3",
        table = "sc.locations",
        column = "iso_alpha_3"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Location",
        property = "type",
        table = "sc.locations",
        column = "type",
        enum = "common.location_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Organization",
        property = "name",
        table = "common.organizations",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Organization",
        property = "address",
        table = "sc.organizations",
        column = "address"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "pmcEntityCode",
        table = "sc.partners",
        column = "pmc_entity_code"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "address",
        table = "sc.partners",
        column = "address"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "globalInnovationsClient",
        table = "sc.partners",
        column = "is_innovations_client"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "active",
        table = "sc.partners",
        column = "active"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "types",
        table = "sc.partners",
        column = "types",
        enum = "sc.partner_types[]"
      )
    )
    queue.offer(
      PropertyCreate(
        baseNode = "Partner",
        property = "financialReportingTypes",
        table = "sc.partners",
        column = "financial_reporting_types",
        enum = "sc.financial_reporting_types[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "agreementStatus",
        table = "sc.partnerships",
        column = "agreement_status",
        enum = "sc.partnership_agreement_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "mouStatus",
        table = "sc.partnerships",
        column = "mou_status",
        enum = "sc.partnership_agreement_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "mouStartOverride",
        table = "sc.partnerships",
        column = "mou_start_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "mouEndOverride",
        table = "sc.partnerships",
        column = "mou_end_override"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "financialReportingType",
        table = "sc.partnerships",
        column = "financial_reporting_type",
        enum = "sc.financial_reporting_types"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "primary",
        table = "sc.partnerships",
        column = "is_primary"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Partnership",
        property = "types",
        table = "sc.partnerships",
        column = "types",
        enum = "sc.partner_types[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "PeriodicReport",
        property = "end",
        table = "sc.periodic_reports",
        column = "end_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "PeriodicReport",
        property = "start",
        table = "sc.periodic_reports",
        column = "start_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "PeriodicReport",
        property = "type",
        table = "sc.periodic_reports",
        column = "type",
        enum = "sc.periodic_report_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "PeriodicReport",
        property = "skippedReason",
        table = "sc.periodic_reports",
        column = "skipped_reason"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Post",
        property = "type",
        table = "sc.posts",
        column = "type",
        enum = "sc.post_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Post",
        property = "shareability",
        table = "sc.posts",
        column = "shareability",
        enum = "sc.post_shareability"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Post",
        property = "body",
        table = "sc.posts",
        column = "body"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "methodology",
        table = "sc.products",
        column = "methodology",
        enum = "common.product_methodologies"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "steps",
        table = "sc.products",
        column = "steps",
        enum = "common.product_methodology_step[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "progressStepMeasurement",
        table = "sc.products",
        column = "progress_step_measurement",
        enum = "common.progress_measurement"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "progressTarget",
        table = "sc.products",
        column = "progress_target"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "mediums",
        table = "sc.products",
        column = "mediums",
        enum = "common.product_mediums[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "purposes",
        table = "sc.products",
        column = "purposes",
        enum = "common.product_purposes[]"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "describeCompletion",
        table = "sc.products",
        column = "describe_completion"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "description",
        table = "sc.products",
        column = "description"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Product",
        property = "title",
        table = "sc.products",
        column = "title"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Producible",
        property = "name",
        table = "sc.products",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "name",
        table = "sc.projects",
        column = "name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "tags",
        table = "sc.projects",
        column = "tags"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "presetInventory",
        table = "sc.projects",
        column = "preset_inventory"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "estimatedSubmission",
        table = "sc.projects",
        column = "estimated_submission"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "initialMouEnd",
        table = "sc.projects",
        column = "initial_mou_end"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "mouStart",
        table = "sc.projects",
        column = "mou_start"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "mouEnd",
        table = "sc.projects",
        column = "mou_end"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "step",
        table = "sc.projects",
        column = "step",
        enum = "sc.project_step"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "stepChangedAt",
        table = "sc.projects",
        column = "step_changed_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "status",
        table = "sc.projects",
        column = "status",
        enum = "sc.project_status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "sensitivity",
        table = "sc.projects",
        "sensitivity",
        enum = "common.sensitivity"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "type",
        table = "sc.projects",
        column = "type",
        enum = "sc.project_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "financialReportReceivedAt",
        table = "sc.projects",
        column = "report_received_at"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Project",
        property = "departmentId",
        table = "sc.projects",
        column = "department"
      )
    )

    // Producibles

    queue.offer(
      PropertyCreate(
        baseNode = "EthnoArt",
        property = "name",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Film",
        property = "name",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Story",
        property = "name",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Song",
        property = "name",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "DirectScriptureProduct",
        property = "methodology",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "DerivativeScriptureProduct",
        property = "methodology",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "OtherProduct",
        property = "title",
        table = "sc.products",
        column = "type",
        enum = "sc.product_type"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "ScriptureRange",
        property = "range",
        table = "common.scripture_references",
        column = ""
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "StepProgress",
        property = "completed",
        table = "sc.step_progress",
        column = "completed"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "StepProgress",
        property = "step",
        table = "sc.step_progress",
        column = "step",
        enum = "common.product_methodology_step"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "about",
        table = "admin.people",
        column = "about"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "phone",
        table = "admin.people",
        column = "phone"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "realFirstName",
        table = "admin.people",
        column = "private_first_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "realLastName",
        table = "admin.people",
        column = "private_last_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "displayFirstName",
        table = "admin.people",
        column = "public_first_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "displayLastName",
        table = "admin.people",
        column = "public_last_name"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "timezone",
        table = "admin.people",
        column = "timezone"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "title",
        table = "admin.people",
        column = "title"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "email",
        table = "admin.users",
        column = "email"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "password",
        table = "admin.users",
        column = "password"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "User",
        property = "status",
        table = "sc.people",
        column = "status"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Unavailability",
        property = "description",
        table = "sc.person_unavailabilities",
        column = "description"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Unavailability",
        property = "start",
        table = "sc.person_unavailabilities",
        column = "period_start"
      )
    )

    queue.offer(
      PropertyCreate(
        baseNode = "Unavailability",
        property = "end",
        table = "sc.person_unavailabilities",
        column = "period_end"
      )
    )

    val migrationStart = DateTime.now().millis

    val concurrency = 1

    coroutineScope {
      for (t in 1..concurrency) {
        launch {
          println("coroutine $t: start")
          delay(1)
          while (true) {
            val createRequest = queue.poll() ?: break
            println("coroutine $t, fetching ${createRequest.baseNode}-${createRequest.property}")
            migratePropertyByType(
              adminPersonId = adminPersonId!!,
              adminGroupId = adminGroupId!!,
              baseNode = createRequest.baseNode,
              property = createRequest.property,
              tableName = createRequest.table,
              column = createRequest.column,
              enum = createRequest.enum,
            )
            delay(1)
          }
          println("coroutine $t: end")
        }
        delay(1)
      }
    }

    println("${propertiesMigrated.get()} Properties in ${(DateTime.now().millis - migrationStart) / 1000F} seconds")
  }

  suspend fun migratePropertyByType(
    adminPersonId: String,
    adminGroupId: String,
    baseNode: String,
    property: String,
    tableName: String,
    column: String,
    enum: String? = null
  ) {

    val batchSize = 2000

    ds.connection.use { conn ->

      val updateStatement: PreparedStatement = if (enum != null) {
        conn.prepareStatement(
          """
          update $tableName set $column = cast(? as $enum) where id = common.uuid_generate_v5(common.uuid_ns_url(), ?)
        """.trimIndent()
        )
      } else if (baseNode == "ScriptureRange") {
        conn.prepareStatement(
          """
          update $tableName set range_start = ?, range_end = ? where id = common.uuid_generate_v5(common.uuid_ns_url(), ?)
        """.trimIndent())
      }
        else {
        conn.prepareStatement(
          """
          update $tableName set $column = ? where id = common.uuid_generate_v5(common.uuid_ns_url(), ?);
        """.trimIndent()
        )
      }

      neo4j.session().use { session ->

        val propertyCount =
          session.run("MATCH (n:$baseNode)-[r:$property]-() RETURN count(r) as count").single()
            .get("count").asInt()

        for (i in 0..ceil((propertyCount / batchSize).toDouble()).toInt()) {
          val query =
            when {
              baseNode == "Project" && property == "type" ->
                "MATCH (n:$baseNode) RETURN n.type, n.id skip ${i * batchSize} limit $batchSize"
              baseNode == "ScriptureRange" ->
                "MATCH (n:$baseNode) RETURN n.start, n.end, id(n) skip ${i * batchSize} limit $batchSize"
              baseNode == "StepProgress" && property == "step" ->
                "MATCH (n:$baseNode) RETURN n.step, n.id skip ${i * batchSize} limit $batchSize"
              else ->  {
                "MATCH (n:$baseNode)-[r:$property { active: true }]-(p) RETURN n.id, p.value, apoc.meta.type(p.value) as type skip ${i * batchSize} limit $batchSize"
              }
            }
          session.run(query)
            .list()
            .forEach {
              when {
                it.get("type").asString() == "FLOAT" ->
                  updateStatement.setDouble(1, it.get("p.value").asDouble())
                it.get("type").asString() == "STRING" -> {
                  if (baseNode in arrayOf("Story", "Film", "Song", "EthnoArt", "DirectScriptureProduct", "DerivativeScriptureProduct", "OtherProduct"))
                    updateStatement.setString(1, baseNode)
                  else updateStatement.setString(1, it.get("p.value").asString())
                }
                it.get("type").asString() == "BOOLEAN" ->
                  updateStatement.setBoolean(1, it.get("p.value").asBoolean())
                it.get("type").asString() == "LocalDate" ->
                  updateStatement.setDate(1, Date.valueOf(it.get("p.value").asLocalDate()))
                it.get("type").asString() == "ZonedDateTime" ->
                  updateStatement.setTimestamp(1, Timestamp.valueOf(it.get("p.value").asZonedDateTime().toLocalDateTime()))
                it.get("type").asString() == "String[]" ->
                  updateStatement.setArray(1, conn.createArrayOf("text", it.get("p.value").asList().toTypedArray()))
                baseNode == "Project" && property == "type" ->
                  updateStatement.setString(1, it.get("n.type").asString())
                baseNode == "StepProgress" && property == "step" ->
                  updateStatement.setString(1, it.get("n.step").asString())
                baseNode == "ScriptureRange" -> {
                  updateStatement.setInt(1, it.get("n.start").asInt())
                  updateStatement.setInt(2, it.get("n.end").asInt())
                }
                else ->
                  if (property in arrayOf("planned", "lukePartnership", "firstScripture", "openToInvestorVisit")) {
                    updateStatement.setBoolean(1, false)
                  } else
                  return@forEach
              }
              if (baseNode == "ScriptureRange") updateStatement.setString(3, it.get("id(n)").asInt().toString())
              else updateStatement.setString(2, it.get("n.id").asString())
              updateStatement.addBatch()
            }
          updateStatement.executeBatch()
        }

        propertiesMigrated.addAndGet(propertyCount)
        println("total $property: $propertyCount")
      }

    }

  }

  suspend fun migrateRelationships() {
    val queue = ConcurrentLinkedQueue<RelationshipCreate>()

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "File",
        type = "parent",
        toBaseNode = "Directory",
        table = "common.files",
        column = "directory",
        baseNodeToPlaceInColumn = "Directory",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Directory",
        type = "parent",
        toBaseNode = "Directory",
        table = "common.directories",
        column = "parent",
        baseNodeToPlaceInColumn = "Directory"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "user",
        toBaseNode = "ProjectMember",
        table = "sc.project_members",
        column = "person",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "director",
        toBaseNode = "FieldRegion",
        table = "sc.field_regions",
        column = "director",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "director",
        toBaseNode = "FieldZone",
        table = "sc.field_zones",
        column = "director",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Budget",
        type = "universalTemplateFileNode",
        toBaseNode = "File",
        table = "sc.budgets",
        column = "universal_template",
        baseNodeToPlaceInColumn = "File",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Budget",
        type = "record",
        toBaseNode = "BudgetRecord",
        table = "sc.budget_records",
        column = "budget",
        baseNodeToPlaceInColumn = "Budget",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Organization",
        type = "organization",
        toBaseNode = "BudgetRecord",
        table = "sc.budget_records",
        column = "organization",
        baseNodeToPlaceInColumn = "Organization",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "owningOrganization",
        toBaseNode = "Organization",
        table = "sc.projects",
        column = "owning_organization",
        baseNodeToPlaceInColumn = "Organization"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "budget",
        toBaseNode = "Budget",
        table = "sc.budgets",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "member",
        toBaseNode = "ProjectMember",
        table = "sc.project_members",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "partnership",
        toBaseNode = "Partnership",
        table = "sc.partnerships",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "BaseFile",
        type = "reportFileNode",
        toBaseNode = "PeriodicReport",
        table = "sc.periodic_reports",
        column = "report_file",
        baseNodeToPlaceInColumn = "BaseFile",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "FieldRegion",
        type = "fieldRegion",
        toBaseNode = "Project",
        table = "sc.projects",
        column = "field_region",
        baseNodeToPlaceInColumn = "FieldRegion",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "FieldRegion",
        type = "zone",
        toBaseNode = "FieldZone",
        table = "sc.field_regions",
        column = "field_zone",
        baseNodeToPlaceInColumn = "FieldZone"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Directory",
        type = "rootDirectory",
        toBaseNode = "Project",
        table = "sc.projects",
        column = "root_directory",
        baseNodeToPlaceInColumn = "Directory",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "EthnologueLanguage",
        type = "ethnologue",
        toBaseNode = "Language",
        table = "sc.languages",
        column = "ethnologue",
        baseNodeToPlaceInColumn = "EthnologueLanguage",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "engagement",
        toBaseNode = "LanguageEngagement",
        table = "sc.language_engagements",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Language",
        type = "language",
        toBaseNode = "LanguageEngagement",
        table = "sc.language_engagements",
        column = "language",
        baseNodeToPlaceInColumn = "Language",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "engagement",
        toBaseNode = "InternshipEngagement",
        table = "sc.internship_engagements",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "LanguageEngagement",
        type = "product",
        toBaseNode = "Product",
        table = "sc.products",
        column = "engagement",
        baseNodeToPlaceInColumn = "LanguageEngagement"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Partner",
        type = "partner",
        toBaseNode = "Partnership",
        table = "sc.partnerships",
        column = "partner",
        baseNodeToPlaceInColumn = "Partner",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Partner",
        type = "organization",
        toBaseNode = "Organization",
        table = "sc.partners",
        column = "organization",
        baseNodeToPlaceInColumn = "Organization"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "pointOfContact",
        toBaseNode = "Partner",
        table = "sc.partners",
        column = "point_of_contact",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "InternshipEngagement",
        type = "ceremony",
        toBaseNode = "Ceremony",
        table = "sc.ceremonies",
        column = "internship_engagement",
        baseNodeToPlaceInColumn = "InternshipEngagement",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "LanguageEngagement",
        type = "ceremony",
        toBaseNode = "Ceremony",
        table = "sc.ceremonies",
        column = "language_engagement",
        baseNodeToPlaceInColumn = "LanguageEngagement",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "FundingAccount",
        type = "fundingAccount",
        toBaseNode = "Location",
        table = "sc.locations",
        column = "funding_account",
        baseNodeToPlaceInColumn = "FundingAccount",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "FieldRegion",
        type = "defaultFieldRegion",
        toBaseNode = "Location",
        table = "sc.locations",
        column = "default_region",
        baseNodeToPlaceInColumn = "FieldRegion",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Location",
        type = "countryOfOrigin",
        toBaseNode = "InternshipEngagement",
        table = "sc.internship_engagements",
        column = "country_of_origin",
        baseNodeToPlaceInColumn = "Location",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "mentor",
        toBaseNode = "InternshipEngagement",
        table = "sc.internship_engagements",
        column = "mentor",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "intern",
        toBaseNode = "InternshipEngagement",
        table = "sc.internship_engagements",
        column = "intern",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "BaseFile",
        type = "pnpNode",
        toBaseNode = "LanguageEngagement",
        table = "sc.language_engagements",
        column = "pnp_file",
        baseNodeToPlaceInColumn = "BaseFile",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Location",
        type = "primaryLocation",
        toBaseNode = "Project",
        table = "sc.projects",
        column = "primary_location",
        baseNodeToPlaceInColumn = "Location",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Location",
        type = "marketingLocation",
        toBaseNode = "Project",
        table = "sc.projects",
        column = "marketing_location",
        baseNodeToPlaceInColumn = "Location",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "unavailability",
        toBaseNode = "Unavailability",
        table = "sc.person_unavailabilities",
        column = "person",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "createdBy",
        toBaseNode = "File",
        table = "common.files",
        column = "created_by",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "User",
        type = "createdBy",
        toBaseNode = "Directory",
        table = "common.directories",
        column = "created_by",
        baseNodeToPlaceInColumn = "User",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "FileVersion",
        type = "parent",
        toBaseNode = "File",
        table = "common.file_versions",
        column = "file",
        baseNodeToPlaceInColumn = "File"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Partnership",
        type = "mou",
        toBaseNode = "Property",
        table = "sc.partnerships",
        column = "mou",
        baseNodeToPlaceInColumn = ""
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Partnership",
        type = "agreement",
        toBaseNode = "Property",
        table = "sc.partnerships",
        column = "agreement",
        baseNodeToPlaceInColumn = ""
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "InternshipEngagement",
        type = "growthPlan",
        toBaseNode = "Property",
        table = "sc.internship_engagements",
        column = "growth_plan",
        baseNodeToPlaceInColumn = ""
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Post",
        type = "creator",
        toBaseNode = "Property",
        table = "sc.posts",
        column = "creator",
        baseNodeToPlaceInColumn = ""
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "PeriodicReport",
        type = "progress",
        toBaseNode = "ProductProgress",
        table = "sc.product_progress",
        column = "report",
        baseNodeToPlaceInColumn = "PeriodicReport"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Product",
        type = "progress",
        toBaseNode = "ProductProgress",
        table = "sc.product_progress",
        column = "product",
        baseNodeToPlaceInColumn = "Product"
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "ProductProgress",
        type = "step",
        toBaseNode = "StepProgress",
        table = "sc.step_progress",
        column = "product_progress",
        baseNodeToPlaceInColumn = "ProductProgress"
      )
    )

//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "DerivativeScriptureProduct",
//        type = "produces",
//        toBaseNode = "Producible",
//        table = "sc.products",
//        column = "produces",
//        baseNodeToPlaceInColumn = "Producible"
//      )
//    )

    val migrationStart = DateTime.now().millis

    val concurrency = 1

    coroutineScope {
      for (t in 1..concurrency) {
        launch {
          println("coroutine $t: start")
          delay(1)
          while (true) {
            val createRequest = queue.poll() ?: break
            println("coroutine $t, fetching ${createRequest.fromBaseNode}-${createRequest.type}-${createRequest.toBaseNode}")
            migrateRelationshipByType(
              adminPersonId = adminPersonId!!,
              adminGroupId = adminGroupId!!,
              fromBaseNode = createRequest.fromBaseNode,
              toBaseNode = createRequest.toBaseNode,
              type = createRequest.type,
              tableName = createRequest.table,
              column = createRequest.column,
              baseNodeToPlaceInColumn = createRequest.baseNodeToPlaceInColumn,
            )
            delay(1)
          }
          println("coroutine $t: end")
        }
        delay(1)
      }
    }

    println("${relationshipsMigrated.get()} Relationships in ${(DateTime.now().millis - migrationStart) / 1000F} seconds")
  }

  suspend fun migrateRelationshipByType(
    adminPersonId: String,
    adminGroupId: String,
    fromBaseNode: String,
    type: String,
    toBaseNode: String,
    tableName: String,
    column: String,
    baseNodeToPlaceInColumn: String,
  ) {

    val batchSize = 2000

    ds.connection.use { conn ->

      val updateStatement: PreparedStatement = conn.prepareStatement(
        """
        update $tableName set $column = common.uuid_generate_v5(common.uuid_ns_url(), ?) where id = common.uuid_generate_v5(common.uuid_ns_url(), ?);
      """.trimIndent()
      )

      neo4j.session().use { session ->

        val relationshipsCount =
          session.run("MATCH (n:$fromBaseNode)-[r:$type { active: true }]-(m:$toBaseNode) RETURN count(r) as count").single()
            .get("count").asInt()

        for (i in 0..ceil((relationshipsCount / batchSize).toDouble()).toInt()) {

          session.run("MATCH (from:$fromBaseNode)-[r:$type { active: true }]-(to:$toBaseNode) RETURN from.id, to.id, to.value skip ${i * batchSize} limit $batchSize")
            .list()
            .forEach {
              if (toBaseNode == "Property") {
                updateStatement.setString(2, it.get("from.id").asString())
                updateStatement.setString(1, it.get("to.value").asString())
              }
              else if (baseNodeToPlaceInColumn == toBaseNode) {
                updateStatement.setString(1, it.get("to.id").asString())
                updateStatement.setString(2, it.get("from.id").asString())
              } else {
                updateStatement.setString(1, it.get("from.id").asString())
                updateStatement.setString(2, it.get("to.id").asString())
              }
              updateStatement.addBatch()
            }

          updateStatement.executeBatch()
        }

        relationshipsMigrated.addAndGet(relationshipsCount)
        println("total $type: $relationshipsCount")
      }

    }

  }

  suspend fun migrateBaseNodes() {

    val queue = ConcurrentLinkedQueue<BaseNodeCreate>()

    queue.offer(BaseNodeCreate("User", "admin.people"))
    queue.offer(BaseNodeCreate("User", "admin.users"))
    queue.offer(BaseNodeCreate("User", "sc.people"))

    queue.offer(BaseNodeCreate("Organization", "common.organizations"))
    queue.offer(BaseNodeCreate("Organization", "sc.organizations"))
    queue.offer(BaseNodeCreate("Partner", "sc.partners"))

    queue.offer(BaseNodeCreate("EthnologueLanguage", "sc.ethnologue"))
    queue.offer(BaseNodeCreate("Language", "sc.languages"))

    queue.offer(BaseNodeCreate("Location", "common.locations"))
    queue.offer(BaseNodeCreate("Location", "sc.locations"))

    queue.offer(BaseNodeCreate("File", "common.files"))
    queue.offer(BaseNodeCreate("FileVersion", "common.file_versions"))
    queue.offer(BaseNodeCreate("Directory", "common.directories"))

    queue.offer(BaseNodeCreate("TranslationProject", "sc.projects"))
    queue.offer(BaseNodeCreate("InternshipProject", "sc.projects"))
    queue.offer(BaseNodeCreate("LanguageEngagement", "sc.language_engagements"))
    queue.offer(BaseNodeCreate("InternshipEngagement", "sc.internship_engagements"))
    queue.offer(BaseNodeCreate("Budget", "sc.budgets"))
    queue.offer(BaseNodeCreate("Partnership", "sc.partnerships"))
    queue.offer(BaseNodeCreate("ProjectMember", "sc.project_members"))
    queue.offer(BaseNodeCreate("Product", "sc.products"))
    queue.offer(BaseNodeCreate("Education", "common.education_entries"))
    queue.offer(BaseNodeCreate("Unavailability", "sc.person_unavailabilities"))
    queue.offer(BaseNodeCreate("FundingAccount", "sc.funding_accounts"))
    queue.offer(BaseNodeCreate("BudgetRecord", "sc.budget_records"))
    queue.offer(BaseNodeCreate("Post", "sc.posts"))
    queue.offer(BaseNodeCreate("PeriodicReport", "sc.periodic_reports"))
    queue.offer(BaseNodeCreate("Ceremony", "sc.ceremonies"))
    queue.offer(BaseNodeCreate("FieldZone", "sc.field_zones"))
    queue.offer(BaseNodeCreate("FieldRegion", "sc.field_regions"))
    queue.offer(BaseNodeCreate("ScriptureRange", "common.scripture_references"))
    queue.offer(BaseNodeCreate("Film", "sc.products"))
    queue.offer(BaseNodeCreate("Story", "sc.products"))
    queue.offer(BaseNodeCreate("EthnoArt", "sc.products"))
    queue.offer(BaseNodeCreate("ProductProgress", "sc.product_progress"))
    queue.offer(BaseNodeCreate("StepProgress", "sc.step_progress"))

    val migrationStart = DateTime.now().millis

    val concurrency = 1

    coroutineScope {
      for (t in 1..concurrency) {
        launch {
          println("coroutine $t: start")
          delay(1)
          while (true) {
            val createRequest = queue.poll() ?: break
            println("coroutine $t, fetching ${createRequest.baseNode}")
            migrateBaseNodesByLabel(
              adminPersonId!!,
              adminGroupId!!,
              createRequest.baseNode,
              createRequest.tableName
            )
            delay(1)
          }
          println("coroutine $t: end")
        }
        delay(1)
      }
    }

    println("${baseNodesMigrated.get()} Base Nodes in ${(DateTime.now().millis - migrationStart) / 1000F} seconds")
  }

  suspend fun migrateBaseNodesByLabel(
    adminPersonId: String,
    adminGroupId: String,
    baseNode: String,
    tableName: String,
  ) {

    val batchSize = 2000

    ds.connection.use { conn ->

      val insertStmt: PreparedStatement = conn.prepareStatement(
        """
        insert into $tableName(id, created_by, modified_by, owning_person, owning_group) 
        values(common.uuid_generate_v5(common.uuid_ns_url(), ?), '$adminPersonId', '$adminPersonId', '$adminPersonId', '$adminGroupId');
      """.trimIndent()
      )

      neo4j.session().use { session ->

        val totalBaseNodes =
          session.run("MATCH (n:$baseNode) RETURN count(n) as count").single().get("count").asInt()

        for (i in 0..ceil((totalBaseNodes / batchSize).toDouble()).toInt()) {

          if (baseNode == "ScriptureRange") {
            session.run("MATCH (n:$baseNode) RETURN id(n) skip ${i * batchSize} limit $batchSize")
              .list()
              .forEach {
                insertStmt.setString(1, it.get("id(n)").asInt().toString())
                insertStmt.addBatch()
              }
          } else {
            session.run("MATCH (n:$baseNode) RETURN n.id skip ${i * batchSize} limit $batchSize")
              .list()
              .forEach {
                insertStmt.setString(1, it.get("n.id").asString())
                insertStmt.addBatch()
              }
          }

          insertStmt.executeBatch()
        }

        baseNodesMigrated.addAndGet(totalBaseNodes)
        println("total $baseNode: $totalBaseNodes")
      }
    }
  }

  suspend fun migrateBaseNodePairs() {
    val queue = ConcurrentLinkedQueue<BaseNodePairCreate>()

    queue.offer(
      BaseNodePairCreate(
        baseNode = "Organization",
        type = "locations",
        secondBaseNode = "Location",
        column = "organization",
        secondColumn = "location",
        tableName = "sc.organization_locations"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "Language",
        type = "locations",
        secondBaseNode = "Location",
        column = "language",
        secondColumn = "location",
        tableName = "sc.language_locations"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "Project",
        type = "primaryLocation",
        secondBaseNode = "Location",
        column = "project",
        secondColumn = "location",
        tableName = "sc.project_locations"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "User",
        type = "education",
        secondBaseNode = "Education",
        column = "person",
        secondColumn = "education",
        tableName = "common.education_by_person"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "User",
        type = "pinned",
        secondBaseNode = "Project",
        column = "person",
        secondColumn = "project",
        tableName = "sc.pinned_projects"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "DerivativeScriptureProduct",
        type = "scriptureReferencesOverride",
        secondBaseNode = "ScriptureRange",
        column = "product",
        secondColumn = "scripture_references_override",
        tableName = "sc.product_scripture_references"
      )
    )

    queue.offer(
      BaseNodePairCreate(
        baseNode = "Producible",
        type = "scriptureReferences",
        secondBaseNode = "ScriptureRange",
        column = "product",
        secondColumn = "scripture_reference",
        tableName = "sc.product_scripture_references"
      )
    )

    val migrationStart = DateTime.now().millis

    val concurrency = 1

    coroutineScope {
      for (t in 1..concurrency) {
        launch {
          println("coroutine $t: start")
          delay(1)
          while (true) {
            val createRequest = queue.poll() ?: break
            println("coroutine $t, fetching ${createRequest.baseNode}-${createRequest.secondBaseNode}")
            migrateBaseNodePairsByLabel(
              adminPersonId!!,
              adminGroupId!!,
              createRequest.baseNode,
              createRequest.secondBaseNode,
              createRequest.type,
              createRequest.column,
              createRequest.secondColumn,
              createRequest.tableName
            )
            delay(1)
          }
          println("coroutine $t: end")
        }
        delay(1)
      }
    }
    println("${relationshipsMigrated.get()} Base Nodes in ${(DateTime.now().millis - migrationStart) / 1000F} seconds")
  }

  suspend fun migrateBaseNodePairsByLabel(
    adminPersonId: String,
    adminGroupId: String,
    baseNode: String,
    secondBaseNode: String,
    type: String,
    column: String,
    secondColumn: String,
    tableName: String
  ) {

    val batchSize = 2000

    ds.connection.use { conn ->
      val insertStmt: PreparedStatement = conn.prepareStatement(
        """
          insert into $tableName($column, $secondColumn, created_by, modified_by, owning_person, owning_group)
          values(common.uuid_generate_v5(common.uuid_ns_url(), ?), 
                 common.uuid_generate_v5(common.uuid_ns_url(), ?),
                 '$adminPersonId', 
                 '$adminPersonId', 
                 '$adminPersonId', 
                 '$adminGroupId');
        """.trimIndent()
      )

      neo4j.session().use { session ->

        val baseNodePairsCount =
          session.run("MATCH (n:$baseNode)-[r:$type { active: true }]-(m:$secondBaseNode) RETURN count(r) as count").single()
            .get("count").asInt()

        for (i in 0..ceil((baseNodePairsCount / batchSize).toDouble()).toInt()) {
          val query = if (secondBaseNode == "ScriptureRange") {
            "MATCH (from:$baseNode)-[r:$type { active: true }]-(to:$secondBaseNode) RETURN from.id, id(to) skip ${i * batchSize} limit $batchSize"
          } else {
            "MATCH (from:$baseNode)-[r:$type ${if (type != "pinned") "{ active: true }" else ""}]-(to:$secondBaseNode) RETURN from.id, to.id skip ${i * batchSize} limit $batchSize"
          }

          session.run(query)
            .list()
            .forEach {
              insertStmt.setString(1, it.get("from.id").asString())
              if (secondBaseNode == "ScriptureRange") insertStmt.setString(2, it.get("id(to)").asInt().toString())
              else insertStmt.setString(2, it.get("to.id").asString())
              insertStmt.addBatch()
            }
          insertStmt.executeBatch()
        }

        relationshipsMigrated.addAndGet(baseNodePairsCount)
        println("total $baseNode - $secondBaseNode: $baseNodePairsCount")
      }
    }
  }

}
