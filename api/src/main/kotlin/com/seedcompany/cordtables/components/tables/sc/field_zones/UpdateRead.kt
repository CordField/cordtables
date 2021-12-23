package com.seedcompany.cordtables.components.tables.sc.field_zones

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_zones.ScFieldZonesReadRequest
import com.seedcompany.cordtables.components.tables.sc.field_zones.ScFieldZonesUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.field_zones.fieldZone
import com.seedcompany.cordtables.components.tables.sc.field_zones.fieldZoneInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldZonesUpdateReadRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScFieldZonesUpdateReadResponse(
    val error: ErrorType,
    val fieldZone: fieldZone? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldZonesUpdateRead")
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
    @PostMapping("sc-field-zones/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScFieldZonesUpdateReadRequest): ScFieldZonesUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScFieldZonesUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScFieldZonesUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScFieldZonesReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScFieldZonesUpdateReadResponse(error = readResponse.error, readResponse.fieldZone)
    }
}
