package com.seedcompany.cordtables.components.tables.common.work_records

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonWorkRecordUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonWorkRecordUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkRecordsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/work-records/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonWorkRecordUpdateRequest): CommonWorkRecordUpdateResponse {

        if (req.token == null) return CommonWorkRecordUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonWorkRecordUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonWorkRecordUpdateResponse(ErrorType.MissingId)

        println(req)

        when (req.column) {

            "person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "ticket" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "ticket",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }

            "hours" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "hours",
                    id = req.id,
                    value = req.value,
                    cast = "::integer"
                )
            }
            "minutes" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "minutes",
                    id = req.id,
                    value = req.value,
                    cast = "::integer"
                )
            }
            "comment" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "comment",
                    id = req.id,
                    value = req.value,
                    cast = "::text"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.work_records",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return CommonWorkRecordUpdateResponse(ErrorType.NoError)
    }
}
