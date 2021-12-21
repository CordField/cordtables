package com.seedcompany.cordtables.components.tables.up

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.UtilityTest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.reflect.typeOf


data class UpPrayerRequestsListResponseString(
  val error: String,
  val prayerRequests: MutableList<prayerRequest>?
)



@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ["spring.main.lazy-initialization=true"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PrayerRequestTests(

  @Autowired
  val util: UtilityTest,

) {

    val userPassword = "asdfasdf"
    val url = "http://localhost:${util.port}"
    val token = util.getToken()

    val req_lang_id = util.getRandomValueFromTable("common.languages", "id", "int")
    val translatorId = util.getRandomValueFromTable("admin.people", "id", "int")

    var prayerRequestOneId = 0

    var prayerRequestEntry1 = prayerRequestInput(
        request_language_id = req_lang_id as Int?,
        target_language_id = req_lang_id as Int?,
        sensitivity = "Low",
        organization_name = util.faker.company.name(),
        parent = null,
        translator = translatorId as Int?,
        location = util.faker.address.city(),
        title = util.fakerSentence(5),
        content = util.fakerParagraph(1),
        reviewed = true,
        prayer_type = "Request",
    )

    var prayerRequestUpdate1 = prayerRequestInput(
        request_language_id = util.getRandomValueFromTable("common.languages", "id", "int") as Int?,
        target_language_id = util.getRandomValueFromTable("common.languages", "id", "int") as Int?,
        sensitivity = "High",
        organization_name = util.faker.company.name(),
        parent = prayerRequestEntry1.id,
        translator = util.getRandomValueFromTable("admin.people", "id", "int") as Int?,
        location = util.faker.address.city(),
        title = util.fakerSentence(5),
        content = util.fakerParagraph(1),
        reviewed = true,
        prayer_type = "Update",
    )

    var prayerRequestEntry2 = prayerRequestInput(
        request_language_id = req_lang_id as Int?,
        target_language_id = req_lang_id as Int?,
        sensitivity = "Medium",
        organization_name = util.faker.company.name(),
        parent = null,
        translator = translatorId as Int?,
        location = util.faker.address.city(),
        title = util.fakerSentence(5),
        content = util.fakerParagraph(1),
        reviewed = false,
        prayer_type = "Request",
    )

//    var prayerRequestUpdate2 = prayerRequestInput(
//        request_language_id = util.getRandomValueFromTable("common.languages", "id", "int") as Int?,
//        target_language_id = util.getRandomValueFromTable("common.languages", "id", "int") as Int?,
//        sensitivity = "High",
//        organization_name = util.faker.company.name(),
//        parent = prayerRequestEntry1.id,
//        translator = util.getRandomValueFromTable("admin.people", "id", "int") as Int?,
//        location = util.faker.address.city(),
//        title = util.fakerSentence(5),
//        content = util.fakerParagraph(1),
//        reviewed = true,
//        prayer_type = "Celebration",
//    )

    var prayerFormData1 = PrayerForm(
        creatorEmail = util.faker.internet.email(),
        translatorEmail =  util.faker.internet.email(),
        ethCode = util.getRandomValueFromTable("sil.language_index", "lang", "string") as String,
        sensitivity = "Low",
        location = util.faker.address.city(),
        prayerType = "Request",
        title = util.fakerSentence(5),
        content = util.fakerParagraph(1),
    )

    @Test
    @Order(1)
    fun create(){
        val createResponseOne = util.rest.postForEntity("$url/up-prayer-requests/create", UpPrayerRequestsCreateRequest(
            token = token,
            prayerRequest = prayerRequestEntry1
        ), UpPrayerRequestsCreateResponse::class.java)

        assert(createResponseOne !== null) { "PrayerRequestTwo response was null" }
        assert(createResponseOne.body?.error === ErrorType.NoError) { "PrayerRequestTwo error" }
        this.prayerRequestEntry1.id = createResponseOne.body?.id

        val createResponseTwo = util.rest.postForEntity("$url/up-prayer-requests/create", UpPrayerRequestsCreateRequest(
          token = token,
          prayerRequest = prayerRequestEntry2
        ), UpPrayerRequestsCreateResponse::class.java)
        assert(createResponseTwo !== null) { "PrayerRequestTwo response was null" }
        assert(createResponseTwo.body?.error === ErrorType.NoError) { "PrayerRequestTwo error" }
        this.prayerRequestEntry2.id = createResponseTwo.body?.id
    }

    @Test
    @Order(2)
    fun read(){
        val readPrayerRequestOne = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
            token = token,
            id = this.prayerRequestEntry1.id
        ), UpPrayerRequestsReadResponse::class.java)

        assert(readPrayerRequestOne !== null) { "readPrayerRequestOne response was null" }
        assert(readPrayerRequestOne.body?.error === ErrorType.NoError) { "readPrayerRequestOne Read error: ${readPrayerRequestOne.body?.error}" }

        println(readPrayerRequestOne.body?.prayerRequest?.request_language_id == prayerRequestEntry1.request_language_id)
        println(readPrayerRequestOne.body?.prayerRequest?.request_language_id)
        println(prayerRequestEntry1.request_language_id)

        assert(readPrayerRequestOne.body?.prayerRequest?.request_language_id == prayerRequestEntry1.request_language_id) { "readPrayerRequestOne Request language comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.target_language_id == prayerRequestEntry1.target_language_id) { "readPrayerRequestOne Target language comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.sensitivity == prayerRequestEntry1.sensitivity) { "readPrayerRequestOne Sensitivity comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.organization_name == prayerRequestEntry1.organization_name) { "readPrayerRequestOne organization_name comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.translator == prayerRequestEntry1.translator) { "readPrayerRequestOne translator comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.location == prayerRequestEntry1.location) { "readPrayerRequestOne location comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.title == prayerRequestEntry1.title) { "readPrayerRequestOne title comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.content == prayerRequestEntry1.content) { "readPrayerRequestOne content comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.reviewed == prayerRequestEntry1.reviewed) { "readPrayerRequestOne reviewed comparison failed"}
        assert(readPrayerRequestOne.body?.prayerRequest?.prayer_type == prayerRequestEntry1.prayer_type) { "readPrayerRequestOne prayer_type comparison failed"}

        val readPrayerRequestTwo = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
          token = token,
          id = prayerRequestEntry2.id
        ), UpPrayerRequestsReadResponse::class.java)

        assert(readPrayerRequestTwo.body?.error === ErrorType.NoError) { "readPrayerRequestTwo Read error: ${readPrayerRequestTwo.body?.error}" }
        assert(readPrayerRequestTwo.body?.prayerRequest?.request_language_id == prayerRequestEntry2.request_language_id) { "readPrayerRequestTwo Request language comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.target_language_id == prayerRequestEntry2.target_language_id) { "readPrayerRequestTwo Target language comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.sensitivity == prayerRequestEntry2.sensitivity) { "readPrayerRequestTwo Sensitivity comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.organization_name == prayerRequestEntry2.organization_name) { "readPrayerRequestTwo organization_name comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.translator == prayerRequestEntry2.translator) { "readPrayerRequestTwo translator comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.location == prayerRequestEntry2.location) { "readPrayerRequestTwo location comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.title == prayerRequestEntry2.title) { "readPrayerRequestTwo title comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.content == prayerRequestEntry2.content) { "readPrayerRequestTwo content comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.reviewed == prayerRequestEntry2.reviewed) { "readPrayerRequestTwo reviewed comparison failed"}
        assert(readPrayerRequestTwo.body?.prayerRequest?.prayer_type == prayerRequestEntry2.prayer_type) { "readPrayerRequestTwo prayer_type comparison failed"}
    }

