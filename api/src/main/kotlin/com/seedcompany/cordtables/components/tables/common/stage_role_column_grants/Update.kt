package com.seedcompany.cordtables.components.tables.common.stage_role_column_grants

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageRoleColumnGrantsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStageRoleColumnGrantsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageRoleColumnGrantsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/stage-role-column-grants/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonStageRoleColumnGrantsUpdateRequest): CommonStageRoleColumnGrantsUpdateResponse {

        if (req.token == null) return CommonStageRoleColumnGrantsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonStageRoleColumnGrantsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonStageRoleColumnGrantsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "stage" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "stage",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "role" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "role",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "table_name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "table_name",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.table_name"
                )
            }

            "column_name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "column_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "access_level" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "access_level",
                    id = req.id,
                    value = req.value,
                    cast = "::admin.access_level"
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.stage_role_column_grants",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return CommonStageRoleColumnGrantsUpdateResponse(ErrorType.NoError)
    }

}
