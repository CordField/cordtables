package com.seedcompany.cordtables.components.tables.common.ticket_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonTicketGraphUpdateRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonTicketGraphUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonTicketGraphUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/ticket-graph/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonTicketGraphUpdateRequest): CommonTicketGraphUpdateResponse {

        if (req.token == null) return CommonTicketGraphUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonTicketGraphUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonTicketGraphUpdateResponse(ErrorType.MissingId)

        println(req)

        when (req.column) {

            "from_ticket" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_graph",
                        column = "from_ticket",
                        id = req.id,
                        value = req.value
                )
            }

            "to_ticket" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_graph",
                        column = "to_ticket",
                        id = req.id,
                        value = req.value
                )
            }


            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_graph",
                        column = "owning_person",
                        id = req.id,
                        value = req.value
                )
            }

            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.ticket_graph",
                        column = "owning_group",
                        id = req.id,
                        value = req.value
                )
            }
        }

        return CommonTicketGraphUpdateResponse(ErrorType.NoError)
    }
}
