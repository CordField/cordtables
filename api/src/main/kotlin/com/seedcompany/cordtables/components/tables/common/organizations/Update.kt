package com.seedcompany.cordtables.components.tables.common.organizations

import com.seedcompany.cordtables.common.CommonSensitivity
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

data class CommonOrganizationsUpdateRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonOrganizationsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonOrganizationsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-organizations/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonOrganizationsUpdateRequest): CommonOrganizationsUpdateResponse {

        if (req.token == null) return CommonOrganizationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonOrganizationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonOrganizationsUpdateResponse(ErrorType.MissingId)

        if (req.column.equals("sensitivity") && req.value != null && !enumContains<CommonSensitivity>(req.value as String)) {
            return CommonOrganizationsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }

        when (req.column) {
            "name" -> {
                util.updateField(
                        token = req.token,
                        table = "common.organizations",
                        column = "name",
                        id = req.id,
                        value = req.value,
                )
            }

            "sensitivity" -> {
                util.updateField(
                        token = req.token,
                        table = "common.organizations",
                        column = "sensitivity",
                        id = req.id,
                        value = req.value,
                        cast = "::common.sensitivity"
                )
            }

            "primary_location" -> {
                util.updateField(
                        token = req.token,
                        table = "common.organizations",
                        column = "primary_location",
                        id = req.id,
                        value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.organizations",
                        column = "owning_person",
                        id = req.id,
                        value = req.value,
                )
            }

            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.organizations",
                        column = "owning_group",
                        id = req.id,
                        value = req.value,
                )
            }
        }

        return CommonOrganizationsUpdateResponse(ErrorType.NoError)
    }
}