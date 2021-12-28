package com.seedcompany.cordtables.components.tables.sc.person_unavailabilities

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.*
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.ScPersonUnavailabilitiesCreateRequest
import com.seedcompany.cordtables.components.tables.sc.person_unavailabilities.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPersonUnavailabilitiesCreateReadRequest(
    val token: String? = null,
    val personUnavailability: personUnavailabilityInput,
)

data class ScPersonUnavailabilitiesCreateReadResponse(
    val error: ErrorType,
    val personUnavailability: personUnavailability? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPersonUnavailabilitiesCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("sc/person-unavailabilities/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScPersonUnavailabilitiesCreateReadRequest): ScPersonUnavailabilitiesCreateReadResponse {

        val createResponse = create.createHandler(
            ScPersonUnavailabilitiesCreateRequest(
                token = req.token,
                personUnavailability = req.personUnavailability
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScPersonUnavailabilitiesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScPersonUnavailabilitiesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScPersonUnavailabilitiesCreateReadResponse(error = readResponse.error, personUnavailability = readResponse.personUnavailability)
    }
}
