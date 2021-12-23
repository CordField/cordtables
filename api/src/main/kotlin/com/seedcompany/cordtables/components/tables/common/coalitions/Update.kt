package com.seedcompany.cordtables.components.tables.common.coalitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonCoalitionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonCoalitionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/coalitions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonCoalitionsUpdateRequest): CommonCoalitionsUpdateResponse {

        if (req.token == null) return CommonCoalitionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonCoalitionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonCoalitionsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalitions",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalitions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.coalitions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonCoalitionsUpdateResponse(ErrorType.NoError)
    }

}
