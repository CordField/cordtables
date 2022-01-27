package com.seedcompany.cordtables.sc

import com.seedcompany.cordtables.TestUtility
import com.seedcompany.cordtables.admin.AdminUsersTestUtility
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueCreateRequest
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ScEthnologueCreateResponse
import com.seedcompany.cordtables.components.tables.sc.ethnologue.ethnologueInput
import com.seedcompany.cordtables.sil.SilLanguageIndexTestUtility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.stereotype.Component

@Component
class ScEthnologueTestUtility(
    @Autowired
    val testUtil: TestUtility,

    @Autowired
    val rest: TestRestTemplate,

    @Autowired
    val silLanguageIndexUtil: SilLanguageIndexTestUtility,

    @Autowired
    val usersUtil: AdminUsersTestUtility,
) {

    fun `create ethnologue`(port: String): String {
        val ethCode = "eng"
        val languageIndex = silLanguageIndexUtil.`create sil language_index entry`(port = port.toString(), lang = ethCode)

        val ethnloue = rest.postForEntity(
            "http://localhost:$port/sc/ethnologue/create",
            ScEthnologueCreateRequest(
                token = usersUtil.getAdminToken(port = port.toString()),
                ethnologue = ethnologueInput(
                    language_index = languageIndex,
                    code = "ENG",
                    language_name = "English",
                    population = 15000,
                    provisional_code = "EN",
                    sensitivity = "Medium"
                )
            ),
            ScEthnologueCreateResponse::class.java
        )
        return ethnloue.body!!.id!!
    }



}
