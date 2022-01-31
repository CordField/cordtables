package com.seedcompany.cordtables.components.tables.sc.locations

import com.seedcompany.cordtables.components.tables.common.locations.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScLocationsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScLocationsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScLocationsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val commonUpdate: CommonUpdate,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/locations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScLocationsUpdateRequest): ScLocationsUpdateResponse {

        if (req.token == null) return ScLocationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScLocationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScLocationsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
          "name" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
              column = "name",
              id = req.id,
              value = req.value
            )
          }
          "funding_account" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
              column = "funding_account",
              id = req.id,
              value = req.value
            )
          }
          "default_region" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
              column = "default_region",
              id = req.id,
              value = req.value
            )
          }
          "iso_alpha_3" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
              column = "iso_alpha_3",
              id = req.id,
              value = req.value
            )
          }
          "type" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
              column = "type",
              id = req.id,
              value = req.value,
              cast = "::common.location_type"
            )
          }
          "owning_person" -> {
            util.updateField(
              token = req.token,
              table = "sc.locations",
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

      return ScLocationsUpdateResponse(ErrorType.NoError)
    }

}
