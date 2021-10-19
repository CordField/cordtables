package com.seedcompany.cordtables.sc

import com.seedcompany.cordtables.components.user.RegisterRequest
import com.seedcompany.cordtables.components.user.RegisterReturn
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.Testcontainers.exposeHostPorts
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LanguagesEx(
    @LocalServerPort
    val port: Int,

    @Autowired
    val rest: TestRestTemplate,
) {
    val userPassword = "asdfasdf"
    val url = "http://localhost:$port"

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withUsername("postgres")
            withPassword("asdfasdf")
            withDatabaseName("cordfield")
        }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            System.setProperty("DB_DOMAIN", "host.docker.internal")
            System.setProperty("DB_DATABASE", "cordfield")
            System.setProperty("DB_PORT", "5432")
            System.setProperty("DB_USERNAME", "postgres")
            System.setProperty("DB_PASSWORD", "asdfasdf")

            System.setProperty("CORD_ADMIN_PASSWORD", "asdfasdf")
            System.setProperty("CORD_API_GRAPHQL_URL", "asdfasdf")
            System.setProperty("CORD_API_EMAIL", "asdfasdf")
            System.setProperty("CORD_API_PASSWORD", "asdfasdf")

            System.setProperty("SERVER_URL", "http://localhost:8080")
            System.setProperty("SERVER_PORT", "8080")

            registry.add("spring.datasource.jdbcUrl", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    init {
        exposeHostPorts(port);
    }

    @Test
    fun user() {
        val user1 = register("user1@cordtables.com", userPassword)

        assert(true) // temp until we have some legit assert in this test case
    }

    fun register(email: String, password: String): RegisterReturn {
        val newUserResponse = rest.postForEntity("$url/user/register", RegisterRequest("asdf@asdf.asdf", userPassword), RegisterReturn::class.java)

        assert(newUserResponse !== null) {"response was null"}
        assert(newUserResponse.body !== null) {"response body was null"}
        assert(!newUserResponse.body!!.isAdmin) {"new user should not be admin"}
        assert(newUserResponse.body!!.token !== null) {"token should be present"}
        assert(newUserResponse.body!!.readableTables.size == 0) {"shouldn't be able to read any tables"}

        return newUserResponse.body!!
    }

}
