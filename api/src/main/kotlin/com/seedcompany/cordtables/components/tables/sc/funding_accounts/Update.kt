package com.seedcompany.cordtables.components.tables.sc.funding_accounts

import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.fundingAccountInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFundingAccountsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScFundingAccountsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScFundingAccountsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-funding-accounts/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScFundingAccountsUpdateRequest): ScFundingAccountsUpdateResponse {

        if (req.token == null) return ScFundingAccountsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScFundingAccountsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScFundingAccountsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.funding_accounts",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
            "account_number" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.funding_accounts",
                    column = "account_number",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.funding_accounts",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.funding_accounts",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.funding_accounts",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScFundingAccountsUpdateResponse(ErrorType.NoError)
    }

}