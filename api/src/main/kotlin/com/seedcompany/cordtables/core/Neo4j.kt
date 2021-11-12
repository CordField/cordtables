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
import org.springframework.jdbc.core.SqlParameterValue
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLType
import java.sql.Types
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource
import kotlin.concurrent.thread
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

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
        println("starting base nodes' property migration")

        var totalPropertyNodes = AtomicInteger(0)
        neo4j.session().use { session ->
            totalPropertyNodes.set(
                session.run("MATCH (n:BaseNode) --> (p:Property) RETURN count(p) as count").single().get("count").asInt())

            println("total count of property nodes is $totalPropertyNodes")
        }

        if (totalPropertyNodes == null) {
            println("failed to count property nodes")
            return
        }

        for( i in 0 until totalPropertyNodes.get()){
            println("loop $i")
            createPropertyNode(i)
        }

    }

    suspend fun createPropertyNode(skip: Int) {
        var node: BaseNode? = null
        var prop: PropertyNode? = null
        neo4j.session().use { session ->
            val baseNodeReturn =
                session.run("MATCH (m:BaseNode) -[r]-> (p:Property) RETURN m, r, p skip $skip limit 1")
                    .single()

            node = BaseNode(
                id = baseNodeReturn.get("m").asNode().get("id").asString(),
                labels = baseNodeReturn.get("m").asNode().labels() as List<String>
            )
            prop = PropertyNode(
                type = baseNodeReturn.get("r").asRelationship().type(),
                value = baseNodeReturn.get("p").asNode().get("value").asObject()
            )
            if (node == null) return
            if (prop?.value == null) return  // postgres assigns values to null by default, so if they're not in neo4j we stop here.
            if (prop?.type.equals("canDelete")) return  // we're not migrating canDelete
        }
        when {
            node!!.labels.contains("File") -> writeNodeProperty(
                targetTable = "sc.files",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Organization") -> writeNodeProperty(
                targetTable = "sc.organizations",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("TranslationProject") -> writeNodeProperty(
                targetTable = "sc.projects",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("InternshipProject") -> writeNodeProperty(
                targetTable = "sc.projects",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("RootUser") -> return      // skip the root user
            node!!.labels.contains("User") -> writeNodeProperty(
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
            node!!.labels.contains("ProjectMember") -> writeNodeProperty(
                targetTable = "sc.project_members",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FieldZone") -> writeNodeProperty(
                targetTable = "sc.field_zone",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("FieldRegion") -> writeNodeProperty(
                targetTable = "sc.field_regions",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Budget") -> writeNodeProperty(
                targetTable = "sc.budgets",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("EthnologueLanguage") -> writeNodeProperty(
                targetTable = "sil.table_of_languages",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Language") -> writeNodeProperty(
                targetTable = "sc.languages",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("LanguageEngagement") -> writeNodeProperty(
                targetTable = "sc.language_engagements",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!,
                preferredType = if(prop!!.type.equals("status")) { Types.OTHER } else null
            )
            node!!.labels.contains("InternshipEngagement") -> writeNodeProperty(
                targetTable = "sc.internship_engagements",
                id = node!!.id,
                propKey =
                    if (prop!!.type.equals("modifiedAt"))
                        { "modified_at" }
                    else if (prop!!.type.equals("growthPlan"))
                        { "growth_plan" }
                    else
                        prop!!.type,
                propValue = prop!!.value!!,
                preferredType = if(prop!!.type.equals("status")) { Types.OTHER } else null,
                foreignKey = if (prop!!.type.equals("growthPlan"))
                                { "growth_plan" }
                             else
                                 null,
                neo4jIdTable = if (prop!!.type.equals("growthPlan"))
                                    return
                             else
                                null,
            )
            node!!.labels.contains("Ceremony") -> writeNodeProperty(
                targetTable = "sc.ceremonies",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("PeriodicReport") -> writeNodeProperty(
                targetTable = "sc.periodic_reports",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Directory") -> writeNodeProperty(
                targetTable = "common.directories",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("BaseFile") -> writeNodeProperty(
                targetTable = "common.files",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Partner") -> writeNodeProperty(
                targetTable = "sc.partners",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("Partnership") -> writeNodeProperty(
                targetTable = "sc.partnerships",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            node!!.labels.contains("BudgetRecord") -> writeNodeProperty(
                targetTable = "sc.budget_records",
                id = node!!.id,
                propKey = prop!!.type,
                propValue = prop!!.value!!
            )
            else -> {
            }//println("didn't process: ${node.labels}")

        }
    }
    suspend fun writeNodeProperty(targetTable: String, id: String, propKey: String, propValue: Any, foreignKey: String? = null, neo4jIdTable: String? = null, preferredType: Int? = null) {

        val exists = jdbcTemplate.queryForObject(
            "select exists(select id from ${neo4jIdTable ?: targetTable} where neo4j_id = ?);",
            Boolean::class.java,
            id
        )

        println(propValue.javaClass.kotlin)
        var realPropValue =
                if (propValue is org.neo4j.driver.internal.value.StringValue )
                    { propValue.asString() }
                else if (propValue is org.neo4j.driver.internal.value.BooleanValue)
                    { propValue.asBoolean() }
                else if (propValue is org.neo4j.driver.internal.value.IntegerValue)
                    { propValue.asInt()}
                else if (propValue is java.time.ZonedDateTime)
                    { java.sql.Timestamp.from(propValue.toInstant()) }
                else
                    propValue;
        println("$targetTable\t$id\t$propKey\t$realPropValue")
        if (exists) {
            if (neo4jIdTable != null) {
                println("update $targetTable SET $propKey = ? WHERE $foreignKey = (SELECT id from $neo4jIdTable where neo4j_id = ?)")
                jdbcTemplate.update(
                    "update $targetTable SET $propKey = ? WHERE $foreignKey = (SELECT id from $neo4jIdTable where neo4j_id = ?)",
                    realPropValue,
                    id
                )
            } else {
                println("update $targetTable SET $propKey = ? WHERE neo4j_id = ?")
                jdbcTemplate.update(
                    "update $targetTable SET $propKey = '$realPropValue' WHERE neo4j_id = ?",
                    id
                )
            }

        } else {
            throw Exception("A BaseNode is not created for this Property yet.")
        }

    }
}