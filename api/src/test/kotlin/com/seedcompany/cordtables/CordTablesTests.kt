package com.seedcompany.cordtables

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
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.nio.file.Paths
import kotlin.io.path.Path

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CordTablesTests(
        @LocalServerPort
        val port: Int,

        @Autowired
        val rest: TestRestTemplate,
) {
    val userPassword = "asdfasdf"
    val url = "http://localhost:$port"

    @Container
    private val container: BrowserWebDriverContainer<*> = BrowserWebDriverContainer<Nothing>()
            .withCapabilities(ChromeOptions().addArguments("no-sandbox").addArguments("headless"))
    companion object {
        val home = Paths.get("").toRealPath()
        @Container
        val postgreSQLContainer:GenericContainer<Nothing> = GenericContainer<Nothing>(ImageFromDockerfile().withDockerfile(home.resolve("src/Dockerfile").toAbsolutePath()))
                .apply {
                    withEnv("POSTGRES_USER", "postgres")
                    withEnv("POSTGRES_PASSWORD", "asdfasdf")
                    withEnv("POSTGRES_DB", "cordfield")
                    withEnv("POSTGRES_PORT", "5432")
                }.withExposedPorts(5432)




        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            println(home)
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

//            registry.add("spring.datasource.jdbcUrl", postgreSQLContainer::getJdbcUrl)
//            do this manually

//            registry.add("spring.datasource.host", postgreSQLContainer::getContainerIpAddress);
//            registry.add("spring.datasource.port") { postgreSQLContainer::getMappedPort };
            registry.add("spring.datasource.username", {"postgres"})
            registry.add("spring.datasource.password", {"asdfasdf"})
            registry.add("spring.datasource.jdbcUrl", {"jdbc:postgresql://localhost:${postgreSQLContainer.getMappedPort(5432)}/postgres"})

//            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)

        }
    }

    init {
        exposeHostPorts(port);
    }

    @Test
    fun `doesThisWork`() {
        container.webDriver["http://host.testcontainers.internal:$port/"]
        val messageElement = container.webDriver.findElementByTagName("app-root")
        println("app-root: ${messageElement.toString()}")
        assert(true)
    }

    @Test
    fun user() {
        val user1 = register("user1@cordtables.com", userPassword)
        assert(true) // temp until we have some legit assert in this test case
    }

    fun register(email: String, password: String): RegisterReturn {
        val newUserResponse = rest.postForEntity("$url/user/register", RegisterRequest("asdf@asdf.asdf", userPassword), RegisterReturn::class.java)
        assert(newUserResponse !== null) { "response was null" }
        assert(newUserResponse.body !== null) { "response body was null" }
        assert(!newUserResponse.body!!.isAdmin) { "new user should not be admin" }
        assert(newUserResponse.body!!.token !== null) { "token should be present" }
        assert(newUserResponse.body!!.readableTables.size == 0) { "shouldn't be able to read any tables" }

        return newUserResponse.body!!
    }

}
