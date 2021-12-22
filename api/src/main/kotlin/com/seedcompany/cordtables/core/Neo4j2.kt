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
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.sql.DataSource
import kotlin.math.ceil

data class Neo4jMigrationRequest2(
  val token: String? = null,
)

data class Neo4jMigrationResponse2(
  val error: ErrorType,
)

data class BaseNode2(
  val id: String,
  val labels: List<String>,
)

data class PropertyNode2(
  val type: String,
  val value: Any? = null,
)

data class BaseAndPropertyNode2(
  val baseNode: BaseNode,
  val propNode: PropertyNode
)

data class Relation2(
  val type: String,
  val active: String
)

data class BaseNodeCreate(
  val baseNode: String,
  val tableName: String,
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

  @PostMapping("migrate/neo4j2")
  @ResponseBody
  suspend fun createHandler(@RequestBody req: Neo4jMigrationRequest): Neo4jMigrationResponse {

    migrateBaseNodes()

    return Neo4jMigrationResponse(ErrorType.NoError)
  }

  suspend fun migrateBaseNodes() {


    val adminPersonId = jdbcTemplate.queryForObject(
      """
      select id from admin.people order by created_at asc offset 0 limit 1;
    """.trimIndent(),
      String::class.java
    )

    val adminGroupId = jdbcTemplate.queryForObject(
      """
      select id from admin.groups order by created_at asc offset 0 limit 1;
    """.trimIndent(),
      String::class.java
    )

    if (adminPersonId != null && adminGroupId != null) {

      val queue = ConcurrentLinkedQueue<BaseNodeCreate>()

      queue.offer(BaseNodeCreate("File", "common.files"))
      queue.offer(BaseNodeCreate("FileVersion", "common.file_versions"))
      queue.offer(BaseNodeCreate("Directory", "common.directories"))
      queue.offer(BaseNodeCreate("TranslationProject", "sc.projects"))
      queue.offer(BaseNodeCreate("InternshipProject", "sc.projects"))
      queue.offer(BaseNodeCreate("LanguageEngagement", "sc.language_engagements"))
      queue.offer(BaseNodeCreate("InternshipEngagement", "sc.internship_engagements"))
      queue.offer(BaseNodeCreate("Language", "common.languages"))
      queue.offer(BaseNodeCreate("Budget", "sc.budgets"))
      // sc languages
      queue.offer(BaseNodeCreate("EthnologueLanguage", "sil.table_of_languages"))
      queue.offer(BaseNodeCreate("Location", "common.locations"))
      queue.offer(BaseNodeCreate("Organization", "common.organizations"))
      // sc orgs
      queue.offer(BaseNodeCreate("Partner", "sc.partnerships"))
      queue.offer(BaseNodeCreate("User", "admin.people"))
      // sc people
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

//      queue.offer(BaseNodeCreate( "ScriptureRange", "common.scripture_references"))
//      queue.offer(BaseNodeCreate( "Film", "sc.films")
//      queue.offer(BaseNodeCreate( "Story", "sc.stories")
//      queue.offer(BaseNodeCreate( "EthnoArt", "sc.ethno_arts")

      val migrationStart = DateTime.now().millis

      val concurrency = 1

      coroutineScope {
        for (t in 1..concurrency) {
          launch {
            println("starting coroutine $t")
            while (true) {
              val createRequest = queue.poll() ?: break
              println("$t, starting ${createRequest.baseNode}")
              getFromNeo4jWriteToPostgres(
                adminPersonId,
                adminGroupId,
                createRequest.baseNode,
                createRequest.tableName
              )
              delay(1)
            }
            println("end of coroutine $t")
          }
        }
      }

      val migrationStop = DateTime.now().millis

      println("time: ${(migrationStop - migrationStart) / 1000F}")
    }

  }

  suspend fun getFromNeo4jWriteToPostgres(
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
        values(?::uuid, '$adminPersonId', '$adminPersonId', '$adminPersonId', '$adminGroupId');
      """.trimIndent()
      )

      neo4j.session().use { session ->

        val totalBaseNodes =
          session.run("MATCH (n:$baseNode) RETURN count(n) as count").single().get("count").asInt()


        for (i in 0..ceil((totalBaseNodes / batchSize).toDouble()).toInt()) {

          session.run("MATCH (n:$baseNode) RETURN n.id skip ${i * batchSize} limit $batchSize")
            .list()
            .forEach {

              // convert id from neo4j into uuid
              insertStmt.setString(
                1,
                UUID
                  .nameUUIDFromBytes(
                    it.get("n.id")
                      .asString()
                      .encodeToByteArray()
                  ).toString()
              )
              insertStmt.addBatch()
            }

          insertStmt.executeBatch()

        }

        println("total $baseNode: $totalBaseNodes")

      }

    }


  }

}
