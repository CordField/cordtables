package com.seedcompany.cordtables.components.tables.sc.global_partner_transitions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerTransitionsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerTransitionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerTransitionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-transitions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScGlobalPartnerTransitionsUpdateRequest): ScGlobalPartnerTransitionsUpdateResponse {

        if (req.token == null) return ScGlobalPartnerTransitionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScGlobalPartnerTransitionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScGlobalPartnerTransitionsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_transitions",
                    column = "organization",
                    id = req.id,
                    value = req.value
                )
            }
            "transition_type" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_transitions",
                    column = "transition_type",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.global_partner_transition_options"
                )
            }
            "effective_date" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_transitions",
                    column = "effective_date",
                    id = req.id,
                    value = req.value,
                    cast = "::timestamp"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_transitions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_transitions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScGlobalPartnerTransitionsUpdateResponse(ErrorType.NoError)
    }

}
