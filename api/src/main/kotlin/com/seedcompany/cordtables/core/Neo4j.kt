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
import kotlin.math.roundToInt

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
data class PropertyNode(
    val type: String,
    val value: Any? = null,
)
data class BaseAndPropertyNode(
    val baseNode: BaseNode,
    val propNode: PropertyNode
)

data class Relation(
    val type: String,
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
    var nodeAndPropertyQueue = ConcurrentLinkedQueue<BaseAndPropertyNode>()
    var errorOutput = mutableMapOf<String, Exception>()

    var nodePairs = ConcurrentLinkedQueue<Array<Any>>()

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

    suspend fun getNodeAndProperties(skip: Int, size: Int) {
        neo4j.session().use { session ->
            session.run("MATCH (m:BaseNode) -[r]-> (p:Property) RETURN m, r, p skip $skip limit $size")
                .list()
                .forEach {
                    nodeAndPropertyQueue.add(
                         BaseAndPropertyNode(
                            baseNode = BaseNode (
                                        id = it.get("m").asNode().get("id").asString(),
                                        labels = it.get("m").asNode().labels() as List<String>
                            ),
                             propNode = PropertyNode(
                                 type = it.get("r").asRelationship().type(),
                                 value = it.get("p").asNode().get("value").asObject()
                             )
                        )
                    )
                }
        }
    }

    suspend fun createBaseNodeIdempotent(node: BaseNode) {
        when {
            node.labels.contains("File") -> writeBaseNode(
                targetTable = "common.files",
                id = node.id,
            )
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
                targetTable = "sc.field_zones",
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
            node!!.labels.contains("Directory") -> writeBaseNode(
                targetTable = "common.directories",
                id = node!!.id,
            )
            node!!.labels.contains("FileVersion") -> { println("FOUND!"); writeBaseNode(
                targetTable = "common.file_versions",
                id = node!!.id,
            ) }
            node!!.labels.contains("Partner") -> writeBaseNode(
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
            node.labels.contains("Unavailability") -> writeBaseNode(
                targetTable = "sc.person_unavailabilities",
                id = node.id,

            )
            node.labels.contains("Post") -> writeBaseNode(
                targetTable = "sc.posts",
                id = node.id
            )
            node!!.labels.contains("Location") -> writeBaseNode(
                targetTable = "common.locations",
                id = node!!.id,
            )
            node!!.labels.contains("FundingAccount") -> writeBaseNode(
                targetTable = "sc.funding_accounts",
                id = node!!.id,
            )
            node!!.labels.contains("Product") -> writeBaseNode(
                targetTable = "sc.products",
                id = node!!.id,
            )
            else -> {
                println("didn't process: ${node.labels}")
            }
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
                    "insert into $commonTable(neo4j_id, created_by, modified_by, owning_person, owning_group) values('$id', 1, 1, 1, 1) returning id;",
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
                    val (n, r, m) = nodePairs.remove()
                    createRelationship(n as BaseNode, r as Relation, m as BaseNode)
                    processedBaseNodeRels.incrementAndGet()
                    delay(1L)
                }
                println("writer 1 closing")
            }

            launch {
                println("starting writer 2")
                while (nodePairs.size > 0) {
                    val (n, r, m) = nodePairs.remove()
                    createRelationship(n as BaseNode, r as Relation, m as BaseNode)
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
            session.run("MATCH (n:BaseNode)-[r]-(m:BaseNode) RETURN n, r, m SKIP $skip LIMIT $size")
                .list()
                .forEach {
                    nodePairs.add(
                        arrayOf(
                            BaseNode(
                                id = it.get("n").asNode().get("id").asString(),
                                labels = it.get("n").asNode().labels() as List<String>
                            ),
                            Relation(
                                type = it.get("r").asRelationship().type()
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

    suspend fun checkRelationship(
        n: BaseNode,
        r: Relation,
        m: BaseNode,
        n_label: String,
        r_label: String,
        m_label: String
    ): Boolean {
        return (n.labels.contains(n_label) && r.type == r_label && m.labels.contains(m_label))
    }

    suspend fun createRelationship(n: BaseNode, r: Relation, m: BaseNode) {
        when {
            checkRelationship(n, r, m, "User", "user", "ProjectMember") -> writeRelationship(
                n,
                m,
                "sc.project_members",
                "sc.people",
                "person",
                "id"
            )
            checkRelationship(n, r, m, "User", "director", "FieldRegion") -> writeRelationship(
                n,
                m,
                "sc.field_regions",
                "admin.people",
                "director",
                "id"
            )
            checkRelationship(n, r, m, "User", "director", "FieldZone") -> writeRelationship(
                n,
                m,
                "sc.field_zone",
                "admin.people",
                "director",
                "id"
            )
            checkRelationship(n, r, m, "Budget", "universalTemplateFileNode", "File") -> writeRelationship(
                m,
                n,
                "sc.budgets",
                "common.files",
                "universal_template",
                "id"
            )
            checkRelationship(n, r, m, "Budget", "record", "BudgetRecord") -> writeRelationship(
                n,
                m,
                "sc.budget_records",
                "sc.budgets",
                "budget",
                "id"
            )
            checkRelationship(n, r, m, "Organization", "organization", "BudgetRecord") -> writeRelationship(
                n,
                m,
                "sc.budget_records",
                "sc.organizations",
                "organization",
                "id"
            )
            checkRelationship(n, r, m, "Project", "budget", "Budget") -> writeRelationship(
                n,
                m,
                "sc.budgets",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, r, m, "Project", "member", "ProjectMember") -> writeRelationship(
                n,
                m,
                "sc.project_members",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, r, m, "Project", "partnership", "Partnership") -> writeRelationship(
                n,
                m,
                "sc.partnerships",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, r, m, "BaseFile", "reportFileNode", "PeriodicReport") -> writeRelationship(
                n,
                m,
                "sc.periodic_reports",
                "common.files",
                "reportFile",
                "id"
            )
            checkRelationship(n, r, m, "FieldRegion", "fieldRegion", "Project") -> writeRelationship(
                n,
                m,
                "sc.projects",
                "sc.field_regions",
                "field_region",
                "id"
            )
            checkRelationship(n, r, m, "Directory", "rootDirectory", "Project") -> writeRelationship(
                n,
                m,
                "sc.projects",
                "common.directories",
                "root_directory",
                "id"
            )
            checkRelationship(n, r, m, "EthnologueLanguage", "ethnologue", "Language") -> writeRelationship(
                n,
                m,
                "sc.languages",
                "sil.table_of_languages",
                "ethnologue",
                "id"
            )
            checkRelationship(n, r, m, "Project", "engagement", "LanguageEngagement") -> writeRelationship(
                n,
                m,
                "sc.language_engagements",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, r, m, "Language", "language", "LanguageEngagement") -> writeRelationship(
                n,
                m,
                "sc.language_engagements",
                "sc.languages",
                "ethnologue",
                "ethnologue"
            )
            checkRelationship(n, r, m, "Project", "engagement", "InternshipEngagement") -> writeRelationship(
                n,
                m,
                "sc.internship_engagements",
                "sc.projects",
                "project",
                "id"
            )
            checkRelationship(n, r, m, "Organization", "organization", "Partner") -> writeRelationship(
                n,
                m,
                "sc.partners",
                "sc.organizations",
                "organization",
                "id"
            )
            checkRelationship(n, r, m, "Partner", "partner", "Partnership") -> writeRelationship(
                n,
                m,
                "sc.partnerships",
                "sc.partners",
                "partner",
                "organization"
            )
            checkRelationship(n, r, m, "User", "pointOfContact", "Partner") -> writeRelationship(
                n,
                m,
                "sc.partners",
                "admin.people",
                "point_of_contact",
                "id"
            )
            checkRelationship(n, r, m, "InternshipEngagement", "ceremony", "Ceremony") -> writeRelationship(
                n,
                m,
                "sc.ceremonies",
                "sc.internship_engagements",
                "project",
                "project"
            )
            checkRelationship(n, r, m, "LanguageEngagement", "ceremony", "Ceremony") -> {
                writeRelationship(n, m, "sc.ceremonies", "sc.language_engagements", "project", "project")
                writeRelationship(n, m, "sc.ceremonies", "sc.language_engagements", "ethnologue", "ethnologue")
            }
            checkRelationship(n, r, m, "FundingAccount", "fundingAccount", "Location") -> writeRelationship(
                n,
                m,
                "sc.locations",
                "sc.funding_account",
                "funding_account",
                "id"
            )
            checkRelationship(n, r, m, "FieldRegion", "defaultFieldRegion", "Location") -> writeRelationship(
                n,
                m,
                "sc.locations",
                "sc.field_regions",
                "default_region",
                "id"
            )
            checkRelationship(n, r, m, "Location", "countryOfOrigin", "InternshipEngagement") -> writeRelationship(
                n,
                m,
                "sc.internship_engagements",
                "sc.locations",
                "country_of_origin",
                "id"
            )
            checkRelationship(n, r, m, "User", "mentor", "InternshipEngagement") -> writeRelationship(
                n,
                m,
                "sc.internship_engagements",
                "admin.people",
                "mentor",
                "id"
            )
            checkRelationship(n, r, m, "User", "intern", "InternshipEngagement") -> writeRelationship(
                n,
                m,
                "sc.internship_engagements",
                "admin.people",
                "intern",
                "id"
            )
            checkRelationship(n, r, m, "BaseFile", "pnpNode", "LanguageEngagement") -> writeRelationship(
                n,
                m,
                "sc.language_engagements",
                "common.files",
                "pnp_file",
                "id"
            )
            checkRelationship(n, r, m, "Location", "primaryLocation", "Project") -> writeRelationship(
                n,
                m,
                "sc.projects",
                "sc.locations",
                "primary_location",
                "id"
            )
            checkRelationship(n, r, m, "Location", "marketingLocation", "Project") -> writeRelationship(
                n,
                m,
                "sc.projects",
                "sc.locations",
                "marketing_location",
                "id"
            )
            checkRelationship(n, r, m, "User", "unavailability", "Unavailability") -> writeRelationship(
                n,
                m,
                "sc.person_unavailabilities",
                "admin.people",
                "person",
                "id"
            )
        }

    }

    suspend fun writeRelationship(
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
        println("starting base nodes' property migration")

        var totalPropNodes = 0
        neo4j.session().use { session ->
            totalPropNodes =
                session.run("MATCH (n:BaseNode) --> (p:Property) RETURN count(p) as count").single().get("count").asInt()

            println("count of property nodes is $totalPropNodes")
        }

        if (totalPropNodes == null) {
            println("failed to count property nodes")
            return
        }

        var fetchedNodes = 0
        val batchSize = 1000
        val maxQueueSize = 5000

        coroutineScope {

            // bring neo4j ids into queue as needed until no more left
            launch {
                println("starting reader")

                while (fetchedNodes < totalPropNodes) {
                    if (nodeAndPropertyQueue.size < maxQueueSize) {
                        if (totalPropNodes - fetchedNodes > batchSize) {
//                            println("fetched nodes: $fetchedNodes")
                            getNodeAndProperties(fetchedNodes, batchSize)
                            fetchedNodes += batchSize
//                            println("queue size is ${neo4jIdQueue.size}")
                        } else {
                            getNodeAndProperties(fetchedNodes, totalPropNodes - fetchedNodes)
                            fetchedNodes += totalPropNodes - fetchedNodes
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
                while (nodeAndPropertyQueue.size > 0) {
                    val node = nodeAndPropertyQueue.remove() ?: return@launch
                    errorOutput += createPropertyNode(node.baseNode, node.propNode)
                    processedNodes.incrementAndGet()
                    delay(1L) // I don't know why this is needed, but it is.
                }
                println("writer 1 closing")
            }

            launch {
                println("starting writer 2")
                while (nodeAndPropertyQueue.size > 0) {
                    val node = nodeAndPropertyQueue.remove() ?: return@launch
                    errorOutput += createPropertyNode(node.baseNode, node.propNode)
                    processedNodes.incrementAndGet()
                    delay(1L) // I don't know why this is needed, but it is.
                }
                println("writer 2 closing")
            }


            // progress
            launch {
                val then = DateTime.now().millis

                while (processedNodes.toInt() < totalPropNodes) {
                    val lapse = DateTime.now().millis - then

                    var rate =
                        if (processedNodes.get() == 0) 0F else processedNodes.get().toFloat() / lapse.toFloat() * 1000F

                    val remainingNodes = totalPropNodes.toFloat() - processedNodes.get().toFloat()
                    val eta = remainingNodes / rate / 60F

                    print("\r${processedNodes}/$totalPropNodes rate: ${rate.toInt()} records/sec eta: ${eta.toInt()}m queue: ${nodeAndPropertyQueue.size}")

                    delay(1000L)
                }
            }

        }

        if (errorOutput.isNotEmpty()) {
            throw Exception("There were errors in property migration: \n$errorOutput")
        }
        println("property migration done")

    }

    suspend fun createPropertyNode(node: BaseNode, prop: PropertyNode): MutableMap<String, Exception> {
        var err = mutableMapOf<String, Exception>();

        if (node == null) {return err;}
        if (prop?.value == null) {return err;}  // postgres assigns values to null by default, so if they're not in neo4j we stop here.
        if (prop?.type.equals("canDelete")) { return err;}  // we're not migrating canDelete
        if (prop?.type.equals("roles")) {return err;} // skipping roles for now

        when {
            node!!.labels.contains("File") -> err = writeNodeProperty(
                targetTable = "common.files",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FileVersion") -> err = writeNodeProperty(
                targetTable = "common.file_versions",
                id = node!!.id,
                propKey = if (prop!!.type.equals("size"))
                                {"file_size"}
                          else
                                {prop!!.type},
                propValue = if (prop!!.type.equals("size"))                   // should be an integer, but saved in neo4j as integers with trailing '.0'
                                { (prop!!.value!! as Double).roundToInt() }
                            else
                                { prop!!.value!! }
            )
            node!!.labels.contains("Organization") -> err = writeNodeProperty(
                targetTable = "sc.organizations",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("TranslationProject") -> err = writeNodeProperty(
                targetTable = "sc.projects",
                id = node!!.id,
                propKey = if (prop!!.type.equals("departmentId"))
                                {"department"}
                          else prop!!.type,
                propValue = prop!!.value!!,
                foreignKey = if (prop!!.type.equals("primaryLocation"))
                                { "primary_location" }
                            else if (prop!!.type.equals("marketingLocation"))
                                {"marketing_location"}
                            else if (prop!!.type.equals("fieldRegion"))
                                {"field_region"}
                            else if (prop!!.type.equals("owningOrganization"))
                                {"owning_organization"}
                            else
                                null,
                foreignTable = if (prop!!.type.equals("primaryLocation") || prop!!.type.equals("marketingLocation"))
                                    { "sc.locations" }
                               else if (prop!!.type.equals("fieldRegion"))
                                    { "sc.field_regions" }
                               else if (prop!!.type.equals("owningOrganization"))
                                    { "sc.organizations" }
                               else
                                    null,
            )
            node!!.labels.contains("InternshipProject") -> err =  writeNodeProperty(
                targetTable = "sc.projects",
                id = node!!.id,
                propKey = if (prop!!.type.equals("departmentId"))
                            {"department" }
                          else
                            { prop!!.type },
                propValue = prop!!.value!!
            )
            node!!.labels.contains("RootUser") -> {return err;}      // skip the root user
            node!!.labels.contains("User") -> err = writeNodeProperty(
                targetTable =
                    if (prop!!.type.equals("email"))
                        {"admin.users"}
                    else if (prop!!.type.equals("password"))
                        {"admin.users"}
                    else
                        {"admin.people"},
                id = node!!.id,
                propKey =
                    if (prop!!.type.equals("realFirstName"))
                        { "private_first_name" }
                    else if (prop!!.type.equals("realLastName"))
                        { "private_last_name" }
                    else if (prop!!.type.equals("displayFirstName"))
                        { "public_first_name"}
                    else if (prop!!.type.equals("displayLastName"))
                        { "public_last_name"}
                    else
                        prop!!.type,

                propValue = prop!!.value!!,
                neo4jIdTable = if (prop!!.type.equals("email") || prop!!.type.equals("password"))
                                    {"admin.people"}
                               else
                                    null,
                foreignKey = if (prop!!.type.equals("email") || prop!!.type.equals("password"))
                                {"person"}
                             else
                                 null,
            )
            node!!.labels.contains("ProjectMember") -> err = writeNodeProperty(
                targetTable = "sc.project_members",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FieldZone") -> err = writeNodeProperty(
                targetTable = "sc.field_zones",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FieldRegion") -> err =  writeNodeProperty(
                targetTable = "sc.field_regions",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Budget") -> err = writeNodeProperty(
                targetTable = "sc.budgets",
                id = node!!.id,
                propKey = if (prop!!.type.equals("universalTemplateFile"))
                            { "universal_template" }
                          else prop!!.type,
                propValue = prop!!.value!!,
                foreignKey = if (prop!!.type.equals("universalTemplateFile"))
                                { "universal_template" }
                            else
                                null,
                foreignTable = if (prop!!.type.equals("universalTemplateFile"))
                                   { "common.files" }
                               else
                                   null,
            )
            node!!.labels.contains("EthnologueLanguage") -> err = writeNodeProperty(
                targetTable = "sil.table_of_languages",
                id = node!!.id,
                propKey = if (prop!!.type.equals("name"))
                            {"language_name"}
                          else
                            { prop!!.type },
                propValue = if (prop!!.type.equals("population"))                   // should be an integer, but saved in neo4j as integers with trailing '.0'
                                 { (prop!!.value!! as Double).roundToInt() }
                            else
                                { prop!!.value!! }
            )
            node!!.labels.contains("Language") -> err = writeNodeProperty(
                targetTable = "sc.languages",
                id = node!!.id,
                propKey =  if (prop!!.type.equals("leastOfThese"))
                            { "is_least_of_these" }
                          else if (prop!!.type.equals("isSignLanguage"))
                            { "is_sign_language" }
                          else
                            { prop!!.type },
                propValue = if (prop!!.type.equals("populationOverride"))                   // should be an integer, but saved in neo4j as integers with trailing '.0'
                                { (prop!!.value!! as Double).roundToInt() }
                            else
                                prop!!.value!!
            )
            node!!.labels.contains("LanguageEngagement") -> err = writeNodeProperty(
                targetTable = "sc.language_engagements",
                id = node!!.id,
                propKey = if (prop!!.type.equals("firstScripture"))
                            { "is_first_scripture" }
                          else if (prop!!.type.equals("lukePartnership"))
                            { "is_luke_partnership" }
                          else if (prop!!.type.equals("paratextRegistryId"))
                            { "paratext_registry" }
                          else if (prop!!.type.equals("pnp"))                       //neo4j doesn't have pnp and pnp file, but pnp references a file, so putting in pnp_file
                            { "pnp_file" }
                          else
                            { prop!!.type },
                propValue = prop!!.value!!,
                foreignKey = if (prop!!.type.equals("pnp"))
                                { "pnp_file" }
                             else
                                null,
                foreignTable = if (prop!!.type.equals("pnp"))
                                    { "common.files" }
                             else
                                 null,
            )
            node!!.labels.contains("InternshipEngagement") -> err = writeNodeProperty(
                targetTable = "sc.internship_engagements",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!,
                foreignKey = if (prop!!.type.equals("growthPlan"))
                                { "growth_plan" }
                             else
                                 null,
                foreignTable = if (prop!!.type.equals("growthPlan"))
                                    { "common.files" }
                               else
                                    null,
            )
            node!!.labels.contains("Ceremony") -> err = writeNodeProperty(
                targetTable = "sc.ceremonies",
                id = node!!.id,
                propKey = if (prop!!.type.equals("planned"))
                                {"is_planned"}
                          else prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("PeriodicReport") ->err = writeNodeProperty(
                targetTable = "sc.periodic_reports",
                id = node!!.id,
                propKey = if (prop!!.type.equals("start"))
                            { "start_at" }
                        else if (prop!!.type.equals("end"))
                            { "end_at" }
                        else
                            prop!!.type,
                propValue = prop!!.value!!,
                foreignKey =
                        if (prop!!.type.equals("reportFile"))
                            { "report_file" }
                        else
                            null,
                foreignTable = if (prop!!.type.equals("reportFile"))
                                    { "common.files" }
                               else
                                    null,
            )
            node!!.labels.contains("Directory") -> err = writeNodeProperty(
                targetTable = "common.directories",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Partner") ->  err = writeNodeProperty(
                targetTable = "sc.partners",
                id = node!!.id,
                propKey =  if (prop!!.type.equals("globalInnovationsClient"))
                                {"is_innovations_client" }
                          else
                                {prop!!.type},
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Partnership") -> err = writeNodeProperty(
                targetTable = "sc.partnerships",
                id = node!!.id,
                propKey = if(prop!!.type.equals("primary"))
                            {"is_primary"}
                          else prop!!.type,
                propValue = prop!!.value!!,
                foreignKey = if (prop!!.type.equals("agreement"))
                                { "agreement" }
                             else if (prop!!.type.equals("mou"))
                                { "mou" }
                             else
                                null,
                foreignTable = if (prop!!.type.equals("agreement") || prop!!.type.equals("mou"))
                                        { "common.files" }
                               else
                                    null,
            )
            node!!.labels.contains("BudgetRecord") -> err = writeNodeProperty(
                targetTable = "sc.budget_records",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = if (prop!!.type.equals("fiscalYear"))                   // should be an integer, but saved in neo4j as integers with trailing '.0'
                                { (prop!!.value!! as Double).roundToInt() }
                            else
                                { prop!!.value!! }
            )
            node!!.labels.contains("Location") -> err = writeNodeProperty(
                targetTable = "common.locations",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FundingAccount") -> err = writeNodeProperty(
                targetTable = "sc.funding_accounts",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = if (prop!!.type.equals("accountNumber"))                   // should be an integer, but saved in neo4j as integers with trailing '.0'
                                { (prop!!.value!! as Double).roundToInt() }
                            else
                                { prop!!.value!! }
            )
            node!!.labels.contains("Product") -> err = writeNodeProperty(
                targetTable = "sc.products",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            else -> {
                err["\nnode: $node \tprop: $prop"] = Exception("Node Not Processed")
            }

        }
        return err;
    }
    suspend fun writeNodeProperty(targetTable: String, id: String, propKey: String, propValue: Any, foreignKey: String? = null, foreignTable: String? = null, neo4jIdTable: String? = null): MutableMap<String, Exception> {
        var err = mutableMapOf<String, Exception>();
        //println(propValue.javaClass)

        val cleanedPropKey = propKey.toSnakeCase()

        var realPropValue =
                if (propValue is org.neo4j.driver.internal.value.StringValue )
                    { propValue.asString().replace("'", "''") }
                else if (propValue is kotlin.String)
                    { propValue.replace("'", "''") }
                else if (propValue is org.neo4j.driver.internal.value.BooleanValue)
                    { propValue.asBoolean() }
                else if (propValue is org.neo4j.driver.internal.value.IntegerValue)
                    { propValue.asInt()}
                else if (propValue is java.time.ZonedDateTime)
                    { java.sql.Timestamp.from(propValue.toInstant()) }
                else
                    propValue.toString().replace('[','{').replace(']','}');
        //System.out.format("%-32s%-32s%-32s%-32s%-32s\n%-32s%-32s%-32s%-32s%-32s\n", "TargetTable","Neo4JIdTable","id","propKey", "realPropValue", targetTable, foreignTable, id, cleanedPropKey, realPropValue)

        try {
            val exists = jdbcTemplate.queryForObject(
                "select exists(select id from ${foreignTable ?: neo4jIdTable ?: targetTable} where neo4j_id = ?);",
                Boolean::class.java,
                if (foreignTable != null) realPropValue else id
            )
            if (exists) {
                if (foreignTable != null) {
                    jdbcTemplate.update(
                        "update $targetTable SET $foreignKey = (SELECT id from $foreignTable where neo4j_id = ?) WHERE neo4j_id = ?",
                        realPropValue,
                        id
                    )
                } else if (neo4jIdTable != null) {
                    jdbcTemplate.update(
                        "update $targetTable SET $cleanedPropKey = '$realPropValue' WHERE $foreignKey = (SELECT id from $neo4jIdTable WHERE neo4j_id = '${id}')"
                    )
                } else {
                    //println("update $targetTable SET $cleanedPropKey = ? WHERE neo4j_id = ?")
                    jdbcTemplate.update(
                        "update $targetTable SET $cleanedPropKey = '$realPropValue' WHERE neo4j_id = ?",
                        id
                    )
                }
            }
        } catch (e: Exception) {
            err["\n\nERROR:\n\tTargetTable: $targetTable\n" +
                    "\tNeo4JIdTable: $neo4jIdTable\n" +
                    "\tid:$id\n\tpropKey:$propKey\n" +
                    "\trealPropValue: $realPropValue\n" +
                    "\tforeignTable:$foreignTable\n" +
                    "\tforeignKey:$foreignKey\n"]= Exception(e);
        }
        //println(err)
        return err;
    }

    fun String.toSnakeCase(): String{
        val regex = "(?<=[a-zA-Z])[A-Z]".toRegex();
        return regex.replace(this) {
            "_${it.value}"
        }.toLowerCase();
    }
}