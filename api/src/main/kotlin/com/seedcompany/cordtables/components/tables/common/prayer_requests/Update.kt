package com.seedcompany.cordtables.components.tables.common.prayer_requests

import com.seedcompany.cordtables.components.tables.common.prayer_requests.CommonPrayerRequestsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.prayer_requests.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.prayer_requests.CommonPrayerRequestsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.prayer_requests.prayerRequestInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPrayerRequestsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPrayerRequestsUpdateResponse(
    val error: ErrorType,
)



@Controller("CommonPrayerRequestsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-prayer-requests/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonPrayerRequestsUpdateRequest): CommonPrayerRequestsUpdateResponse {

        if (req.token == null) return CommonPrayerRequestsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonPrayerRequestsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonPrayerRequestsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "parent" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_requests",
                    column = "parent",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "content" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_requests",
                    column = "content",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_requests",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.prayer_requests",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonPrayerRequestsUpdateResponse(ErrorType.NoError)
    }

}