//    @AfterAll
//    fun assertOutput() {
//      println(prayerFormData1.id)
//    }

    @Test
    @Order(3)
    fun googleFormCreate(){
        val googleFormCreateResponse = util.rest.postForEntity("$url/up-prayer-requests/create-from-form", UpPrayerRequestsCreateFromFormRequest(
            token = token,
            prayerForm = prayerFormData1
        ), UpPrayerRequestsCreateFromFormResponse::class.java)
        assert(googleFormCreateResponse !== null) { "googleFormCreate response was null" }
        assert(googleFormCreateResponse.body?.error === ErrorType.NoError) { "googleFormCreate Response error: ${googleFormCreateResponse.body?.error}" }
        prayerFormData1.id = googleFormCreateResponse.body?.id
    }


    @Test
    @Order(4)
    fun list(){
        val prayerRequests = util.rest.postForEntity("$url/up-prayer-requests/list", UpPrayerRequestsListRequest(token = token), UpPrayerRequestsListResponse::class.java)
        assert(prayerRequests !== null) { "prayerRequests list response was null" }
        assert(prayerRequests.body?.error === ErrorType.NoError) { "prayerRequests list Response error: ${prayerRequests.body?.error}" }

        var prayerRequestEntry1Exists:Boolean = false
        var prayerRequestEntry2Exists:Boolean = false
        var prayerFormData1Exists:Boolean = false

        for (pr in prayerRequests.body?.prayerRequests!!){
            if (pr.id == prayerRequestEntry1.id){
                prayerRequestEntry1Exists = true
            }
            if (pr.id == prayerRequestEntry2.id){
                prayerRequestEntry2Exists = true
            }
            if (pr.id == prayerFormData1.id){
                prayerFormData1Exists = true
            }
        }
        assert(prayerRequestEntry1Exists) {"prayerRequest entry 1 not exists in list response"}
        assert(prayerRequestEntry2Exists) {"prayerRequest entry 2 not exists in list response"}
        assert(prayerFormData1Exists) {"prayerRequest Google Form entry not exists in list response"}
    }

    @Test
    @Order(5)
    fun update(){

        val updateRequestLanguage = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "request_language_id",
            value = prayerRequestUpdate1.request_language_id
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateRequestLanguage !== null) { "updateRequestLanguage response was null" }
        assert(updateRequestLanguage.body?.error === ErrorType.NoError) { "updateRequestLanguage Response error: ${updateRequestLanguage.body?.error}" }

        val updateTargetLanguage = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "target_language_id",
            value = prayerRequestUpdate1.target_language_id
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateTargetLanguage !== null) { "updateTargetLanguage response was null" }
        assert(updateTargetLanguage.body?.error === ErrorType.NoError) { "updateTargetLanguage Response error: ${updateTargetLanguage.body?.error}" }

        val updateSensitivity = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "sensitivity",
            value = prayerRequestUpdate1.sensitivity
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateSensitivity !== null) { "updateSensitivity response was null" }
        assert(updateSensitivity.body?.error === ErrorType.NoError) { "updateSensitivity Response error: ${updateSensitivity.body?.error}" }

        val updateOrganizationName = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "organization_name",
            value = prayerRequestUpdate1.organization_name
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateOrganizationName !== null) { "updateOrganizationName response was null" }
        assert(updateOrganizationName.body?.error === ErrorType.NoError) { "updateOrganizationName Response error: ${updateOrganizationName.body?.error}" }

        val updateParent = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "parent",
            value = prayerRequestUpdate1.parent
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateParent !== null) { "updateParent response was null" }
        assert(updateParent.body?.error === ErrorType.NoError) { "updateParent Response error: ${updateParent.body?.error}" }

        val updateTranslator = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "translator",
            value = prayerRequestUpdate1.translator
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateTranslator !== null) { "updateTranslator response was null" }
        assert(updateTranslator.body?.error === ErrorType.NoError) { "updateTranslator Response error: ${updateTranslator.body?.error}" }

        val updateLocation = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "location",
            value = prayerRequestUpdate1.location
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateLocation !== null) { "updateLocation response was null" }
        assert(updateLocation.body?.error === ErrorType.NoError) { "updateLocation Response error: ${updateLocation.body?.error}" }

        val updateTitle = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "title",
            value = prayerRequestUpdate1.title
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateTitle !== null) { "updateTitle response was null" }
        assert(updateTitle.body?.error === ErrorType.NoError) { "updateTitle Response error: ${updateTitle.body?.error}" }

        val updateContent = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "content",
            value = prayerRequestUpdate1.content
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateContent !== null) { "updateContent response was null" }
        assert(updateContent.body?.error === ErrorType.NoError) { "updateContent Response error: ${updateContent.body?.error}" }

        val updateReviewed = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "reviewed",
            value = prayerRequestUpdate1.reviewed
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updateReviewed !== null) { "updateReviewed response was null" }
        assert(updateReviewed.body?.error === ErrorType.NoError) { "updateReviewed Response error: ${updateReviewed.body?.error}" }

        val updatePrayerType = util.rest.postForEntity("$url/up-prayer-requests/update", UpPrayerRequestsUpdateRequest(
            token = token,
            id = prayerRequestEntry1.id,
            column = "prayer_type",
            value = prayerRequestUpdate1.prayer_type
        ), UpPrayerRequestsUpdateResponse::class.java)

        assert(updatePrayerType !== null) { "updatePrayerType response was null" }
        assert(updatePrayerType.body?.error === ErrorType.NoError) { "updatePrayerType Response error: ${updatePrayerType.body?.error}" }
    }

    @Test
    @Order(6)
    fun updateReadAndVerify(){
        val readPrayerRequestUpdate = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
          token = token,
          id = prayerRequestEntry1.id
        ), UpPrayerRequestsReadResponse::class.java)

        assert(readPrayerRequestUpdate !== null) { "updateReadAndVerify response was null" }
        assert(readPrayerRequestUpdate.body?.error === ErrorType.NoError) { "Prayer Request Updated Read failed: ${readPrayerRequestUpdate.body?.error}" }
        assert(readPrayerRequestUpdate.body?.prayerRequest?.request_language_id == prayerRequestUpdate1.request_language_id) { "updateReadAndVerify request_language_id comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.target_language_id == prayerRequestUpdate1.target_language_id) { "updateReadAndVerify target_language_id comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.sensitivity == prayerRequestUpdate1.sensitivity) { "updateReadAndVerify Sensitivity comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.organization_name == prayerRequestUpdate1.organization_name) { "updateReadAndVerify organization_name comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.translator == prayerRequestUpdate1.translator) { "updateReadAndVerify translator comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.location == prayerRequestUpdate1.location) { "updateReadAndVerify location comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.title == prayerRequestUpdate1.title) { "updateReadAndVerify title comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.content == prayerRequestUpdate1.content) { "updateReadAndVerify content comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.reviewed == prayerRequestUpdate1.reviewed) { "updateReadAndVerify reviewed comparison failed"}
        assert(readPrayerRequestUpdate.body?.prayerRequest?.prayer_type == prayerRequestUpdate1.prayer_type) { "updateReadAndVerify prayer_type comparison failed"}
    }

    @Test
    @Order(7)
    fun delete(){
        //val deletePrayerRequest
        val deletePrayerRequestEntry1 = util.rest.postForEntity("$url/up-prayer-requests/delete", prayerRequestEntry1.id?.let {
          UpPrayerRequestsDeleteRequest(
            token = token,
            id = it
          )
        }, UpPrayerRequestsDeleteResponse::class.java)

        assert(deletePrayerRequestEntry1 !== null) { "deletePrayerRequestEntry1 response was null" }
        assert(deletePrayerRequestEntry1.body?.error === ErrorType.NoError) {"deletePrayerRequestEntry1 response error: ${deletePrayerRequestEntry1.body?.error}"}

        val deletePrayerRequestEntry2 = util.rest.postForEntity("$url/up-prayer-requests/delete", prayerRequestEntry2.id?.let {
            UpPrayerRequestsDeleteRequest(
                token = token,
                id = it
            )
        }, UpPrayerRequestsDeleteResponse::class.java)

        assert(deletePrayerRequestEntry2 !== null) { "deletePrayerRequestEntry2 response was null" }
        assert(deletePrayerRequestEntry2.body?.error === ErrorType.NoError) {"deletePrayerRequestEntry2 response error: ${deletePrayerRequestEntry2.body?.error}"}

        val deleteprayerFormData1 = util.rest.postForEntity("$url/up-prayer-requests/delete", prayerFormData1.id?.let {
            UpPrayerRequestsDeleteRequest(
                token = token,
                id = it
            )
        }, UpPrayerRequestsDeleteResponse::class.java)

        assert(deleteprayerFormData1 !== null) { "deleteprayerFormData1 response was null" }
        assert(deleteprayerFormData1.body?.error === ErrorType.NoError) {"deleteprayerFormData1 response error: ${deleteprayerFormData1.body?.error}"}
    }

    @Test
    @Order(8)
    fun verifyDelete(){
      val verifyDeleteEntry1 = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
        token = token,
        id = prayerRequestEntry1.id
      ), UpPrayerRequestsReadResponse::class.java)

      assert(verifyDeleteEntry1 !== null) { "verifyDeleteEntry1 response was null" }
      assert(verifyDeleteEntry1.body?.error !== ErrorType.NoError) {"verifyDeleteEntry1 response error ${verifyDeleteEntry1.body?.error}"}

      val verifyDeleteEntry2 = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
        token = token,
        id = prayerRequestEntry2.id
      ), UpPrayerRequestsReadResponse::class.java)

      assert(verifyDeleteEntry2 !== null) { "verifyDeleteEntry2 response was null" }
      assert(verifyDeleteEntry2.body?.error !== ErrorType.NoError) {"verifyDeleteEntry2 response error ${verifyDeleteEntry2.body?.error}"}

      val verifyDeleteEntry3 = util.rest.postForEntity("$url/up-prayer-requests/read", UpPrayerRequestsReadRequest(
        token = token,
        id = prayerFormData1.id
      ), UpPrayerRequestsReadResponse::class.java)

      assert(verifyDeleteEntry3 !== null) { "verifyDeleteEntry3 response was null" }
      assert(verifyDeleteEntry3.body?.error !== ErrorType.NoError) {"verifyDeleteEntry3 response error ${verifyDeleteEntry3.body?.error}"}

    }
}
