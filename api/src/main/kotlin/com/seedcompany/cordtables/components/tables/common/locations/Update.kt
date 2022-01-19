package com.seedcompany.cordtables.components.tables.common.locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonLocationsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonLocationsUpdateRequest): CommonLocationsUpdateResponse {

        if (req.token == null) return CommonLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonLocationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonLocationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "sensitivity" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "sensitivity",
                    id = req.id,
                    value = req.value,
                    cast = "::common.sensitivity"
                )
            }
            "type" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "type",
                    id = req.id,
                    value = req.value,
                    cast = "::common.location_type"
                )
            }
            "iso_alpha3" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "iso_alpha3",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.locations",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return CommonLocationsUpdateResponse(ErrorType.NoError)
    }

}
