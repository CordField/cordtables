package com.seedcompany.cordtables.components.tables.sc.field_zones

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFieldZonesUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScFieldZonesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFieldZonesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-field-zones/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScFieldZonesUpdateRequest): ScFieldZonesUpdateResponse {

        if (req.token == null) return ScFieldZonesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScFieldZonesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScFieldZonesUpdateResponse(ErrorType.MissingId)


        when (req.column) {
//            "neo4j_id" -> {
//                util.updateField(
//                    token = req.token,
//                    table = "sc.field_zones",
//                    column = "neo4j_id",
//                    id = req.id,
//                    value = req.value,
//                )
//            }
            "director" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_zones",
                    column = "director",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_zones",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_zones",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.field_zones",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScFieldZonesUpdateResponse(ErrorType.NoError)
    }

}
