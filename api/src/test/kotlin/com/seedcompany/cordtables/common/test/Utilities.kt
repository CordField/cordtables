package com.seedcompany.cordtables.common.test

import com.seedcompany.cordtables.components.user.LoginRequest
import com.seedcompany.cordtables.components.user.LoginReturn
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.Extension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.stereotype.Component
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import java.nio.file.Paths
import javax.sql.DataSource
import kotlin.reflect.KClass


@Component
class Utilities (
  @LocalServerPort
  val port: Int,

  @Autowired
  val rest: TestRestTemplate,

  @Autowired
  val ds: DataSource,
  ){
  val userPassword = "asdfasdf"
  val url = "http://localhost:$port"


  companion object {
    val home = Paths.get("").toRealPath()

    @Container
    private val postgreSQLContainer: GenericContainer<Nothing> = GenericContainer<Nothing>(ImageFromDockerfile().withDockerfile(
      home.resolve("src/Dockerfile").toAbsolutePath()))
      .apply {
        withEnv("POSTGRES_USER", "postgres")
        withEnv("POSTGRES_PASSWORD", "asdfasdf")
        withEnv("POSTGRES_DB", "cordfield")
        withEnv("POSTGRES_PORT", "5432")
      }.withExposedPorts(5432)


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

      registry.add("spring.datasource.username", {"postgres"})
      registry.add("spring.datasource.password", {"asdfasdf"})
      registry.add("spring.datasource.jdbcUrl", {"jdbc:postgresql://localhost:${postgreSQLContainer.getMappedPort(5432)}/postgres"})
    }
  }

  init {
    org.testcontainers.Testcontainers.exposeHostPorts(port);
  }

  fun userLogin(email: String, password: String): LoginReturn {
    val userLoginResponse = rest.postForEntity("$url/user/login", LoginRequest(email = email, password = password), LoginReturn::class.java)
    return  userLoginResponse.body!!
  }

}
