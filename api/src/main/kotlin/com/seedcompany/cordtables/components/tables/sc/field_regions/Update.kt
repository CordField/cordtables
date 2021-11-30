package com.seedcompany.cordtables.components.tables.sc.field_regions

import com.seedcompany.cordtables.components.tables.sc.field_regions.ScFieldRegionsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.field_regions.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.field_regions.fieldRegionInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldRegionsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScFieldRegionsUpdateResponse(
    val error: ErrorType,
)



@Controller("ScFieldRegionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-field-regions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScFieldRegionsUpdateRequest): ScFieldRegionsUpdateResponse {

        if (req.token == null) return ScFieldRegionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScFieldRegionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScFieldRegionsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_regions",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "director" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_regions",
                    column = "director",
                    id = req.id,
                    value = req.value,
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_regions",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_regions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_regions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return ScFieldRegionsUpdateResponse(ErrorType.NoError)
    }

}