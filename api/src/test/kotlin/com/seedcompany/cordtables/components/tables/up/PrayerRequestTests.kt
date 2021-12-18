package com.seedcompany.cordtables.components.tables.up

import com.seedcompany.cordtables.common.UtilityTest
import com.seedcompany.cordtables.components.tables.up.prayer_requests.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.postForEntity
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = ["spring.main.lazy-initialization=true"])
class PrayerRequestTests(

  @Autowired
  val util: UtilityTest,

) {

    val userPassword = "asdfasdf"
    val url = "http://localhost:${util.port}"
    val token = util.getToken()

    @Test
    fun list(){
      val prayerRequests = util.rest.postForEntity("$url/up-prayer-requests/list", UpPrayerRequestsListRequest(token = token), UpPrayerRequestsListResponse::class.java)
      println(prayerRequests)
    }

    @Test
    fun googleFormCreate(){

        val googleFormCreateResponse = util.rest.postForEntity("$url/up-prayer-requests/create-from-form", UpPrayerRequestsCreateFromFormRequest(
            token = token,
            prayerForm = PrayerForm(
              creatorEmail = "devops@tsco.org",
              translatorEmail = "devops@tsco.org",
              ethCode = "en",
              sensitivity = "Low",
              location = "Test",
              prayerType = "Request",
              title = "New prayer request",
              content = "New prayer request content test"
            )
        ), UpPrayerRequestsCreateFromFormResponse::class.java)



    }




}
