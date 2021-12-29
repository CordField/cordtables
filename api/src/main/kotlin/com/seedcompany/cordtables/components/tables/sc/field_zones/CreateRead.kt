package com.seedcompany.cordtables.components.tables.sc.field_zones

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_zones.*
import com.seedcompany.cordtables.components.tables.sc.field_zones.ScFieldZonesCreateRequest
import com.seedcompany.cordtables.components.tables.sc.field_zones.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldZonesCreateReadRequest(
    val token: String? = null,
    val fieldZone: fieldZoneInput,
)

data class ScFieldZonesCreateReadResponse(
    val error: ErrorType,
    val fieldZone: fieldZone? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldZonesCreateRead")
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
    @PostMapping("sc/field-zones/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScFieldZonesCreateReadRequest): ScFieldZonesCreateReadResponse {

        val createResponse = create.createHandler(
            ScFieldZonesCreateRequest(
                token = req.token,
                fieldZone = req.fieldZone
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScFieldZonesCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScFieldZonesReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScFieldZonesCreateReadResponse(error = readResponse.error, fieldZone = readResponse.fieldZone)
    }
}
