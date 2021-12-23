package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesReadRequest
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.personUnavailability
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.personUnavailabilityInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPersonUnavailabilitiesUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPersonUnavailabilitiesUpdateReadResponse(
    val error: ErrorType,
    val personUnavailability: personUnavailability? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesUpdateRead")
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
    @PostMapping("sc-person-unavailabilities/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScPersonUnavailabilitiesUpdateReadRequest): ScPersonUnavailabilitiesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScPersonUnavailabilitiesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScPersonUnavailabilitiesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScPersonUnavailabilitiesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScPersonUnavailabilitiesUpdateReadResponse(error = readResponse.error, readResponse.personUnavailability)
    }
}
