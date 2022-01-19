package com.seedcompany.cordtables.common

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.components.tables.common.languages.CommonLanguagesCreateRequest
import com.seedcompany.cordtables.components.tables.common.languages.CommonLanguagesCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class CommonLanguagesTestUtility(
  @Autowired
  val testUtil: TestUtility,

  @Autowired
  val usersUtil: AdminUsersTestUtility,

  @Autowired
  val rest: TestRestTemplate,
) {
  fun `create common language`(port: String): String {

    val createResponse = rest.postForEntity(
      "http://localhost:$port/common/languages/create",
      CommonLanguagesCreateRequest(token = usersUtil.getAdminToken(port = port)),
      CommonLanguagesCreateResponse::class.java,
    )

    assert(createResponse !== null) {"common language create response was null"}
    assert(createResponse.body !== null) {"response body was null"}
    assert(createResponse.body!!.id !== null) {"response id was null"}

    return createResponse.body!!.id!!

  }
}
