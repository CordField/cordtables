package com.seedcompany.cordtables.components.tables.sc.field_regions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_regions.ScFieldRegionsReadRequest
import com.seedcompany.cordtables.components.tables.sc.field_regions.ScFieldRegionsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.field_regions.fieldRegion
import com.seedcompany.cordtables.components.tables.sc.field_regions.fieldRegionInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldRegionsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScFieldRegionsUpdateReadResponse(
    val error: ErrorType,
    val fieldRegion: fieldRegion? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldRegionsUpdateRead")
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
    @PostMapping("sc-field-regions/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScFieldRegionsUpdateReadRequest): ScFieldRegionsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScFieldRegionsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScFieldRegionsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScFieldRegionsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScFieldRegionsUpdateReadResponse(error = readResponse.error, readResponse.fieldRegion)
    }
}