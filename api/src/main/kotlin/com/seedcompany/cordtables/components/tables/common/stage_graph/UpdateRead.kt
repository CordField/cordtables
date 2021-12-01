package com.seedcompany.cordtables.components.tables.common.stage_graph

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphReadRequest
import com.seedcompany.cordtables.components.tables.common.stage_graph.CommonStageGraphUpdateRequest
import com.seedcompany.cordtables.components.tables.common.stage_graph.stageGraph
import com.seedcompany.cordtables.components.tables.common.stage_graph.stageGraphInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonStageGraphUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonStageGraphUpdateReadResponse(
    val error: ErrorType,
    val stageGraph: stageGraph? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonStageGraphUpdateRead")
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
    @PostMapping("common-stage-graph/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonStageGraphUpdateReadRequest): CommonStageGraphUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonStageGraphUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonStageGraphUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonStageGraphReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonStageGraphUpdateReadResponse(error = readResponse.error, readResponse.stageGraph)
    }
}