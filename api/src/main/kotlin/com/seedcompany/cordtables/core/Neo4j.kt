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
                            println("fetched nodes: $fetchedNodes")
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
            val concurrency = AtomicInteger(0)
            val maxConcurrency = 4
            val processedNodes = AtomicInteger(0)

            val then = DateTime.now().millis

            launch {
                println("starting writer 1")
                while (neo4jIdQueue.size > 0) {

                    if (concurrency.get() < maxConcurrency) {

//                        GlobalScope.launch {

                            concurrency.incrementAndGet()
                            val node = neo4jIdQueue.remove() ?: return@launch
                            val lapse = DateTime.now().millis - then

                            val rate =
                                if (processedNodes.get() == 0) 0F else lapse.toFloat() * 1000F / processedNodes.get()
                                    .toFloat()

                            val remainingNodes = totalBaseNodes.toFloat() - processedNodes.get().toFloat()
                            val eta = remainingNodes/rate/60F

                            print("\r${processedNodes}/$totalBaseNodes elapsed: ${(lapse / 1000).toInt()}s rate: $rate records/sec eta: ${eta}m queue: ${neo4jIdQueue.size} concurrency: ${concurrency.get()} node: ${node.id} ${node.labels} ")

                            createBaseNodeIdempotent(node)
                            processedNodes.incrementAndGet()
                            concurrency.decrementAndGet()

//                        }
                        delay(1L) // I don't know why this is needed, but it is.
                    }
                }
                println("writer 1 closing")
            }

            launch {
                println("starting writer 2")
                while (neo4jIdQueue.size > 0) {

                    if (concurrency.get() < maxConcurrency) {

//                        GlobalScope.launch {

                        concurrency.incrementAndGet()
                        val node = neo4jIdQueue.remove() ?: return@launch
                        val lapse = DateTime.now().millis - then

                        val rate =
                            if (processedNodes.get() == 0) 0F else lapse.toFloat() * 1000F / processedNodes.get()
                                .toFloat()

                        val remainingNodes = totalBaseNodes.toFloat() - processedNodes.get().toFloat()
                        val eta = remainingNodes/rate/60F

                        print("\r${processedNodes}/$totalBaseNodes elapsed: ${(lapse / 1000).toInt()}s rate: $rate records/sec eta: ${eta}m queue: ${neo4jIdQueue.size} concurrency: ${concurrency.get()} node: ${node.id} ${node.labels} ")

                        createBaseNodeIdempotent(node)
                        processedNodes.incrementAndGet()
                        concurrency.decrementAndGet()

//                        }
                        delay(1L) // I don't know why this is needed, but it is.
                    }
                }
                println("writer 2 closing")
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
            node.labels.contains("File") -> writeBaseNode(
                targetTable = "sc.files",
                id = node.id,
                commonTable = "common.files",
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
            else -> {
            }//println("didn't process: ${node.labels}")
        }
    }

    suspend fun writeUserNode(personTable: String, id: String, userTable: String? = null) {
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
    }

    suspend fun migrateBaseNodeProperties() {

    }
}