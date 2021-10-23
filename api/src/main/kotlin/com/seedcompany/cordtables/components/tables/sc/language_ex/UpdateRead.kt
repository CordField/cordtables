package com.seedcompany.cordtables.components.tables.sc.language_ex

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class LanguageExUpdateReadRequest(
    val token: String?,
    val languageEx: LanguageExInput? = null,
)

data class LanguageExUpdateReadResponse(
    val error: ErrorType,
    val languageEx: LanguageEx? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExUpdate")
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
    @PostMapping("sc-languages/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: LanguageExUpdateReadRequest): LanguageExUpdateReadResponse {

        val updateResponse = update.updateHandler(
            LanguageExUpdateRequest(
                token = req.token,
                languageEx = req.languageEx,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return LanguageExUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            LanguageExReadRequest(
                token = req.token,
                id = req.languageEx!!.id
            )
        )

        return LanguageExUpdateReadResponse(error = readResponse.error, readResponse.languageEx)
    }
}
