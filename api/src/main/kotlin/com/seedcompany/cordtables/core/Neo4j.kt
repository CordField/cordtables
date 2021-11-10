package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.Create
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource

data class Neo4jMigrationRequest(
    val token: String? = null,
)

data class Neo4jMigrationResponse(
    val error: ErrorType,
)

data class BaseNode(
    val id: String,
    val labels: List<String>,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("Neo4jMigration")
class Neo4j(
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

    var baseNodeCounter = AtomicInteger(0)

    var nodePairs = ConcurrentLinkedQueue<Array<BaseNode>>()

    @PostMapping("migrate/neo4j")
    @ResponseBody
    suspend fun createHandler(@RequestBody req: Neo4jMigrationRequest): Neo4jMigrationResponse {

        migrateBaseNodes()
        migrateBaseNodeToBaseNodeRelationships()
        migrateBaseNodeProperties()

        return Neo4jMigrationResponse(ErrorType.NoError)
    }

    suspend fun migrateBaseNodes() {

        neo4j.session().use { session ->
            baseNodeCounter.set(
                session.run("MATCH (n:BaseNode) RETURN count(n) as count").single().get("count").asInt()
            )

            println("count of base nodes is $baseNodeCounter")
        }

        if (baseNodeCounter == null) {
            println("failed to count base nodes")
            return
        }

        val ticker = AtomicInteger(0)

        for (i in 0 until baseNodeCounter.get()) {
            println("loop $i")
            createBaseNodeIdempotent(i)
        }

    }

    suspend fun createBaseNodeIdempotent(skip: Int) {
        var node: BaseNode? = null

//        val nextNode = baseNodeCounter.decrementAndGet()

//        if (nextNode <= 0) return

        neo4j.session().use { session ->
            val baseNodeReturn =
                session.run("MATCH (m:BaseNode) RETURN m skip $skip limit 1")
                    .single()

            node = BaseNode(
                id = baseNodeReturn.get("m").asNode().get("id").asString(),
                labels = baseNodeReturn.get("m").asNode().labels() as List<String>
            )
        }

        if (node == null) return

        when {
            node!!.labels.contains("Organization") -> writeBaseNode(
                targetTable = "sc.organizations",
                id = node!!.id,
                commonTable = "common.organizations",
            )

            node!!.labels.contains("TranslationProject") -> writeBaseNode(
                targetTable = "sc.projects",
                id = node!!.id,
            )
            node!!.labels.contains("InternshipProject") -> writeBaseNode(
                targetTable = "sc.projects",
                id = node!!.id,
            )
            node!!.labels.contains("User") -> writeUserNode(
                personTable = "admin.people",
                id = node!!.id,
                userTable = "admin.users",
            )
            node!!.labels.contains("ProjectMember") -> writeBaseNode(
                targetTable = "sc.project_members",
                id = node!!.id,
            )
            node!!.labels.contains("FieldZone") -> writeBaseNode(
                targetTable = "sc.field_zone",
                id = node!!.id,
            )
            node!!.labels.contains("FieldRegion") -> writeBaseNode(
                targetTable = "sc.field_regions",
                id = node!!.id,
            )
            node!!.labels.contains("Budget") -> writeBaseNode(
                targetTable = "sc.budgets",
                id = node!!.id,
            )
            node!!.labels.contains("EthnologueLanguage") -> writeBaseNode(
                targetTable = "sil.table_of_languages",
                id = node!!.id,
            )
            node!!.labels.contains("Language") -> writeBaseNode(
                targetTable = "sc.languages",
                id = node!!.id,
            )
            node!!.labels.contains("LanguageEngagement") -> writeBaseNode(
                targetTable = "sc.language_engagements",
                id = node!!.id,
            )
            node!!.labels.contains("InternshipEngagement") -> writeBaseNode(
                targetTable = "sc.internship_engagements",
                id = node!!.id,
            )
            node!!.labels.contains("Ceremony") -> writeBaseNode(
                targetTable = "sc.ceremonies",
                id = node!!.id,
            )
            node!!.labels.contains("PeriodicReport") -> writeBaseNode(
                targetTable = "sc.periodic_reports",
                id = node!!.id,
            )
            node!!.labels.contains("Directory") -> writeBaseNode(
                targetTable = "common.directories",
                id = node!!.id,
            )
            node!!.labels.contains("BaseFile") -> writeBaseNode(
                targetTable = "common.files",
                id = node!!.id,
            )
            node!!.labels.contains("Partner") -> writeBaseNode(
                targetTable = "sc.partners",
                id = node!!.id,
            )
            node!!.labels.contains("Partnership") -> writeBaseNode(
                targetTable = "sc.partnerships",
                id = node!!.id,
            )
            node!!.labels.contains("BudgetRecord") -> writeBaseNode(
                targetTable = "sc.budget_records",
                id = node!!.id,
            )
            else -> println(node!!.labels)

        }
    }

    suspend fun writeUserNode(personTable: String, id: String, userTable: String? = null) {
        val exists = jdbcTemplate.queryForObject(
            "select exists(select id from $personTable where neo4j_id = ?);",
            Boolean::class.java,
            id
        )

        if (exists) {
            println("node $id exists")
        } else {
            println("creating $personTable node $id")

            val personId = jdbcTemplate.queryForObject(
                "insert into $personTable(neo4j_id, created_by, modified_by, owning_person, owning_group) values('$id', 1, 1, 1, 1) returning id;",
                Int::class.java
            )

            jdbcTemplate.update(
                "insert into $userTable(person, created_by, modified_by, owning_person, owning_group) values(?, 1, 1, 1, 1);",
                personId,
            )
        }
    }

    suspend fun writeBaseNode(targetTable: String, id: String, commonTable: String? = null) {
        val exists = jdbcTemplate.queryForObject(
            "select exists(select id from $targetTable where neo4j_id = ?);",
            Boolean::class.java,
            id
        )

        if (exists) {
            println("node $id exists")
        } else {
            println("creating $targetTable node $id")

            if (commonTable != null) {
                val commonId = jdbcTemplate.queryForObject(
                    "insert into $commonTable(created_by, modified_by, owning_person, owning_group) values(1, 1, 1, 1) returning id;",
                    Int::class.java
                )

                jdbcTemplate.update(
                    "insert into $targetTable(id, neo4j_id, created_by, modified_by, owning_person, owning_group) values(?, ?, 1, 1, 1, 1);",
                    commonId, id,
                )
            } else {
                jdbcTemplate.update(
                    "insert into $targetTable(neo4j_id, created_by, modified_by, owning_person, owning_group) values(?, 1, 1, 1, 1);",
                    id,
                )

            }
        }
    }

    suspend fun migrateBaseNodeToBaseNodeRelationships() {
        getNodePairs()

        while (nodePairs.size > 0) {
            val (n, m) = nodePairs.remove()
            when {
                //checkRelationships(n, m, "Budget", "File") -> writeRelationships(n, m, "sc.budgets", "common.files", "universal_template" )
                //checkRelationships(n, m,"User","ProjectMember") -> writeRelationships(n, m, "sc.project_members", "admin.people", "person")
                checkRelationship(n, m,"FieldRegion", "User") -> writeRelationships(n, m, "sc.field_regions", "admin.people", "director")
                checkRelationship(n, m, "FieldZone", "User") -> writeRelationships(n, m, "sc.field_zone", "admin.people", "director")

                checkRelationship(n, m, "BudgetRecord", "Budget") -> writeRelationships(n, m, "sc.budget_records", "sc.budgets", "budget")

                //checkRelationships(n, m, "BudgetRecord", "Organization") -> writeRelationships(n, m , "sc.budget_records", "sc.organizations", "partnership")

                checkRelationship(n, m, "Budget", "Project") -> writeRelationships(n, m, "sc.budgets", "sc.projects", "project")
                checkRelationship(n, m, "PeriodicReport", "BaseFile") -> writeRelationships(n, m, "sc.periodic_reports", "common.files", "reportFile")

                checkRelationship(n, m, "Project", "FieldRegion") -> writeRelationships(n, m, "sc.projects", "sc.field_regions", "field_region")
                checkRelationship(n, m, "Project", "Directory") -> writeRelationships(n, m, "sc.projects", "common.directories", "root_directory")
                checkRelationship(n, m, "ProjectMember", "Project") -> writeRelationships(n, m, "sc.project_members", "sc.projects", "project")

                checkRelationship(n, m, "Language", "EthnologueLanguage") -> writeRelationships(n, m, "sc.languages", "sil.table_of_languages", "ethnologue")
                checkRelationship(n, m, "LanguageEngagement", "Project") -> writeRelationships(n, m, "sc.language_engagements", "sc.projects", "project")
                checkRelationship(n, m, "LanguageEngagement", "Language") -> writeRelationships(n, m, "sc.language_engagements", "sc.languages", "ethnologue")

                checkRelationship(n, m, "InternshipEngagement", "Project") -> writeRelationships(n, m, "sc.internship_engagements", "sc.projects", "project")

                checkRelationship(n, m, "Partner", "Organization") -> writeRelationships(n, m, "sc.partners", "sc.organizations", "organization")

                checkRelationship(n, m, "Partnership", "Partner") -> writeRelationships(n, m, "sc.partnerships", "sc.partners", "partner")
                checkRelationship(n, m, "Partnership", "Project") -> writeRelationships(n, m, "sc.partnerships", "sc.projects", "project")

                checkRelationship(n, m, "Ceremony", "LanguageEngagement") -> writeRelationships(n, m, "sc.ceremonies", "sc.language_engagements", "ethnologue")
                checkRelationship(n, m, "Ceremony", "LanguageEngagement") -> writeRelationships(n, m, "sc.ceremonies", "sc.language_engagements", "project")
                checkRelationship(n, m, "Ceremony", "InternshipEngagement") -> writeRelationships(n, m, "sc.ceremonies", "sc.internship_engagements", "project")
            }
        }
    }

    suspend fun getNodePairs() {
        neo4j.session().use { session ->
            session.run("MATCH (n:BaseNode)-[]-(m:BaseNode) RETURN n, m")
                .list()
                .forEach {
                    nodePairs.add(
                        arrayOf<BaseNode>(
                            BaseNode(
                                id = it.get("n").asNode().get("id").asString(),
                                labels = it.get("n").asNode().labels() as List<String>
                            ),
                            BaseNode(
                                id = it.get("m").asNode().get("id").asString(),
                                labels = it.get("m").asNode().labels() as List<String>
                            )
                        )
                    )
                }
        }
    }

    suspend fun checkRelationship(n: BaseNode, m: BaseNode, n_label: String, m_label: String): Boolean {
        return (n.labels.contains(n_label) && m.labels.contains(m_label)) ||
                (n.labels.contains(m_label) && m.labels.contains(n_label))
    }

    suspend fun writeRelationships(n: BaseNode, m: BaseNode, targetTable: String, refTable: String, field: String) {
        val queryField = when {
            checkRelationship(n, m, "Partnership", "Partner") -> "organization"
            checkRelationship(n, m, "Language", "LanguageEngagement") -> "ethnologue"
            checkRelationship(n, m, "Ceremony", "Engagement") -> field
             else -> "id"
        }

        val exists = jdbcTemplate.queryForObject("select exists(select ? from $refTable where neo4j_id = ?);",
            Boolean::class.java,
            queryField,
            n.id
        )

        if (exists) {
            val id = jdbcTemplate.queryForObject(
            "select $queryField from $refTable where neo4j_id = ?;",
            Int::class.java,
            n.id
            )

            jdbcTemplate.update(
            "update $targetTable set $field = ? where neo4j_id = ?;",
            id,
            m.id,
            )
        }
    }

    suspend fun migrateBaseNodeProperties() {

    }
}