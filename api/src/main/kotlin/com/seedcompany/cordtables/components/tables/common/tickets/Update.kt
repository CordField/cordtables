package com.seedcompany.cordtables.components.tables.common.tickets

import com.seedcompany.cordtables.common.CommonTicketStatus
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

data class CommonTicketsUpdateRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonOrganizationsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-tickets/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonTicketsUpdateRequest): CommonOrganizationsUpdateResponse {

        if (req.token == null) return CommonOrganizationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonOrganizationsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonOrganizationsUpdateResponse(ErrorType.MissingId)

        if (req.column.equals("ticket_status") && req.value != null && !enumContains<CommonTicketStatus>(req.value as String)) {
            return CommonOrganizationsUpdateResponse(
                    error = ErrorType.ValueDoesNotMap
            )
        }
        println(req)

        when (req.column) {

            "ticket_status" -> {
                util.updateField(
                        token = req.token,
                        table = "common.tickets",
                        column = "ticket_status",
                        id = req.id,
                        value = req.value,
                        cast = "::common.ticket_status"
                )
            }

            "parent" -> {
                util.updateField(
                        token = req.token,
                        table = "common.tickets",
                        column = "parent",
                        id = req.id,
                        value = req.value,
                        cast = "::uuid"
                )
            }

            "content" -> {
                util.updateField(
                        token = req.token,
                        table = "common.tickets",
                        column = "content",
                        id = req.id,
                        value = req.value,
                )
            }


            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.tickets",
                        column = "owning_person",
                        id = req.id,
                        value = req.value,
                        cast = "::uuid"
                )
            }

            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.tickets",
                        column = "owning_group",
                        id = req.id,
                        value = req.value,
                        cast = "::uuid"
                )
            }
        }

        return CommonOrganizationsUpdateResponse(ErrorType.NoError)
    }
}
