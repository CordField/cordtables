package com.seedcompany.cordtables.sil

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.common.CommonLanguagesTestUtility
import com.seedcompany.cordtables.components.tables.sil.language_index.LanguageIndexInput
import com.seedcompany.cordtables.components.tables.sil.language_index.SilLanguageIndexCreateRequest
import com.seedcompany.cordtables.components.tables.sil.language_index.SilLanguageIndexCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class SilLanguageIndexTestUtility(
  @Autowired
  val testUtil: TestUtility,

  @Autowired
  val usersUtil: AdminUsersTestUtility,

  @Autowired
  val commonLanguagesUtil: CommonLanguagesTestUtility,

  @Autowired
  val rest: TestRestTemplate,
) {
  fun `create sil language_index entry`(port: String, lang: String): String {

    val createResponse = rest.postForEntity(
      "http://localhost:$port/sil/language-index/create",
      SilLanguageIndexCreateRequest(
        token = usersUtil.getAdminToken(port = port),
        language = LanguageIndexInput(
          id = commonLanguagesUtil.`create common language`(port),
          lang = lang,
          country = "us",
          name_type = "L",
          name = "name",
        )
      ),
      SilLanguageIndexCreateResponse::class.java,
    )

    assert(createResponse !== null) { "common language create response was null" }
    assert(createResponse.body !== null) { "response body was null" }
    assert(createResponse.body!!.id !== null) { "response id was null" }

    return createResponse.body!!.id!!

  }
}
