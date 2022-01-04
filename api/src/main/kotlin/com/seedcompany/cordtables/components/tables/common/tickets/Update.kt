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
        val ticket: CommonTicketsInput
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
    @PostMapping("common/tickets/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonTicketsUpdateRequest): CommonOrganizationsUpdateResponse {

        if (req.token == null) return CommonOrganizationsUpdateResponse(ErrorType.TokenNotFound)
        if (req.id == null) return CommonOrganizationsUpdateResponse(ErrorType.MissingId)

            util.updateField(
              token = req.token,
              table = "common.tickets",
              column = "title",
              id = req.id,
              value = req.ticket.title
            )

// <<<<<<< HEAD
//         when (req.column) {

//             "ticket_status" -> {
//                 util.updateField(
//                         token = req.token,
//                         table = "common.tickets",
//                         column = "ticket_status",
//                         id = req.id,
//                         value = req.value,
//                         cast = "::common.ticket_status"
//                 )
//             }

//             "parent" -> {
//                 util.updateField(
//                         token = req.token,
//                         table = "common.tickets",
//                         column = "parent",
//                         id = req.id,
//                         value = req.value,
//                         cast = "::uuid"
//                 )
//             }

//             "content" -> {
//                 util.updateField(
//                         token = req.token,
//                         table = "common.tickets",
//                         column = "content",
//                         id = req.id,
//                         value = req.value,
//                 )
//             }
// =======
//             util.updateField(
//               token = req.token,
//               table = "common.tickets",
//               column = "ticket_status",
//               id = req.id,
//               value = req.ticket.ticket_status,
//               cast = "::common.ticket_status"
//             )
// >>>>>>> a18aa452d9d4f0d8cdce0ff1253d613e4b4f05cc

//             util.updateField(
//               token = req.token,
//               table = "common.tickets",
//               column = "parent",
//               id = req.id,
//               value = req.ticket.parent,
//               cast = "::integer"
//             )

// <<<<<<< HEAD
//             "owning_person" -> {
//                 util.updateField(
//                         token = req.token,
//                         table = "common.tickets",
//                         column = "owning_person",
//                         id = req.id,
//                         value = req.value,
//                         cast = "::uuid"
//                 )
//             }

//             "owning_group" -> {
//                 util.updateField(
//                         token = req.token,
//                         table = "common.tickets",
//                         column = "owning_group",
//                         id = req.id,
//                         value = req.value,
//                         cast = "::uuid"
//                 )
//             }
//         }
// =======
//             util.updateField(
//               token = req.token,
//               table = "common.tickets",
//               column = "content",
//               id = req.id,
//               value = req.ticket.content,
//             )

// >>>>>>> a18aa452d9d4f0d8cdce0ff1253d613e4b4f05cc

        return CommonOrganizationsUpdateResponse(ErrorType.NoError)
    }
}
