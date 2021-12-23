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
import java.sql.PreparedStatement
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource
import kotlin.math.ceil

data class BaseNodeCreate(
  val baseNode: String,
  val tableName: String,
)

data class RelationshipCreate(
  val fromBaseNode: String,
  val toBaseNode: String,
  val type: String,
  val table: String,
  val column: String,
  val baseNodeToPlaceInColumn: String,
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
//      migrateBaseNodes()
      migrateRelationships()
    }

    return Neo4jMigrationResponse(ErrorType.NoError)
  }

  suspend fun migrateRelationships() {
    val queue = ConcurrentLinkedQueue<RelationshipCreate>()

//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "User",
//        type = "user",
//        toBaseNode = "ProjectMember",
//        table = "sc.project_members",
//        column = "person",
//        baseNodeToPlaceInColumn = "User",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "User",
//        type = "director",
//        toBaseNode = "FieldRegion",
//        table = "sc.field_regions",
//        column = "director",
//        baseNodeToPlaceInColumn = "User",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "User",
//        type = "director",
//        toBaseNode = "FieldZone",
//        table = "sc.field_zones",
//        column = "director",
//        baseNodeToPlaceInColumn = "User",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Budget",
//        type = "universalTemplateFileNode",
//        toBaseNode = "File",
//        table = "sc.budgets",
//        column = "universal_template",
//        baseNodeToPlaceInColumn = "File",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Budget",
//        type = "record",
//        toBaseNode = "BudgetRecord",
//        table = "sc.budget_records",
//        column = "budget",
//        baseNodeToPlaceInColumn = "Budget",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Organization",
//        type = "organization",
//        toBaseNode = "BudgetRecord",
//        table = "sc.budget_records",
//        column = "organization",
//        baseNodeToPlaceInColumn = "Organization",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Project",
//        type = "budget",
//        toBaseNode = "Budget",
//        table = "sc.budgets",
//        column = "project",
//        baseNodeToPlaceInColumn = "Project",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Project",
//        type = "member",
//        toBaseNode = "ProjectMember",
//        table = "sc.project_members",
//        column = "project",
//        baseNodeToPlaceInColumn = "Project",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Project",
//        type = "partnership",
//        toBaseNode = "Partnership",
//        table = "sc.partnerships",
//        column = "project",
//        baseNodeToPlaceInColumn = "Project",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "BaseFile",
//        type = "reportFileNode",
//        toBaseNode = "PeriodicReport",
//        table = "sc.periodic_reports",
//        column = "report_file",
//        baseNodeToPlaceInColumn = "BaseFile",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "FieldRegion",
//        type = "fieldRegion",
//        toBaseNode = "Project",
//        table = "sc.projects",
//        column = "field_region",
//        baseNodeToPlaceInColumn = "FieldRegion",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Directory",
//        type = "rootDirectory",
//        toBaseNode = "Project",
//        table = "sc.projects",
//        column = "root_directory",
//        baseNodeToPlaceInColumn = "Directory",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "EthnologueLanguage",
//        type = "ethnologue",
//        toBaseNode = "Language",
//        table = "sc.languages",
//        column = "ethnologue",
//        baseNodeToPlaceInColumn = "EthnologueLanguage",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Project",
//        type = "engagement",
//        toBaseNode = "LanguageEngagement",
//        table = "sc.language_engagements",
//        column = "project",
//        baseNodeToPlaceInColumn = "Project",
//      )
//    )
//
//    queue.offer(
//      RelationshipCreate(
//        fromBaseNode = "Language",
//        type = "language",
//        toBaseNode = "LanguageEngagement",
//        table = "sc.language_engagements",
//        column = "language",
//        baseNodeToPlaceInColumn = "Language",
//      )
//    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Project",
        type = "engagement",
        toBaseNode = "InternshipEngagement",
        table = "sc.projects",
        column = "project",
        baseNodeToPlaceInColumn = "Project",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "Organization",
        type = "organization",
        toBaseNode = "Partner",
        table = "sc.partners",
        column = "organization",
        baseNodeToPlaceInColumn = "Organization",
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
        column = "project",
        baseNodeToPlaceInColumn = "InternshipEngagement",
      )
    )

    queue.offer(
      RelationshipCreate(
        fromBaseNode = "LanguageEngagement",
        type = "ceremony",
        toBaseNode = "Ceremony",
        table = "sc.ceremonies",
        column = "project",
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
        update $tableName set $column = public.uuid_generate_v5(public.uuid_ns_url(), ?) where id = public.uuid_generate_v5(public.uuid_ns_url(), ?);
      """.trimIndent()
      )

      neo4j.session().use { session ->

        val relationshipsCount =
          session.run("MATCH (n:$fromBaseNode)-[r:$type]-(m:$toBaseNode) RETURN count(r) as count").single()
            .get("count").asInt()

        for (i in 0..ceil((relationshipsCount / batchSize).toDouble()).toInt()) {

          session.run("MATCH (from:$fromBaseNode)-[r:$type]-(to:$toBaseNode) RETURN from.id, to.id skip ${i * batchSize} limit $batchSize")
            .list()
            .forEach {
//              println("$fromBaseNode ${it.get("from.id").asString()} $toBaseNode ${it.get("to.id").asString()}")
              // convert id from neo4j into uuid
              if (baseNodeToPlaceInColumn == toBaseNode){
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
    queue.offer(BaseNodeCreate("Organization", "sc.partners"))

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
    queue.offer(BaseNodeCreate("Partner", "sc.partnerships"))
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
        values(public.uuid_generate_v5(public.uuid_ns_url(), ?), '$adminPersonId', '$adminPersonId', '$adminPersonId', '$adminGroupId');
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

}
