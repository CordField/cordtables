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
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource
import kotlin.concurrent.thread

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

    var neo4jIdQueue = ConcurrentLinkedQueue<BaseNode>()

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

        println("starting base node migration")

        var totalBaseNodes = 0
        neo4j.session().use { session ->
            totalBaseNodes =
                session.run("MATCH (n:BaseNode) RETURN count(n) as count").single().get("count").asInt()

            println("count of base nodes is $totalBaseNodes")
        }

        if (totalBaseNodes == null) {
            println("failed to count base nodes")
            return
        }

        var fetchedNodes = 0
        val batchSize = 1000
        val maxQueueSize = 5000

        coroutineScope {

            // bring neo4j ids into queue as needed until no more left
            launch {
                println("starting reader")

                while (fetchedNodes < totalBaseNodes) {
                    if (neo4jIdQueue.size < maxQueueSize) {
                        if (totalBaseNodes - fetchedNodes > batchSize) {
//                            println("fetched nodes: $fetchedNodes")
                            getNeo4jIds(fetchedNodes, batchSize)
                            fetchedNodes += batchSize
//                            println("queue size is ${neo4jIdQueue.size}")
                        } else {
                            getNeo4jIds(fetchedNodes, totalBaseNodes - fetchedNodes)
                            fetchedNodes += totalBaseNodes - fetchedNodes
                        }
                    } else {
                        delay(1000L)
                    }
                    delay(1L)
                }
                println("reader closing")
            }

            // feed nodes into postgres until gone
            val processedNodes = AtomicInteger(0)

//            val then = DateTime.now().millis

            launch {
                println("starting writer 1")
                while (neo4jIdQueue.size > 0) {
                    val node = neo4jIdQueue.remove() ?: return@launch
                    createBaseNodeIdempotent(node)
                    processedNodes.incrementAndGet()
                    delay(1L) // I don't know why this is needed, but it is.
                }
                println("writer 1 closing")
            }

            launch {
                println("starting writer 2")
                while (neo4jIdQueue.size > 0) {
                    val node = neo4jIdQueue.remove() ?: return@launch
                    createBaseNodeIdempotent(node)
                    processedNodes.incrementAndGet()
                    delay(1L) // I don't know why this is needed, but it is.
                }
                println("writer 2 closing")
            }
//
//            launch {
//                println("starting writer 3")
//                while (neo4jIdQueue.size > 0) {
//                    val node = neo4jIdQueue.remove() ?: return@launch
//                    createBaseNodeIdempotent(node)
//                    processedNodes.incrementAndGet()
//                    delay(1L) // I don't know why this is needed, but it is.
//                }
//                println("writer 3 closing")
//            }
//
//            launch {
//                println("starting writer 4")
//                while (neo4jIdQueue.size > 0) {
//                    val node = neo4jIdQueue.remove() ?: return@launch
//                    createBaseNodeIdempotent(node)
//                    processedNodes.incrementAndGet()
//                    delay(1L) // I don't know why this is needed, but it is.
//                }
//                println("writer 4 closing")
//            }

            // progress
            launch {
                val then = DateTime.now().millis

                while (processedNodes.toInt() < totalBaseNodes) {
                    val lapse = DateTime.now().millis - then

                    var rate =
                        if (processedNodes.get() == 0) 0F else processedNodes.get().toFloat() / lapse.toFloat() * 1000F

                    val remainingNodes = totalBaseNodes.toFloat() - processedNodes.get().toFloat()
                    val eta = remainingNodes / rate / 60F

                    print("\r${processedNodes}/$totalBaseNodes rate: ${rate.toInt()} records/sec eta: ${eta.toInt()}m queue: ${neo4jIdQueue.size}")

                    delay(1000L)
                }
            }

        }

        println("base node migration done")
    }

    suspend fun getNeo4jIds(skip: Int, size: Int) {
        neo4j.session().use { session ->
            session.run("MATCH (n:BaseNode) RETURN n skip $skip limit $size")
                .list()
                .forEach {
                    neo4jIdQueue.add(
                        BaseNode(
                            id = it.get("n").asNode().get("id").asString(),
                            labels = it.get("n").asNode().labels() as List<String>
                        )
                    )
                }
        }
    }

    suspend fun createBaseNodeIdempotent(node: BaseNode) {
        when {
            node.labels.contains("File") -> {
                writeBaseNode(
                    targetTable = "sc.files",
                    id = node.id
                )
                writeBaseNode(
                    targetTable = "common.files",
                    id = node.id,
                )
            }
            node.labels.contains("FileVersion") -> {
                writeBaseNode(
                    targetTable = "sc.file_versions",
                    id = node.id,
                )
                writeBaseNode(
                    targetTable = "common.file_versions",
                    id = node.id,
                )
            }
            node.labels.contains("Organization") -> writeBaseNode(
                targetTable = "sc.organizations",
                id = node.id,
                commonTable = "common.organizations",
            )
            node.labels.contains("TranslationProject") -> writeBaseNode(
                targetTable = "sc.projects",
                id = node.id,
            )
            node.labels.contains("InternshipProject") -> writeBaseNode(
                targetTable = "sc.projects",
                id = node.id,
            )
            node.labels.contains("User") -> writeUserNode(
                personTable = "admin.people",
                id = node.id,
                userTable = "admin.users",
                commonTable = "sc.people"
            )
            node.labels.contains("ProjectMember") -> writeBaseNode(
                targetTable = "sc.project_members",
                id = node.id,
            )
            node.labels.contains("FieldZone") -> writeBaseNode(
                targetTable = "sc.field_zone",
                id = node.id,
            )
            node.labels.contains("FieldRegion") -> writeBaseNode(
                targetTable = "sc.field_regions",
                id = node.id,
            )
            node.labels.contains("Budget") -> writeBaseNode(
                targetTable = "sc.budgets",
                id = node.id,
            )
            node.labels.contains("EthnologueLanguage") -> writeBaseNode(
                targetTable = "sil.table_of_languages",
                id = node.id,
            )
            node.labels.contains("Language") -> writeBaseNode(
                targetTable = "sc.languages",
                id = node.id,
            )
            node.labels.contains("LanguageEngagement") -> writeBaseNode(
                targetTable = "sc.language_engagements",
                id = node.id,
            )
            node.labels.contains("InternshipEngagement") -> writeBaseNode(
                targetTable = "sc.internship_engagements",
                id = node.id,
            )
            node.labels.contains("Ceremony") -> writeBaseNode(
                targetTable = "sc.ceremonies",
                id = node.id,
            )
            node.labels.contains("PeriodicReport") -> writeBaseNode(
                targetTable = "sc.periodic_reports",
                id = node.id,
            )
            node.labels.contains("Directory") -> writeBaseNode(
                targetTable = "common.directories",
                id = node.id,
            )
            node.labels.contains("Partner") -> writeBaseNode(
                targetTable = "sc.partners",
                id = node.id,
            )
            node.labels.contains("Partnership") -> writeBaseNode(
                targetTable = "sc.partnerships",
                id = node.id,
            )
            node.labels.contains("BudgetRecord") -> writeBaseNode(
                targetTable = "sc.budget_records",
                id = node.id,
            )
            node.labels.contains("Location") -> writeBaseNode(
                targetTable = "sc.locations",
                id = node.id,
                commonTable = "common.locations"
            )
            node.labels.contains("FundingAccount") -> writeBaseNode(
                targetTable = "sc.funding_account",
                id = node.id
            )
            else -> {
            }//println("didn't process: ${node.labels}")
        }
    }

    suspend fun writeUserNode(personTable: String, id: String, userTable: String? = null, commonTable: String?) {
        val exists = jdbcTemplate.queryForObject(
            "select exists(select id from $personTable where neo4j_id = ?);",
            Boolean::class.java,
            id
        )

        if (exists) {
//            println("node $id exists")
        } else {
//            println("creating $personTable node $id")

            val personId = jdbcTemplate.queryForObject(
                "insert into $personTable(neo4j_id, created_by, modified_by, owning_person, owning_group) values('$id', 1, 1, 1, 1) returning id;",
                Int::class.java
            )

            jdbcTemplate.update(
                "insert into $commonTable (id, neo4j_id, created_by, modified_by, owning_person, owning_group) values(?, ?, 1, 1, 1, 1);",
                personId,
                id
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
//            println("node $id exists")
        } else {
//            println("creating $targetTable node $id")

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

        var totalBaseNodeRels = 0
        neo4j.session().use { session ->
            totalBaseNodeRels =
                session.run("MATCH (n:BaseNode)-[r]-(m:BaseNode) RETURN count(r) as count").single().get("count")
                    .asInt()

            println("count of base nodes relationships is $totalBaseNodeRels")
        }

        if (totalBaseNodeRels == null) {
            println("failed to count base nodes")
            return
        }

        var fetchedNodes = 0
        val batchSize = 1000
        val maxQueueSize = 5000

        coroutineScope {
            launch {
                println("starting BaseNode relationships reader")

                while (fetchedNodes < totalBaseNodeRels) {
                    if (nodePairs.size < maxQueueSize) {
                        if (totalBaseNodeRels - fetchedNodes > batchSize) {
                            getNodePairs(fetchedNodes, batchSize)
                            fetchedNodes += batchSize
                        } else {
                            getNodePairs(fetchedNodes, totalBaseNodeRels - fetchedNodes)
                            fetchedNodes += totalBaseNodeRels - fetchedNodes
                        }
                    } else {
                        delay(1000L)
                    }
                    delay(10L)
                }
                println("\nBaseNode relationships reader closing")
            }

            val processedBaseNodeRels = AtomicInteger(0)

            launch {
                println("starting writer 1")
                while (nodePairs.size > 0) {
                    val (n, m) = nodePairs.remove()
                    createRelationship(n, m)
                    processedBaseNodeRels.incrementAndGet()
                    delay(1L)
                }
                println("writer 1 closing")
            }

            launch {
                println("starting writer 2")
                while (nodePairs.size > 0) {
                    val (n, m) = nodePairs.remove()
                    createRelationship(n, m)
                    processedBaseNodeRels.incrementAndGet()
                    delay(1L)
                }
                println("writer 2 closing")
            }

            launch {
                val then = DateTime.now().millis

                while (processedBaseNodeRels.toInt() < totalBaseNodeRels) {
                    val lapse = DateTime.now().millis - then

                    val rate =
                        if (processedBaseNodeRels.get() == 0) 0F else processedBaseNodeRels.get()
                            .toFloat() / lapse.toFloat() * 1000F

                    val remainingNodes = totalBaseNodeRels.toFloat() - processedBaseNodeRels.get().toFloat()
                    val eta = remainingNodes / rate / 60F

                    print("\r${processedBaseNodeRels}/$totalBaseNodeRels rate: ${rate.toInt()} records/sec eta: ${eta.toInt()}r queue: ${nodePairs.size}")
                    delay(1000L)
                }
            }


        }

        println("base node to base node relationships migration done")
    }
    suspend fun getNodePairs(skip: Int, size: Int) {
        neo4j.session().use { session ->
            session.run("MATCH (n:BaseNode)-[]-(m:BaseNode) RETURN n, m SKIP $skip LIMIT $size")
                .list()
                .forEach {
                    nodePairs.add(
                        arrayOf(
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
        return (n.labels.contains(n_label) && m.labels.contains(m_label))
    }

    suspend fun createRelationship(n: BaseNode, m: BaseNode) {
        when {
            checkRelationship(n, m, "User", "ProjectMember") -> writeRelationships(
                n,
                m,
                "sc.project_members",
                "sc.people",
                "person",
                "id"
            )
            checkRelationship(n, m, "User", "FieldRegion") -> writeRelationships(
                n,
                m,
                "sc.field_regions",
                "admin.people",
                "director",
                "id"
            )
            checkRelationship(n, m, "User", "FieldZone") -> writeRelationships(
                n,
                m,
                "sc.field_zone",
                "admin.people",
                "director",
                "id"
            )
            checkRelationship(n, m, "Budget", "File") -> writeRelationships(
                m,
                n,
                "sc.budgets",
                "common.files",
                "universal_template",
                "id"
            )
            checkRelationship(n, m, "Budget", "BudgetRecord") -> writeRelationships(
                n,
                m,
                "sc.budget_records",
                "sc.budgets",
                "budget",
                "id"
            )
            checkRelationship(n, m, "Organization", "BudgetRecord") -> writeRelationships(
                n,
                m,
                "sc.budget_records",
                "sc.organizations",
                "organization",
                "id"
            )
            checkRelationship(n, m, "Project", "Budget") -> writeRelationships(
                n,
                m,
                "sc.budgets",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, m, "Project", "ProjectMember") -> writeRelationships(
                n,
                m,
                "sc.project_members",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, m, "Project", "Partnership") -> writeRelationships(
                n,
                m,
                "sc.partnerships",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, m, "BaseFile", "PeriodicReport") -> writeRelationships(
                n,
                m,
                "sc.periodic_reports",
                "common.files",
                "reportFile",
                "id"
            )
            checkRelationship(n, m, "FieldRegion", "Project") -> writeRelationships(
                n,
                m,
                "sc.projects",
                "sc.field_regions",
                "field_region",
                "id"
            )
            checkRelationship(n, m, "Directory", "Project") -> writeRelationships(
                n,
                m,
                "sc.projects",
                "common.directories",
                "root_directory",
                "id"
            )
            checkRelationship(n, m, "EthnologueLanguage", "Language") -> writeRelationships(
                n,
                m,
                "sc.languages",
                "sil.table_of_languages",
                "ethnologue",
                "id"
            )
            checkRelationship(n, m, "Project", "LanguageEngagement") -> writeRelationships(
                n,
                m,
                "sc.language_engagements",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, m, "Language", "LanguageEngagement") -> writeRelationships(
                n,
                m,
                "sc.language_engagements",
                "sc.languages",
                "ethnologue",
                "ethnologue"
            )
            checkRelationship(n, m, "Project", "InternshipEngagement") -> writeRelationships(
                n,
                m,
                "sc.internship_engagements",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, m, "Organization", "Partner") -> writeRelationships(
                n,
                m,
                "sc.partners",
                "sc.organizations",
                "organization",
                "id"
            )
            checkRelationship(n, m, "Partner", "Partnership") -> writeRelationships(
                n,
                m,
                "sc.partnerships",
                "sc.partners",
                "partner",
                "organization"
            )
            checkRelationship(n, m, "InternshipEngagement", "Ceremony") -> writeRelationships(
                n,
                m,
                "sc.ceremonies",
                "sc.internship_engagements",
                "project",
                "project"
            )
            checkRelationship(n, m, "LanguageEngagement", "Ceremony") -> {
                writeRelationships(n, m, "sc.ceremonies", "sc.language_engagements", "project", "project")
                writeRelationships(n, m, "sc.ceremonies", "sc.language_engagements", "ethnologue", "ethnologue")
            }
            checkRelationship(n, m, "FundingAccount", "Location") -> writeRelationships(
                n,
                m,
                "sc.locations",
                "sc.funding_account",
                "funding_account",
                "id"
            )
            checkRelationship(n, m, "FieldRegion", "Location") -> writeRelationships(
                n,
                m,
                "sc.locations",
                "sc.field_regions",
                "default_region",
                "id"
            )
        }

    }

    suspend fun writeRelationships(
        n: BaseNode,
        m: BaseNode,
        targetTable: String,
        refTable: String,
        field: String,
        foreignKey: String
    ) {
        val exists = jdbcTemplate.queryForObject(
            "select exists(select ? from $refTable where neo4j_id = ?);",
            Boolean::class.java,
            foreignKey,
            n.id
        )

        if (exists) {
            val id = jdbcTemplate.queryForObject(
                "select $foreignKey from $refTable where neo4j_id = ?;",
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