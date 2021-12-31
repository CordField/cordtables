package com.seedcompany.cordtables.common

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.sql.DataSource

@Component
class Utility(
//    @Autowired
//    val appConfig: AppConfig,
  @Autowired
  val ds: DataSource,
) {

  val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
  val encoder = Argon2PasswordEncoder(16, 32, 1, 4096, 3)
  val adminGroupId: String? = this.adminGroupId()
  val adminRole: String? = this.adminRole()
  val publicGroupId: String? = this.publicGroupId()
  val personId: String? = this.personId()

  //language=SQL
  val getUserIdFromSessionIdQuery = """
        select user_id from sessions where session_id = ?;
    """.trimIndent()

  fun getBearer(): String {
    return "todo"
  }

  fun createToken(): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    val token = (1..64)
      .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
      .map(charPool::get)
      .joinToString("")

    return token
  }

  fun getUserIdFromSessionId(sessionId: String): String? {
    //language=SQL
    val userId: String? = jdbcTemplate.queryForObject(
      """
          select user_id from sessions where session_id = ?;
      """.trimIndent(),
      String::class.java,
      sessionId,
    )

    return userId
  }

  fun isEmailValid(email: String?): Boolean {
    if (email != null && email.contains('@') && email.contains('.')) return true
    return false
  }

  fun isAdmin(token: String): Boolean {

    var isAdmin = false

    this.ds.connection.use { conn ->
      //language=SQL
      val statement = conn.prepareCall(
        """
                select exists(
                	select id 
                	from admin.roles 
                	where id in (
                		select role 
                		from admin.role_memberships 
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                	and name = 'Administrator'
                );
            """.trimIndent()
      )

      statement.setString(1, token);
      val result = statement.executeQuery()

      if (result.next()) {
        isAdmin = result.getBoolean(1)
      }
    }

    return isAdmin;
  }

  fun userHasCreatePermission(token: String, tableName: String): Boolean {
    if (isAdmin(token)) {
      return true;
    }
    var userHasCreatePermission: Boolean = false;
    this.ds.connection.use { conn ->
      //language=SQL
      val statement = conn.prepareCall(
        """
                select exists(
                	select a.id 
                	from admin.roles as a 
                    inner join admin.role_table_permissions as b 
                    on a.id = b.role 
                	where a.id in (
                		select role 
                		from admin.role_memberships
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                   and b.table_name::text = ?
                   and b.table_permission = 'Create'
                );
            """.trimIndent()
      )

      statement.setString(1, token);
      statement.setString(2, tableName);
      val result = statement.executeQuery()

      if (result.next()) {
        userHasCreatePermission = result.getBoolean(1)
      }
    }
    return userHasCreatePermission;
  }

  fun userHasDeletePermission(token: String, tableName: String): Boolean {
    if (isAdmin(token)) {
      return true;
    }
    var userHasDeletePermission: Boolean = false;
    this.ds.connection.use { conn ->
      //language=SQL
      val statement = conn.prepareCall(
        """
                select exists(
                	select a.id 
                	from admin.roles as a 
                    inner join admin.role_table_permissions as b 
                    on a.id = b.role 
                	where a.id in (
                		select role 
                		from admin.role_memberships
                		where person = (
                			select person
                			from admin.tokens 
                			where token = ?
                        )
                    ) 
                   and b.table_name::text = ?
                   and b.table_permission = 'Delete'
                );
            """.trimIndent()
      )

      statement.setString(1, token);
      statement.setString(2, tableName);
      val result = statement.executeQuery()

      if (result.next()) {
        userHasDeletePermission = result.getBoolean(1)
      }
    }
    return userHasDeletePermission;
  }

  fun userHasUpdatePermission(token: String, tableName: String, columnName: String, rowId: String): Boolean {
    if (isAdmin(token)) {
      return true;
    }
    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
    var userHasUpdatePermission = false;
    val paramSource = MapSqlParameterSource()
    paramSource.addValue("token", token)
    paramSource.addValue("table", tableName)
    paramSource.addValue("column", columnName)
    paramSource.addValue("rowId", rowId)

    this.ds.connection.use { conn ->
      //language=SQL
      val statement = """
                with row_level_access as 
                    (
                        select 1 as dummy_column
                        from admin.group_row_access as a  
                        inner join admin.group_memberships as b 
                        on a.group_id = b.group_id 
                        inner join admin.tokens as c 
                        on b.person = c.person
                        where a.table_name::text = :table
                        and a.row = :rowId
                        and c.token = :token
                    ), 
                column_level_access as 
                    (
                        select 1 as dummy_column
                        from admin.role_column_grants a 
                        inner join admin.role_memberships b 
                        on a.role = b.role 
                        inner join admin.tokens c 
                        on b.person = c.person 
                        where a.table_name::text = :table 
                        and a.column_name = :column
                        and a.access_level = 'Write'
                        and c.token = :token
                    )
                    select exists(
                        select 1 from row_level_access as a 
                        inner join column_level_access as b
                        on a.dummy_column = b.dummy_column
                    )
               
            """.trimIndent()
      val result = jdbcTemplate.queryForRowSet(statement, paramSource)
      println("result $result")
      if (result.next()) {
        userHasUpdatePermission = result.getBoolean(1)
      }
    }
    return userHasUpdatePermission
  }

  fun userHasUpdatePermissionMultipleColumns(
    token: String,
    tableName: String,
    columnNames: MutableList<String>
  ): Boolean {
    if (isAdmin(token)) {
      return true;
    }

    var userHasUpdatePermission = false;
    this.ds.connection.use { conn ->
      var updateSql = "select count(*) from \n" +
        "(select column_name from admin.role_column_grants as a \n" +
        "inner join admin.roles as b  \n" +
        "on a.role = b.id \n" +
        "where b.id in (\n" +
        "\tselect role \n" +
        "    from admin.role_memberships\n" +
        "\twhere person = (\n" +
        "\t\t\t\t\tselect person\n" +
        "\t\t\t\t\tfrom admin.tokens \n" +
        "\t\t\t\t\twhere token = ?\n" +
        "                    )\n" +
        ")\n" +
        " and a.table_name::text = ? \n" +
        " and a.access_level = 'Write' \n" +
        " and a.column_name in ( ''\n"
//            need to add a empty column above so that when no update is sent, the query doesn't give an error for an empty subquery in check
      columnNames.forEach { columnName ->
        updateSql = "$updateSql '$columnName',"
      }
      updateSql = updateSql.dropLast(1)
      updateSql = "$updateSql ))sq"
      //language=SQL
      val statement = conn.prepareCall("$updateSql ;")
      statement.setString(1, token);
      statement.setString(2, tableName);
      val result = statement.executeQuery()

      if (result.next()) {
        val countOfColumnsWithWriteAccess = result.getInt(1)
        if (countOfColumnsWithWriteAccess == columnNames.size) {
          userHasUpdatePermission = true
        }
      }
    }
    return userHasUpdatePermission;

  }

  fun getReadableTables(token: String): List<String> {
    val tableNames = mutableListOf<String>()

    if (isAdmin(token)) {
      this.ds.connection.use { conn ->
        val statement = conn.prepareCall(
          """
                        select table_schema || '.' || table_name as table_name 
                        from information_schema.tables 
                        where table_schema in ('admin', 'common', 'sc', 'sil', 'up')
                        order by table_name asc;
                    """.trimIndent()
        )

        val result = statement.executeQuery()
        while (result.next()) {
          tableNames.add(result.getString("table_name"))
        }
      }
    } else {
      this.ds.connection.use { conn ->
        val statement = conn.prepareCall(
          """
                        select distinct table_name
                        from admin.role_column_grants as a
                        inner join admin.role_memberships as b
                        on a.role = b.role
                        inner join admin.tokens as c
                        on b.person = c.person
                        where c.token = ? order by table_name asc;
                    """.trimIndent()
        )
        statement.setString(1, token)
        val result = statement.executeQuery()
        while (result.next()) {
          tableNames.add(result.getString("table_name"))
        }
      }

    }

    return tableNames
      .filter { !it.contains("_history") }
      .filter { !it.contains("_peer") }
      .map { it.replace('_', '-') };
  }

  fun updateField(token: String, table: String, column: String, id: String?, value: Any?, cast: String? = "") {

    if (userHasUpdatePermission(
        token = token,
        tableName = table,
        columnName = column,
        rowId = id!!
      )
    ) {
      jdbcTemplate.update(
        """
                    update $table 
                    set 
                        $column = ?$cast,
                        modified_by = 
                            (
                              select person 
                              from admin.tokens 
                              where token = ?
                            ),
                        modified_at = CURRENT_TIMESTAMP
                    where id = ?::uuid;
                """.trimIndent(),
        value,
        token,
        id,
      )
    }
  }

  // Char -> Decimal -> Hex
  fun convertStringToHex(str: String): String? {
    val hex = StringBuffer()

    // loop chars one by one
    for (temp in str.toCharArray()) {

      // convert char to int, for char `a` decimal 97
      val decimal = temp.code

      // convert int to hex, for decimal 97 hex 61
      hex.append(Integer.toHexString(decimal))
    }
    return hex.toString()
  }

  // Hex -> Decimal -> Char
  fun convertHexToString(hex: String): String? {
    val result = StringBuilder()

    // split into two chars per loop, hex, 0A, 0B, 0C...
    var i = 0
    while (i < hex.length - 1) {
      val tempInHex = hex.substring(i, i + 2)

      //convert hex to decimal
      val decimal = tempInHex.toInt(16)

      // convert the decimal to char
      result.append(decimal.toChar())
      i += 2
    }
    return result.toString()
  }

  fun getPeopleDetailsFromIds(idArray: MutableList<String>): MutableList<PeopleDetails> {
    var peopleDetails: MutableList<PeopleDetails> = mutableListOf()
//    var stringPeopleIds = "("
//    if(idArray.isNotEmpty()) {idArray.forEach { it -> stringPeopleIds+="$it," }}
//    else {stringPeopleIds += "0,"}
//    stringPeopleIds = stringPeopleIds.dropLast(1)
//    stringPeopleIds+=")"

//    if (idArray.isEmpty()) idArray.add(0)

    this.ds.connection.use { conn ->
      //language=SQL
      val statement = conn.prepareCall(
        """
               select id, public_first_name, public_last_name from admin.people where id  = ANY(?)
            """.trimIndent()
      )

      statement.setObject(1, idArray.toTypedArray(), java.sql.Types.ARRAY);
      println(statement)
      val result = statement.executeQuery()
      while (result.next()) {
        peopleDetails.add(
          PeopleDetails(
            id = result.getInt(1),
            public_first_name = result.getString(2),
            public_last_name = result.getString(3)
          )
        )
      }
    }
    return peopleDetails;
  }

  fun isSchemaExists(): Boolean {
    var result: Int? = 0
    try {
      result = jdbcTemplate.queryForObject(
        """
                    SELECT count(schema_name) as size FROM information_schema.schemata WHERE schema_name = 'admin';
                """.trimIndent(),
        Int::class.java
      )
    } catch (e: EmptyResultDataAccessException) {
      println("Schemas not created")
    }
    return result != null && result > 0
  }

  fun personId(): String? {
    return if (this.isSchemaExists()) {
      jdbcTemplate.queryForObject(
        """
                    SELECT id FROM admin.people WHERE sensitivity_clearance = 'High';
                """.trimIndent(),
        String::class.java,
      )
    } else {
      null
    }
  }

  fun adminGroupId(): String? {
    return if (this.isSchemaExists()) {
      jdbcTemplate.queryForObject(
        """
                    SELECT id FROM admin.groups WHERE  name = 'Administrators';
                """.trimIndent(),
        String::class.java
      )
    } else {
      null
    }
  }

  fun publicGroupId(): String? {
    return if (this.isSchemaExists()) {
      jdbcTemplate.queryForObject(
        """
                    SELECT id FROM admin.groups WHERE name='Public'
                """.trimIndent(),
        String::class.java
      )
    } else {
      null
    }
  }

  fun adminRole(): String? {
    return if (this.isSchemaExists()) {
      jdbcTemplate.queryForObject(
        """
                    SELECT id FROM admin.roles WHERE name='Administrator'
                """.trimIndent(),
        String::class.java
      )
    } else {
      null
    }
  }
}

inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
  return enumValues<T>().any { it.name == name }
}


