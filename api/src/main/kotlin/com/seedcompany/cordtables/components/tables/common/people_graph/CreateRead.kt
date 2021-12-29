package com.seedcompany.cordtables.components.tables.common.people_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.people_graph.*
import com.seedcompany.cordtables.components.tables.common.people_graph.CommonPeopleGraphCreateRequest
import com.seedcompany.cordtables.components.tables.common.people_graph.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPeopleGraphCreateReadRequest(
    val token: String? = null,
    val peopleGraph: peopleGraphInput,
)

data class CommonPeopleGraphCreateReadResponse(
    val error: ErrorType,
    val peopleGraph: peopleGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPeopleGraphCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("common-people-graph/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonPeopleGraphCreateReadRequest): CommonPeopleGraphCreateReadResponse {

        val createResponse = create.createHandler(
            CommonPeopleGraphCreateRequest(
                token = req.token,
                peopleGraph = req.peopleGraph
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonPeopleGraphCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonPeopleGraphReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonPeopleGraphCreateReadResponse(error = readResponse.error, peopleGraph = readResponse.peopleGraph)
    }
}