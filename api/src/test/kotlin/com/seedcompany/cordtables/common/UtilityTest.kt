package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.components.user.LoginRequest
import com.seedcompany.cordtables.components.user.LoginReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.stereotype.Component

import io.github.serpro69.kfaker.Faker
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource


@Component
class UtilityTest (
    @LocalServerPort
    val port: Int,

    @Autowired
    val rest: TestRestTemplate,

    @Autowired
    val ds: DataSource
){
    val userPassword = "asdfasdf"
    val url = "http://localhost:$port"
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    val faker = Faker()

    val fakerSentence = { size: Int ->
      List(size) { this.faker.lorem.words() }.joinToString(" ")
    }

    val fakerParagraph = { size: Int ->
      List(size) { "${fakerSentence(25)}\n" }.joinToString(" ")
    }

    fun getToken(): String?{
        val user = userLogin("devops@tsco.org", "asdfasdf")
        return if (user.error == ErrorType.NoError){
            user.token
        }
        else {
            null
        }
    }

    fun userLogin(email: String, password: String): LoginReturn {
        val userLoginResponse = rest.postForEntity("$url/user/login", LoginRequest(email = email, password = password), LoginReturn::class.java)
        assert(userLoginResponse !== null) { "response was null" }
        assert(userLoginResponse.body !== null) { "response body was null" }
        // assert(!userLoginResponse.body!!.isAdmin) { "new user should not be admin" }
        assert(userLoginResponse.body!!.token !== null) { "token should be present" }
        // assert(userLoginResponse.body!!.readableTables.isEmpty()) { "shouldn't be able to read any tables" }
        return  userLoginResponse.body!!
    }

    fun getRandomValueFromTable(table: String, field: String, type: String): Any? {
        val totalRows = getNumRows(table)
        var fieldValue: Any? = null
        this.ds.connection.use { conn ->
            val statement = conn.prepareCall(
              """
                  SELECT $field FROM $table OFFSET floor(random() * $totalRows) LIMIT 1;
              """.trimIndent()
            )
            val result = statement.executeQuery()
            while (result.next()){
                if (type == "int"){
                    fieldValue = result.getInt(field)
                }
                if (type == "string"){
                    fieldValue = result.getString(field)
                }
            }
        }
      return fieldValue
    }

    fun getNumRows(table: String): Int? {
        return jdbcTemplate.queryForObject("""
            SELECT COUNT(*) FROM $table
          """.trimIndent(), Int::class.java)
    }

//    fun checkIdExistsInResponseData(data:List<Any>, id){
//        for (el in data){
//            if (el. == id){
//
//          }
//        }
//    }
}
