package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphReadRequest
import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphUpdateRequest
import com.seedcompany.cordtables.components.tables.common.people_graph.peopleGraph
import com.seedcompany.cordtables.components.tables.common.people_graph.peopleGraphInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleGraphUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonPeopleGraphUpdateReadResponse(
    val error: ErrorType,
    val peopleGraph: peopleGraph? = null,
)


@Controller("CommonPeopleGraphUpdateRead")
class UpdateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    @PostMapping("common-people-graph/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonPeopleGraphUpdateReadRequest): CommonPeopleGraphUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonPeopleGraphUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonPeopleGraphUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPeopleGraphReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonPeopleGraphUpdateReadResponse(error = readResponse.error, readResponse.peopleGraph)
    }
}