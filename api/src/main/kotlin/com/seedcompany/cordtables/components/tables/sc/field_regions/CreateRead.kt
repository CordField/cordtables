package com.seedcompany.cordtables.components.tables.sc.field_regions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.field_regions.*
import com.seedcompany.cordtables.components.tables.sc.field_regions.ScFieldRegionsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.field_regions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldRegionsCreateReadRequest(
    val token: String? = null,
    val fieldRegion: fieldRegionInput,
)

data class ScFieldRegionsCreateReadResponse(
    val error: ErrorType,
    val fieldRegion: fieldRegion? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldRegionsCreateRead")
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
    @PostMapping("sc/field-regions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScFieldRegionsCreateReadRequest): ScFieldRegionsCreateReadResponse {

        val createResponse = create.createHandler(
            ScFieldRegionsCreateRequest(
                token = req.token,
                fieldRegion = req.fieldRegion
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScFieldRegionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScFieldRegionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScFieldRegionsCreateReadResponse(error = readResponse.error, fieldRegion = readResponse.fieldRegion)
    }
}
