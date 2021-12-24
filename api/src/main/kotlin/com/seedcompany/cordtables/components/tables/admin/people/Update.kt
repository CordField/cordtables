package com.seedcompany.cordtables.components.tables.admin.people

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class AdminPeopleUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class AdminPeopleUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("AdminPeopleUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("admin/people/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: AdminPeopleUpdateRequest): AdminPeopleUpdateResponse {

        if (req.token == null) return AdminPeopleUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return AdminPeopleUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return AdminPeopleUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "about" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "about",
                    id = req.id,
                    value = req.value,
                )
            }
            "phone" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "phone",
                    id = req.id,
                    value = req.value,
                )
            }
            "picture" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "picture",
                    id = req.id,
                    value = req.value,
                )
            }
            "private_first_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "private_first_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "private_last_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "private_last_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "public_first_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "public_first_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "public_last_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "public_last_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "primary_location" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "primary_location",
                    id = req.id,
                    value = req.value,
                )
            }
            "private_full_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "private_full_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "public_full_name" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "public_full_name",
                    id = req.id,
                    value = req.value,
                )
            }
            "sensitivity_clearance" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "sensitivity_clearance",
                    id = req.id,
                    value = req.value,
                    cast = "::common.sensitivity"
                )
            }
            "time_zone" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "timezone",
                    id = req.id,
                    value = req.value,
                )
            }
            "title" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "title",
                    id = req.id,
                    value = req.value,
                )
            }
            "status" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "status",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "admin.people",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return AdminPeopleUpdateResponse(ErrorType.NoError)
    }

}
