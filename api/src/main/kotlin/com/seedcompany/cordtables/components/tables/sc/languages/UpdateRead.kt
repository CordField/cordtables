package com.seedcompany.cordtables.components.tables.sc.languages

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguagesUpdateReadRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class ScLanguagesUpdateReadResponse(
        val error: ErrorType,
        val language: Language? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguagesUpdateRead")
class UpdateRead(
    @Autowired
        val util: Utility,

    @Autowired
        val ds: DataSource,

    @Autowired
        val update: Update,

    @Autowired
        val read: Read,
) {
    @PostMapping("sc/languages/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScLanguagesUpdateReadRequest): ScLanguagesUpdateReadResponse {

        val updateResponse = update.updateHandler(
                ScLanguagesUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLanguagesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                ScLanguagesReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return ScLanguagesUpdateReadResponse(error = readResponse.error, readResponse.language)
    }
}
