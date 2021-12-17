package com.seedcompany.cordtables.components.tables.up

import com.seedcompany.cordtables.Utilities2
import com.seedcompany.cordtables.common.test.Utilities
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.nio.file.Paths
import org.testcontainers.containers.BrowserWebDriverContainer
import org.openqa.selenium.chrome.ChromeOptions
import org.testcontainers.Testcontainers.exposeHostPorts

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrayerRequests(
  @LocalServerPort
  val port: Int,

  @Autowired
    val util: Utilities2,

//  @Autowired
//     val rest: TestRestTemplate,
)  {
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

      System.setProperty("NEO4J_URL", "asdfasdf")
      System.setProperty("NEO4J_USERNAME", "asdfasdf")
      System.setProperty("NEO4J_PASSWORD", "asdfasdf")

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


//  val userPassword = util.userPassword
//  val url = util.url

  @Test
  fun `doesThisWork`() {
    println(util.asdf())
    //val user = util.userLogin("devops@tsco.org", "asdfasdf")
//    if(user.error == ErrorType.NoError){
//      val prayerRequest = rest.postForEntity("$url/up-prayer-requests/create", UpPrayerRequestsCreateRequest(
//        token = user.token,
//        prayerRequest = prayerRequestInput(
//
//        )
//      ), UpPrayerRequestsCreateResponse::class.java)
//    }
//    else{
//
//    }

    //println("test")
//    container.webDriver["http://host.testcontainers.internal:$port/"]
//    val messageElement = container.webDriver.findElementByTagName("app-root")
//    println("app-root: ${messageElement.toString()}")
//    assert(true)
  }


//  fun userLogin(email: String, password: String): LoginReturn {
//
//    val userLoginResponse = rest.postForEntity("$url/user/login", LoginRequest(email = email, password = password), LoginReturn::class.java)
//    //assert(userLoginResponse !== null) { "response was null" }
//    //assert(userLoginResponse.body !== null) { "response body was null" }
//    //assert(!userLoginResponse.body!!.isAdmin) { "new user should not be admin" }
//    //assert(userLoginResponse.body!!.token !== null) { "token should be present" }
//    //assert(userLoginResponse.body!!.readableTables.size == 0) { "shouldn't be able to read any tables" }
//
//    return  userLoginResponse.body!!
//  }

}
