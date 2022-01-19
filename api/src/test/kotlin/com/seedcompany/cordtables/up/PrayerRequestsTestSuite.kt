package com.seedcompany.cordtables.up

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.UsersTestUtility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.Testcontainers.exposeHostPorts
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.nio.file.Paths

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrayerRequestsTestSuite(
  @LocalServerPort
  val port: Int,

  @Autowired
  val testUtil: TestUtility,

  @Autowired
  val usersUtil: UsersTestUtility,

  @Autowired
  val rest: TestRestTemplate,
) {
  val userPassword = "asdfasdf"
  val url = "http://localhost:$port"

  companion object {
    val home = Paths.get("").toRealPath()
    val util = TestUtility()

    @Container
    val postgreSQLContainer: GenericContainer<Nothing> =
      GenericContainer<Nothing>(ImageFromDockerfile().withDockerfile(home.resolve("src/Dockerfile").toAbsolutePath()))
        .apply {
          withEnv("POSTGRES_USER", util.dbUser)
          withEnv("POSTGRES_PASSWORD", util.dbPassword)
          withEnv("POSTGRES_DB", util.dbDatabase)
          withEnv("POSTGRES_PORT", util.dbPort.toString())
        }.withExposedPorts(util.dbPort)

    @DynamicPropertySource
    @JvmStatic
    fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
      println(home)
      System.setProperty("DB_DOMAIN", util.dbDomain)
      System.setProperty("DB_DATABASE", util.dbDatabase)
      System.setProperty("DB_PORT", util.dbPort.toString())
      System.setProperty("DB_USERNAME", util.dbUser)
      System.setProperty("DB_PASSWORD", util.dbPassword)

      System.setProperty("SERVER_URL", util.serverUrl)
      System.setProperty("SERVER_PORT", util.serverPort.toString())

      System.setProperty("AWS_ACCESS_KEY_ID", util.awsAccessKey)
      System.setProperty("AWS_SECRET_ACCESS_KEY", util.awsSecret)

      System.setProperty("EMAIL_SERVER", util.emailServer)

      System.setProperty("NEO4J_URL", "asdfasdf")
      System.setProperty("NEO4J_USERNAME", "asdfasdf")
      System.setProperty("NEO4J_PASSWORD", "asdfasdf")

      System.setProperty("CORD_ADMIN_PASSWORD", util.cordAdminPassword)

      registry.add("spring.datasource.username") { util.dbUser }
      registry.add("spring.datasource.password") { util.dbPassword }
      registry.add(
        "spring.datasource.jdbcUrl"
      ) { "jdbc:postgresql://localhost:${postgreSQLContainer.getMappedPort(util.dbPort)}/${util.dbDatabase}" }

    }
  }

  init {
    exposeHostPorts(port);
  }

  @Test
  fun user() {
    val user1 = usersUtil.register(port.toString(), "user1@cordtables.com", userPassword)
    assert(true) // temp until we have some legit assert in this test case
  }

}

