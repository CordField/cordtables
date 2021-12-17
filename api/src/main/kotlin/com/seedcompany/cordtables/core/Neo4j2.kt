package com.seedcompany.cordtables.core

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.admin.users.Create
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import org.neo4j.driver.Driver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.util.concurrent.ConcurrentLinkedQueue
import javax.sql.DataSource

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

  var neo4jIdQueue = ConcurrentLinkedQueue<BaseNode>()
  var nodeAndPropertyQueue = ConcurrentLinkedQueue<BaseAndPropertyNode>()
  var errorOutput = mutableMapOf<String, Exception>()

  var nodePairs = ConcurrentLinkedQueue<Array<Any>>()

  @PostMapping("migrate/neo4j2")
  @ResponseBody
  suspend fun createHandler(@RequestBody req: Neo4jMigrationRequest): Neo4jMigrationResponse {

    return Neo4jMigrationResponse(ErrorType.NoError)
  }

}
