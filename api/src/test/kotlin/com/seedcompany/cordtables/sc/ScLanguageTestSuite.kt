package com.seedcompany.cordtables.sc

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.components.tables.sc.languages.*
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
import com.seedcompany.cordtables.common.CommonSensitivity
import javax.sql.rowset.serial.SerialArray

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScLanguageTestSuite (
  @LocalServerPort
  val port: Int,

  @Autowired
  val usersUtil: AdminUsersTestUtility,

  @Autowired
  val ethnologueUtil: ScEthnologueTestUtility,

  @Autowired
  val languageTestUtil: ScLanguagesTestUtility,

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
                    withEnv("POSTGRES_PORT", util.dbPort.toString()
                    )
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
    fun `create language`(){
        val ethnologue = ethnologueUtil.`create ethnologue`(port = port.toString())
        val languageCreateResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/create",
            ScLanguagesCreateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                LanguageInput(
                    name = "English",
                    display_name = "English",
                    ethnologue = ethnologue
                )
            ),
            ScLanguagesCreateResponse::class.java
        )
        assert(languageCreateResponse !== null) {"response was null"}
        assert(languageCreateResponse.body !== null) {"response body was null"}
        assert(languageCreateResponse.body!!.id!! !== null) {"response body id was null"}
    }

    @Test
    fun `edit language`(){
        val languageId = languageTestUtil.`create language`(port = port.toString())
        val ethnologue = ethnologueUtil.`create ethnologue`(port = port.toString())

        val updateNameResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "name",
                value = "Spanish",
            ),
            ScLanguagesUpdateResponse::class.java
        )

        println(updateNameResponse)
        assert(updateNameResponse !== null) {"response was null"}
        assert(updateNameResponse.body !== null) {"response body was null"}
        // assert(updateNameResponse.body.)

        val updateDisplayNameResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "display_name",
                value = "Spanish",
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateDisplayNameResponse !== null) {"response was null"}
        assert(updateDisplayNameResponse.body !== null) {"response body was null"}

        val updateEthnologueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "ethnologue",
                value = ethnologue
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateEthnologueResponse !== null) {"response was null"}
        assert(updateEthnologueResponse.body !== null) {"response body was null"}

        val updateDisplayNamePronunciationResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "display_name_pronunciation",
                value = "Some Display Name"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateDisplayNamePronunciationResponse !== null) {"response was null"}
        assert(updateDisplayNamePronunciationResponse.body !== null) {"response body was null"}

        val updateTagsResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "tags",
                value = "{tag1,tag2}"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateTagsResponse !== null) {"response was null"}
        assert(updateTagsResponse.body !== null) {"response body was null"}

        val updatePresetInventoryResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "preset_inventory",
                value = true
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePresetInventoryResponse !== null) {"response was null"}
        assert(updatePresetInventoryResponse.body !== null) {"response body was null"}

        val updateIsDialectResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "is_dialect",
                value = true
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateIsDialectResponse !== null) {"response was null"}
        assert(updateIsDialectResponse.body !== null) {"response body was null"}

        val updateIsSignLanguageResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "is_sign_language",
                value = true
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateIsSignLanguageResponse !== null) {"response was null"}
        assert(updateIsSignLanguageResponse.body !== null) {"response body was null"}

        val updateIsLeastOfTheseResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "is_least_of_these",
                value = true
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateIsLeastOfTheseResponse !== null) {"response was null"}
        assert(updateIsLeastOfTheseResponse.body !== null) {"response body was null"}

        val updateLeastOfTheseReasonResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "least_of_these_reason",
                value = "Some Reason"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLeastOfTheseReasonResponse !== null) {"response was null"}
        assert(updateLeastOfTheseReasonResponse.body !== null) {"response body was null"}

        val updatePopulationOverrideResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "population_override",
                value = 25452
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePopulationOverrideResponse !== null) {"response was null"}
        assert(updatePopulationOverrideResponse.body !== null) {"response body was null"}

        val updateRegistryOfDialectsCodeResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "registry_of_dialects_code",
                value = "DIL CODE"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateRegistryOfDialectsCodeResponse !== null) {"response was null"}
        assert(updateRegistryOfDialectsCodeResponse.body !== null) {"response body was null"}

        val updateSensitivityResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "sensitivity",
                value = "Medium"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateSensitivityResponse !== null) {"response was null"}
        assert(updateSensitivityResponse.body !== null) {"response body was null"}

        val updateSignLanguageCodeResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "sign_language_code",
                value = "ENG"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateSignLanguageCodeResponse !== null) {"response was null"}
        assert(updateSignLanguageCodeResponse.body !== null) {"response body was null"}

        val updateSponsorStartDateResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "sponsor_start_date",
                value = "2021-08-22"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateSponsorStartDateResponse !== null) {"response was null"}
        assert(updateSponsorStartDateResponse.body !== null) {"response body was null"}

        val updateSponsorEstimatedEndDateResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "sponsor_estimated_end_date",
                value = "2022-02-04"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateSponsorEstimatedEndDateResponse !== null) {"response was null"}
        assert(updateSponsorEstimatedEndDateResponse.body !== null) {"response body was null"}

        val updateProgressBibleResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "progress_bible",
                value = true
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateProgressBibleResponse !== null) {"response was null"}
        assert(updateProgressBibleResponse.body !== null) {"response body was null"}

        val updateLocationLongResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "location_long",
                value = "location long text"
            ),
            ScLanguagesUpdateResponse::class.java
        )
        println(updateLocationLongResponse.body)
        assert(updateLocationLongResponse !== null) {"response was null"}
        assert(updateLocationLongResponse.body !== null) {"response body was null"}

        val updateIslandResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "island",
                value = "some island"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateIslandResponse !== null) {"response was null"}
        assert(updateIslandResponse.body !== null) {"response body was null"}

        val updateProvinceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "province",
                value = "some province"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateProvinceResponse !== null) {"response was null"}
        assert(updateProvinceResponse.body !== null) {"response body was null"}

        val updateFirstLanguagePopulationResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "first_language_population",
                value = 52896
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateFirstLanguagePopulationResponse !== null) {"response was null"}
        assert(updateFirstLanguagePopulationResponse.body !== null) {"response body was null"}

        val updatePopulationValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "population_value",
                value = 85635.58
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePopulationValueResponse !== null) {"response was null"}
        assert(updatePopulationValueResponse.body !== null) {"response body was null"}

        val updateLeastReachedProgressJpsLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "least_reached_progress_jps_level",
                value = "3"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLeastReachedProgressJpsLevelResponse !== null) {"response was null"}
        assert(updateLeastReachedProgressJpsLevelResponse.body !== null) {"response body was null"}

        val updateLeastReachedValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "least_reached_value",
                value = 5.25
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLeastReachedValueResponse !== null) {"response was null"}
        assert(updateLeastReachedValueResponse.body !== null) {"response body was null"}

        val updatePartnerInterestLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "partner_interest_level",
                value = "Significant"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePartnerInterestLevelResponse !== null) {"response was null"}
        assert(updatePartnerInterestLevelResponse.body !== null) {"response body was null"}

        val updatePartnerInterestValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "partner_interest_value",
                value = 45.25
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePartnerInterestValueResponse !== null) {"response was null"}
        assert(updatePartnerInterestValueResponse.body !== null) {"response body was null"}

        val updateEgidsLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "egids_level",
                value = "6b"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateEgidsLevelResponse !== null) {"response was null"}
        assert(updateEgidsLevelResponse.body !== null) {"response body was null"}

        val updateEgidsValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "egids_value",
                value = 23.15
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateEgidsValueResponse !== null) {"response was null"}
        assert(updateEgidsValueResponse.body !== null) {"response body was null"}

        val updatePartnerInterestDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "partner_interest_description",
                value = "this is partner interest description test value"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePartnerInterestDescriptionResponse !== null) {"response was null"}
        assert(updatePartnerInterestDescriptionResponse.body !== null) {"response body was null"}

        val updatePartnerInterestSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "partner_interest_source",
                value = "this is partner interest source test value"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updatePartnerInterestSourceResponse !== null) {"response was null"}
        assert(updatePartnerInterestSourceResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageLinguisticLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_linguistic_level",
                value = "Considerable"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageLinguisticLevelResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageLinguisticLevelResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageLinguisticValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_linguistic_value",
                value = 252.25
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageLinguisticValueResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageLinguisticValueResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageLinguisticDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_linguistic_description",
                value = "this is multiple_languages_leverage_linguistic_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageLinguisticDescriptionResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageLinguisticDescriptionResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageLinguisticSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_linguistic_source",
                value = "this is multiple languages leverage linguistic source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageLinguisticSourceResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageLinguisticSourceResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageJointTrainingLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_joint_training_level",
                value = "Significant"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageJointTrainingLevelResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageJointTrainingLevelResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageJointTrainingValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_joint_training_value",
                value = 635.27
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageJointTrainingValueResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageJointTrainingValueResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageJointTrainingDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_joint_training_description",
                value = "this is multiple_languages_leverage_joint_training_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageJointTrainingDescriptionResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageJointTrainingDescriptionResponse.body !== null) {"response body was null"}

        val updateMultipleLanguagesLeverageJointTrainingSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "multiple_languages_leverage_joint_training_source",
                value = "this is multiple_languages_leverage_joint_training_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateMultipleLanguagesLeverageJointTrainingSourceResponse !== null) {"response was null"}
        assert(updateMultipleLanguagesLeverageJointTrainingSourceResponse.body !== null) {"response body was null"}

        val updateLangCommIntInLanguageDevelopmentLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_language_development_level",
                value = "NoInterest"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInLanguageDevelopmentLevelResponse !== null) {"response was null"}
        assert(updateLangCommIntInLanguageDevelopmentLevelResponse.body !== null) {"response body was null"}

        val updateLangCommIntInLanguageDevelopmentValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_language_development_value",
                value = 158.25
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInLanguageDevelopmentValueResponse !== null) {"response was null"}
        assert(updateLangCommIntInLanguageDevelopmentValueResponse.body !== null) {"response body was null"}

        val updateLangCommIntInLanguageDevelopmentDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_language_development_description",
                value = "this is lang_comm_int_in_language_development_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInLanguageDevelopmentDescriptionResponse !== null) {"response was null"}
        assert(updateLangCommIntInLanguageDevelopmentDescriptionResponse.body !== null) {"response body was null"}

        val updateLangCommIntInLanguageDevelopmentSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_language_development_source",
                value = "lang_comm_int_in_language_development_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInLanguageDevelopmentSourceResponse !== null) {"response was null"}
        assert(updateLangCommIntInLanguageDevelopmentSourceResponse.body !== null) {"response body was null"}

        val updateLangCommIntInScriptureTranslationLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_scripture_translation_level",
                value = "Considerable"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInScriptureTranslationLevelResponse !== null) {"response was null"}
        assert(updateLangCommIntInScriptureTranslationLevelResponse.body !== null) {"response body was null"}

        val updateLangCommIntInScriptureTranslationValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_scripture_translation_value",
                value = 58.35
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInScriptureTranslationValueResponse !== null) {"response was null"}
        assert(updateLangCommIntInScriptureTranslationValueResponse.body !== null) {"response body was null"}

        val updateLangCommIntInScriptureTranslationDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_scripture_translation_description",
                value = "lang_comm_int_in_scripture_translation_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInScriptureTranslationDescriptionResponse !== null) {"response was null"}
        assert(updateLangCommIntInScriptureTranslationDescriptionResponse.body !== null) {"response body was null"}

        val updateLangCommIntInScriptureTranslationSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "lang_comm_int_in_scripture_translation_source",
                value = "lang_comm_int_in_scripture_translation_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateLangCommIntInScriptureTranslationSourceResponse !== null) {"response was null"}
        assert(updateLangCommIntInScriptureTranslationSourceResponse.body !== null) {"response body was null"}

        val updateAccessToScriptureInLwcLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "access_to_scripture_in_lwc_level",
                value = "Majority"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateAccessToScriptureInLwcLevelResponse !== null) {"response was null"}
        assert(updateAccessToScriptureInLwcLevelResponse.body !== null) {"response body was null"}

        val updateAccessToScriptureInLwcValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "access_to_scripture_in_lwc_value",
                value = 45.85
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateAccessToScriptureInLwcValueResponse !== null) {"response was null"}
        assert(updateAccessToScriptureInLwcValueResponse.body !== null) {"response body was null"}

        val updateAccessToScriptureInLwcDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "access_to_scripture_in_lwc_description",
                value = "this is access_to_scripture_in_lwc_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateAccessToScriptureInLwcDescriptionResponse !== null) {"response was null"}
        assert(updateAccessToScriptureInLwcDescriptionResponse.body !== null) {"response body was null"}

        val updateAccessToScriptureInLwcSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "access_to_scripture_in_lwc_source",
                value = "this is access_to_scripture_in_lwc_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateAccessToScriptureInLwcSourceResponse !== null) {"response was null"}
        assert(updateAccessToScriptureInLwcSourceResponse.body !== null) {"response body was null"}

        val updateBeginWorkGeoChallengesLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_geo_challenges_level",
                value = "VeryDifficult"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkGeoChallengesLevelResponse !== null) {"response was null"}
        assert(updateBeginWorkGeoChallengesLevelResponse.body !== null) {"response body was null"}

        val updateBeginWorkGeoChallengesValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_geo_challenges_value",
                value = 84.56
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkGeoChallengesValueResponse !== null) {"response was null"}
        assert(updateBeginWorkGeoChallengesValueResponse.body !== null) {"response body was null"}

        val updateBeginWorkGeoChallengesDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_geo_challenges_description",
                value = "this is begin_work_geo_challenges_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkGeoChallengesDescriptionResponse !== null) {"response was null"}
        assert(updateBeginWorkGeoChallengesDescriptionResponse.body !== null) {"response body was null"}

        val updateBeginWorkGeoChallengesSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_geo_challenges_source",
                value = "this is begin_work_geo_challenges_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkGeoChallengesSourceResponse !== null) {"response was null"}
        assert(updateBeginWorkGeoChallengesSourceResponse.body !== null) {"response body was null"}

        val updateBeginWorkRelPolObstaclesLevelResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_rel_pol_obstacles_level",
                value = "Moderate"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkRelPolObstaclesLevelResponse !== null) {"response was null"}
        assert(updateBeginWorkRelPolObstaclesLevelResponse.body !== null) {"response body was null"}

        val updateBeginWorkRelPolObstaclesValueResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_rel_pol_obstacles_value",
                value = 76.48
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkRelPolObstaclesValueResponse !== null) {"response was null"}
        assert(updateBeginWorkRelPolObstaclesValueResponse.body !== null) {"response body was null"}

        val updateBeginWorkRelPolObstaclesDescriptionResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_rel_pol_obstacles_description",
                value = "this is begin_work_rel_pol_obstacles_description"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkRelPolObstaclesDescriptionResponse !== null) {"response was null"}
        assert(updateBeginWorkRelPolObstaclesDescriptionResponse.body !== null) {"response body was null"}

        val updateBeginWorkRelPolObstaclesSourceResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "begin_work_rel_pol_obstacles_source",
                value = "this is begin_work_rel_pol_obstacles_source"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateBeginWorkRelPolObstaclesSourceResponse !== null) {"response was null"}
        assert(updateBeginWorkRelPolObstaclesSourceResponse.body !== null) {"response body was null"}

        val updateSuggestedStrategiesResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "suggested_strategies",
                value = "this is suggested_strategies"
            ),
            ScLanguagesUpdateResponse::class.java
        )

        assert(updateSuggestedStrategiesResponse !== null) {"response was null"}
        assert(updateSuggestedStrategiesResponse.body !== null) {"response body was null"}

        val updateCommentsResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/update",
            ScLanguagesUpdateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId,
                column = "comments",
                value = "this is comments"
            ),
            ScLanguagesUpdateResponse::class.java
        )
        assert(updateCommentsResponse !== null) {"response was null"}
        assert(updateCommentsResponse.body !== null) {"response body was null"}

        val language = languageTestUtil.`read language`(languageId, port = port.toString())
        assert(language !== null ) {"response null"}
        assert(language.language!!.name == "Spanish") {"name update failed"}
        assert(language.language!!.display_name == "Spanish") {"display_name value not saved"}
        assert(language.language!!.display_name_pronunciation ==	"Some Display Name") {"display_name_pronunciation value not saved"}
        assert(language.language!!.tags.contentToString() == "[tag1, tag2]") {"tags value not saved"}
        assert(language.language!!.preset_inventory == true) {"preset_inventory value not saved"}
        assert(language.language!!.is_dialect == true) {"is_dialect value not saved"}
        assert(language.language!!.is_sign_language == true) {"is_sign_language value not saved"}
        assert(language.language!!.is_least_of_these == true) {"is_least_of_these value not saved"}
        assert(language.language!!.least_of_these_reason ==	"Some Reason") {"least_of_these_reason value not saved"}
        assert(language.language!!.population_override == 25452) {"population_override value not saved"}
        assert(language.language!!.registry_of_dialects_code ==	"DIL CODE") {"registry_of_dialects_code value not saved"}
        assert( language.language!!.sensitivity == CommonSensitivity.Medium) {"sensitivity value not saved"}
        assert(language.language!!.sign_language_code == "ENG") {"sign_language_code value not saved"}
        // assert(language.language!!.sponsor_start_date == "2021-08-22") {"sponsor_start_date value not saved"}

        assert(language.language!!.sponsor_estimated_end_date == "2022-02-04 00:00:00.0") {"sponsor_estimated_end_date value not saved"}
        assert(language.language!!.progress_bible == true) {"progress_bible value not saved"}
        assert(language.language!!.location_long == "location long text") {"location_long value not saved"}
        assert(language.language!!.island == "some island") {"island value not saved"}
        assert(language.language!!.province == "some province") {"province value not saved"}
        assert(language.language!!.first_language_population == 52896) {"first_language_population value not saved"}
        assert(language.language!!.population_value == 0.5) {"population_value value not saved"}
        assert(language.language!!.least_reached_progress_jps_level == LeastReachedProgressScale.`3`) {"least_reached_progress_jps_level value not saved"}
        assert(language.language!!.least_reached_value == 0.5) {"least_reached_value value not saved"}
        assert(language.language!!.partner_interest_level == PartnerInterestScale.Significant) {"partner_interest_level value not saved"}
        assert(language.language!!.partner_interest_value == 0.66) {"partner_interest_value value not saved"}
        assert(language.language!!.egids_level == EgidsScale.`6b`) {"egids_level value not saved"}
        assert(language.language!!.egids_value == 0.4) {"egids_value value not saved"}
        assert(language.language!!.partner_interest_description == "this is partner interest description test value") {"partner_interest_description value not saved"}
        assert(language.language!!.partner_interest_source == "this is partner interest source test value") {"partner_interest_source value not saved"}
        assert(language.language!!.multiple_languages_leverage_linguistic_level == MultipleLanguagesLeverageLinguisticScale.Considerable) {"multiple_languages_leverage_linguistic_level value not saved"}
        assert(language.language!!.multiple_languages_leverage_linguistic_value == 0.6) {"multiple_languages_leverage_linguistic_value value not saved"}

        assert(language.language!!.multiple_languages_leverage_linguistic_description == "this is multiple_languages_leverage_linguistic_description") {"multiple_languages_leverage_linguistic_description value not saved"}
        assert(language.language!!.multiple_languages_leverage_linguistic_source == "this is multiple languages leverage linguistic source") {"multiple_languages_leverage_linguistic_source value not saved"}
        assert(language.language!!.multiple_languages_leverage_joint_training_level == MultipleLanguagesLeverageJointTrainingScale.Significant) {"multiple_languages_leverage_joint_training_level value not saved"}
        assert(language.language!!.multiple_languages_leverage_joint_training_value == 0.4) {"multiple_languages_leverage_joint_training_value value not saved"}
        assert(language.language!!.multiple_languages_leverage_joint_training_description == "this is multiple_languages_leverage_joint_training_description") {"multiple_languages_leverage_joint_training_description value not saved"}
        assert(language.language!!.multiple_languages_leverage_joint_training_source == "this is multiple_languages_leverage_joint_training_source") {"multiple_languages_leverage_joint_training_source value not saved"}
        assert(language.language!!.lang_comm_int_in_language_development_level == LangCommIntInLanguageDevelopmentScale.NoInterest) {"lang_comm_int_in_language_development_level value not saved"}
        assert(language.language!!.lang_comm_int_in_language_development_value == 0.0) {"lang_comm_int_in_language_development_value value not saved"}
        assert(language.language!!.lang_comm_int_in_language_development_description == "this is lang_comm_int_in_language_development_description") {"lang_comm_int_in_language_development_description value not saved"}
        assert(language.language!!.lang_comm_int_in_language_development_source == "lang_comm_int_in_language_development_source") {"lang_comm_int_in_language_development_source value not saved"}
        assert(language.language!!.lang_comm_int_in_scripture_translation_level == LangCommIntInScriptureTranslationScale.Considerable) {"lang_comm_int_in_scripture_translation_level value not saved"}
        assert(language.language!!.lang_comm_int_in_scripture_translation_value == 1.0) {"lang_comm_int_in_scripture_translation_value value not saved"}
        assert(language.language!!.lang_comm_int_in_scripture_translation_description == "lang_comm_int_in_scripture_translation_description") {"lang_comm_int_in_scripture_translation_description value not saved"}
        assert(language.language!!.lang_comm_int_in_scripture_translation_source == "lang_comm_int_in_scripture_translation_source") {"lang_comm_int_in_scripture_translation_source value not saved"}
        assert(language.language!!.access_to_scripture_in_lwc_level == AccessToScriptureInLwcScale.Majority) {"access_to_scripture_in_lwc_level value not saved"}
        assert(language.language!!.access_to_scripture_in_lwc_value == 0.5) {"access_to_scripture_in_lwc_value value not saved"}
        assert(language.language!!.access_to_scripture_in_lwc_description == "this is access_to_scripture_in_lwc_description") {"access_to_scripture_in_lwc_description value not saved"}
        assert(language.language!!.access_to_scripture_in_lwc_source == "this is access_to_scripture_in_lwc_source") {"access_to_scripture_in_lwc_source value not saved"}
        assert(language.language!!.begin_work_geo_challenges_level == BeginWorkGeoChallengesScale.VeryDifficult) {"begin_work_geo_challenges_level value not saved"}
        assert(language.language!!.begin_work_geo_challenges_value == 0.0) {"begin_work_geo_challenges_value value not saved"}
        assert(language.language!!.begin_work_geo_challenges_description == "this is begin_work_geo_challenges_description") {"begin_work_geo_challenges_description value not saved"}
        assert(language.language!!.begin_work_geo_challenges_source == "this is begin_work_geo_challenges_source") {"begin_work_geo_challenges_source value not saved"}
        assert(language.language!!.begin_work_rel_pol_obstacles_level == BeginWorkRelPolObstaclesScale.Moderate) {"begin_work_rel_pol_obstacles_level value not saved"}
        assert(language.language!!.begin_work_rel_pol_obstacles_value == 0.75) {"begin_work_rel_pol_obstacles_value value not saved"}
        assert(language.language!!.begin_work_rel_pol_obstacles_description == "this is begin_work_rel_pol_obstacles_description") {"begin_work_rel_pol_obstacles_description value not saved"}
        assert(language.language!!.begin_work_rel_pol_obstacles_source == "this is begin_work_rel_pol_obstacles_source") {"begin_work_rel_pol_obstacles_source value not saved"}
        assert(language.language!!.suggested_strategies == "this is suggested_strategies") {"suggested_strategies value not saved"}
        assert(language.language!!.comments == "this is comments") {"comments value not saved"}

//        assert()
    }

    @Test
    fun  `delete language`(){
        val languageId = languageTestUtil.`create language`(port = port.toString())
        val deleteResponse = rest.postForEntity(
            "http://localhost:$port/sc/languages/delete",
            ScLanguagesDeleteRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = languageId
            ),
            ScLanguagesDeleteResponse::class.java
        )

        assert(deleteResponse !== null) {"response was empty"}
        assert(deleteResponse.body !== null)
    }

    fun getPopulationValue(population: Int): Float {
      return when (population) {
        in 1..100 -> 0.000F
        in 101..500 -> 0.125F
        in 501..1000 -> 0.250F
        in 1001..10000 -> 0.375F
        in 10001..100000 -> 0.500F
        in 100001..1000000 -> 0.625F
        in 1000001..10000000 -> 0.750F
        in 10000000..100000000 -> 0.875F
        else -> 1.000F
      }
    }
}
