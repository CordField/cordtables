package com.seedcompany.cordtables.sc

import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.components.tables.sc.languages.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class ScLanguagesTestUtility(
  @Autowired
  val rest: TestRestTemplate,

  @Autowired
  val ethnologueUtil: ScEthnologueTestUtility,

  @Autowired
  val usersUtil: AdminUsersTestUtility,
) {

    fun `create language`(port: String): String{
        val ethnologue = ethnologueUtil.`create ethnologue`(port)

        val language = rest.postForEntity(
            "http://localhost:$port/sc/languages/create",
            ScLanguagesCreateRequest(
                token = usersUtil.getAdminToken(port),
                LanguageInput(
                    name = "English",
                    display_name = "English",
                    ethnologue = ethnologue
                )
            ),
            ScLanguagesCreateResponse::class.java
        )
        return language.body!!.id!!
    }

    fun `read language`(id: String, port: String): ScLanguagesReadResponse {
        val language = rest.postForEntity(
            "http://localhost:$port/sc/languages/read",
            ScLanguagesReadRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                id = id,
            ),
            ScLanguagesReadResponse::class.java
        )
        return language.body!!
    }
}
