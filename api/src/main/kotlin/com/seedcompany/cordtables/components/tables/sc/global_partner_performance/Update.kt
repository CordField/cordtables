package com.seedcompany.cordtables.components.tables.sc.global_partner_performance

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScGlobalPartnerPerformanceUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScGlobalPartnerPerformanceUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerPerformanceUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/global-partner-performance/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScGlobalPartnerPerformanceUpdateRequest): ScGlobalPartnerPerformanceUpdateResponse {

        if (req.token == null) return ScGlobalPartnerPerformanceUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScGlobalPartnerPerformanceUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScGlobalPartnerPerformanceUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "organization" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "organization",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "reporting_performance" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "reporting_performance",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.partner_performance_options"
                )
            }
            "financial_performance" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "financial_performance",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.partner_performance_options"
                )
            }
            "translation_performance" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "translation_performance",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.partner_performance_options"
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.global_partner_performance",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScGlobalPartnerPerformanceUpdateResponse(ErrorType.NoError)
    }

}
