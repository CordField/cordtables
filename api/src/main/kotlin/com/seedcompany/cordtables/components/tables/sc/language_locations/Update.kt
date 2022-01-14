package com.seedcompany.cordtables.components.tables.sc.language_locations

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLanguageLocationsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScLanguageLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLanguageLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/language-locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScLanguageLocationsUpdateRequest): ScLanguageLocationsUpdateResponse {

      if (req.token == null) return ScLanguageLocationsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return ScLanguageLocationsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return ScLanguageLocationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScLanguageLocationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "language" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_locations",
                    column = "language",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "location" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_locations",
                    column = "location",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_locations",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.language_locations",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScLanguageLocationsUpdateResponse(ErrorType.NoError)
    }

}
