package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.ScGlobalPartnerTransitionsReadRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.ScGlobalPartnerTransitionsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.globalPartnerTransition
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.globalPartnerTransitionInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerTransitionsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerTransitionsUpdateReadResponse(
    val error: ErrorType,
    val globalPartnerTransition: globalPartnerTransition? = null,
)


@Controller("ScGlobalPartnerTransitionsUpdateRead")
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
    @PostMapping("sc-global-partner-transitions/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScGlobalPartnerTransitionsUpdateReadRequest): ScGlobalPartnerTransitionsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScGlobalPartnerTransitionsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerTransitionsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerTransitionsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScGlobalPartnerTransitionsUpdateReadResponse(error = readResponse.error, readResponse.globalPartnerTransition)
    }
}