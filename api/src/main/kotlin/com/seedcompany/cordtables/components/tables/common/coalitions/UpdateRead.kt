package com.seedcompany.cordtables.components.tables.common.coalitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.coalitions.CommonCoalitionsReadRequest
import com.seedcompany.cordtables.components.tables.common.coalitions.CommonCoalitionsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.coalitions.coalition
import com.seedcompany.cordtables.components.tables.common.coalitions.coalitionInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonCoalitionsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonCoalitionsUpdateReadResponse(
    val error: ErrorType,
    val coalition: coalition? = null,
)


@Controller("CommonCoalitionsUpdateRead")
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
    @PostMapping("common-coalitions/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonCoalitionsUpdateReadRequest): CommonCoalitionsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonCoalitionsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonCoalitionsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonCoalitionsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return CommonCoalitionsUpdateReadResponse(error = readResponse.error, readResponse.coalition)
    }
}