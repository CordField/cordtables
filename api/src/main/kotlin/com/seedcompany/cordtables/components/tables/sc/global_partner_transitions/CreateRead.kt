package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.*
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.ScGlobalPartnerTransitionsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.global_partner_transitions.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerTransitionsCreateReadRequest(
    val token: String? = null,
    val globalPartnerTransition: globalPartnerTransitionInput,
)

data class ScGlobalPartnerTransitionsCreateReadResponse(
    val error: ErrorType,
    val globalPartnerTransition: globalPartnerTransition? = null,
)


@Controller("ScGlobalPartnerTransitionsCreateRead")
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
    @PostMapping("sc-global-partner-transitions/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScGlobalPartnerTransitionsCreateReadRequest): ScGlobalPartnerTransitionsCreateReadResponse {

        val createResponse = create.createHandler(
            ScGlobalPartnerTransitionsCreateRequest(
                token = req.token,
                globalPartnerTransition = req.globalPartnerTransition
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScGlobalPartnerTransitionsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScGlobalPartnerTransitionsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScGlobalPartnerTransitionsCreateReadResponse(error = readResponse.error, globalPartnerTransition = readResponse.globalPartnerTransition)
    }
}