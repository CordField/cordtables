package com.seedcompany.cordtables.components.tables.common.work_estimates

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkEstimateUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkEstimateUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-work-estimates/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonWorkEstimateUpdateRequest): CommonWorkEstimateUpdateResponse {

        if (req.token == null) return CommonWorkEstimateUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonWorkEstimateUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonWorkEstimateUpdateResponse(ErrorType.MissingId)

        println(req)

        when (req.column) {

            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "person",
                    id = req.id,
                    value = req.value
                )
            }

            "hours" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "hours",
                    id = req.id,
                    value = req.value,
                    cast = "::integer"
                )
            }

            "minutes" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "minutes",
                    id = req.id,
                    value = req.value,
                    cast = "::integer"
                )
            }


            "comment" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "comment",
                    id = req.id,
                    value = req.value,
                    cast = "::text"
                )
            }


            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_estimates",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return CommonWorkEstimateUpdateResponse(ErrorType.NoError)
    }
}
