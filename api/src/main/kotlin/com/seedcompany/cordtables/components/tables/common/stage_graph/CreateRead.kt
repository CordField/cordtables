package com.seedcompany.cordtables.components.tables.common.stage_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_graph.*
import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphCreateRequest
import com.seedcompany.cordtables.components.tables.common.stage_graph.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageGraphCreateReadRequest(
    val token: String? = null,
    val stageGraph: stageGraphInput,
)

data class CommonStageGraphCreateReadResponse(
    val error: ErrorType,
    val stageGraph: stageGraph? = null,
)


@Controller("CommonStageGraphCreateRead")
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
    @PostMapping("common-stage-graph/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonStageGraphCreateReadRequest): CommonStageGraphCreateReadResponse {

        val createResponse = create.createHandler(
            CommonStageGraphCreateRequest(
                token = req.token,
                stageGraph = req.stageGraph
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonStageGraphCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStageGraphReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonStageGraphCreateReadResponse(error = readResponse.error, stageGraph = readResponse.stageGraph)
    }
}