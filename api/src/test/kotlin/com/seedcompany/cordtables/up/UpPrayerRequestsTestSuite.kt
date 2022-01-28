package com.seedcompany.cordtables.up

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.AdminPeopleTestUtility
import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.common.CommonLanguagesTestUtility
import com.seedcompany.cordtables.components.tables.up.prayer_requests.*
import com.seedcompany.cordtables.sil.SilLanguageIndexTestUtility
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
  val usersUtil: AdminUsersTestUtility,

  @Autowired
  val silLanguageIndexUtil: SilLanguageIndexTestUtility,

  @Autowired
  val commonLanguagesTestUtility: CommonLanguagesTestUtility,

  @Autowired
  val adminPeopleTestUtility: AdminPeopleTestUtility,

  @Autowired
  val rest: TestRestTemplate,
) {

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

      System.setProperty("DB_DOMAIN", util.dbDomain)
      System.setProperty("DB_DATABASE", util.dbDatabase)
      System.setProperty("DB_PORT", util.dbPort.toString())
      System.setProperty("DB_USERNAME", util.dbUser)
      System.setProperty("DB_PASSWORD", util.dbPassword)

      System.setProperty("SERVER_URL", util.serverUrl)
      System.setProperty("SERVER_PORT", util.serverPort.toString())

      System.setProperty("AWS_ACCESS_KEY_ID", util.awsAccessKey)
      System.setProperty("AWS_SECRET_ACCESS_KEY", util.awsSecret)+

      System.setProperty("EMAIL_SERVER", util.emailServer)

      System.setProperty("NEO4J_URL", "asdfasdf")
      System.setProperty("NEO4J_USERNAME", "asdfasdf")
      System.setProperty("NEO4J_PASSWORD", "asdfasdf")

      System.setProperty("CORD_ADMIN_PASSWORD", util.cordAdminPassword)

      registry.add("cord.admin.password") { util.cordAdminPassword }

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
    fun `create prayer request`(){

        val languageId = commonLanguagesTestUtility.`create common language`(port = port.toString())
        val translator = adminPeopleTestUtility.`create admin people`(port = port.toString())

        val prayerRequestCreateResponse = rest.postForEntity(
            "http://localhost:$port/up/prayer-requests/create",
            UpPrayerRequestsCreateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                prayerRequest = prayerRequestInput(
                    request_language_id = languageId,
                    target_language_id = languageId,
                    sensitivity = "Low",
                    organization_name = "Test Organization",
                    parent = null,
                    translator = translator,
                    location = "Texas",
                    title = "Test Prayer Request",
                    content = "Test Prayer Request Content",
                    reviewed = true,
                    prayer_type = "Request",
                )
            ),
            UpPrayerRequestsCreateResponse::class.java
        )

        assert(prayerRequestCreateResponse !== null) {"response was null"}
        assert(prayerRequestCreateResponse.body !== null) {"response body was null"}
        assert(prayerRequestCreateResponse.body!!.id !== null) {"response id was null"}
    }

    @Test
    fun `create prayer request from form`() {

        val ethCode = "eng"

        // create language first
        val silLangIndexId = silLanguageIndexUtil.`create sil language_index entry`(port = port.toString(), lang = ethCode)

        // create prayer entry
        val prayerCreateResponse = rest.postForEntity(
            "http://localhost:$port/up/prayer-requests/create-from-form",
            UpPrayerRequestsCreateFromFormRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                prayerForm = PrayerForm(
                    creatorEmail = "creator_email@asdf.asdf",
                    translatorEmail = "translator_email@asdf.asdf",
                    ethCode = ethCode,
                    sensitivity = "Low",
                    location = "location",
                    prayerType = "Request",
                    title = "title",
                    content = "content",
                ),
            ),
            UpPrayerRequestsCreateFromFormResponse::class.java,
        )
        assert(prayerCreateResponse !== null) {"response was null"}
        assert(prayerCreateResponse.body !== null) {"response body was null"}
        assert(prayerCreateResponse.body!!.id !== null) {"response id was null"}
    }

}

