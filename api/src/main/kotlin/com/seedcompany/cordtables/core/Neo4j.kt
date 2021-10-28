package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.Create
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import kotlinx.coroutines.runBlocking
import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource

data class Neo4jMigrationRequest(
    val token: String? = null,
)

data class Neo4jMigrationResponse(
    val error: ErrorType,
)

data class Node1(
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
                session.run("MATCH (n:Organization) RETURN count(n) as count").single().get("count").asInt()
            )

            println("count of base nodes is $baseNodeCounter")
        }

        if (baseNodeCounter == null) {
            println("failed to count base nodes")
            return
        }

        while (baseNodeCounter.get() > 0) {
            println("loop ${baseNodeCounter.get()}")
            runBlocking {
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
                addBaseNode()
            }
        }
    }

    suspend fun addBaseNode() {
        var node1: Node1? = null

        val nextNode = baseNodeCounter.decrementAndGet()

        if (nextNode <= 0) return

        neo4j.session().use { session ->
            val aasdf =
                session.run("MATCH (m:Organization) RETURN m skip $nextNode limit 1")
                    .single()

            node1 = Node1(
                id = aasdf.get("m").asNode().get("id").asString(),
                labels = aasdf.get("m").asNode().labels() as List<String>
            )
        }

        if (node1 == null) return

        if (node1!!.labels.contains("Organization")) {
            val exists = jdbcTemplate.queryForObject(
                "select exists(select id from sc.organizations where neo4j_id = ?);",
                Boolean::class.java,
                node1!!.id
            )

            if (exists) {
                println("node ${node1!!.id} exists")
            } else {
                println("creating node ${node1!!.id} exists")
                val commonId = jdbcTemplate.queryForObject(
                    """
                            insert into common.organizations(created_by, modified_by, owning_person, owning_group)
                                values(1, 1, 1, 1) 
                                returning id;
                        """.trimIndent(),
                    Int::class.java
                )

                jdbcTemplate.update(
                    """
                    insert into sc.organizations(id, neo4j_id, created_by, modified_by, owning_person, owning_group)
                        values(?, ?, 1, 1, 1, 1);
                """.trimIndent(), commonId, node1!!.id,
                )
            }
        }
    }

    suspend fun migrateBaseNodeToBaseNodeRelationships() {

    }

    suspend fun migrateBaseNodeProperties() {

    }
}