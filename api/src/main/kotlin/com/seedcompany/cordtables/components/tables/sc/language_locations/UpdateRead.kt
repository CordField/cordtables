package com.seedcompany.cordtables.components.tables.sc.language_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.language_locations.ScLanguageLocationsReadRequest
import com.seedcompany.cordtables.components.tables.sc.language_locations.ScLanguageLocationsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.language_locations.languageLocation
import com.seedcompany.cordtables.components.tables.sc.language_locations.languageLocationInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageLocationsUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScLanguageLocationsUpdateReadResponse(
    val error: ErrorType,
    val languageLocation: languageLocation? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsUpdateRead")
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
    @PostMapping("sc/language-locations/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScLanguageLocationsUpdateReadRequest): ScLanguageLocationsUpdateReadResponse {

      if (req.token == null) return ScLanguageLocationsUpdateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScLanguageLocationsUpdateReadResponse(ErrorType.AdminOnly)

        val updateResponse = update.updateHandler(
            ScLanguageLocationsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScLanguageLocationsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScLanguageLocationsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScLanguageLocationsUpdateReadResponse(error = readResponse.error, readResponse.languageLocation)
    }
}
