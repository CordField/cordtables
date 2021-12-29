package com.seedcompany.cordtables.components.tables.sc.funding_accounts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsReadRequest
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.fundingAccount
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.fundingAccountInput
import com.seedcompany.cordtables.components.tables.sc.locations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFundingAccountsUpdateReadRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
    val fundingAccount: fundingAccountInput? = null,
)

data class ScFundingAccountsUpdateReadResponse(
    val error: ErrorType,
    val fundingAccount: fundingAccount? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFundingAccountsUpdateRead")
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
    @PostMapping("sc-funding-accounts/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: ScFundingAccountsUpdateReadRequest): ScFundingAccountsUpdateReadResponse {

        val updateResponse = update.updateHandler(
            ScFundingAccountsUpdateRequest(
                token = req.token,
                column = req.column,
                id = req.id,
                value = req.value,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return ScFundingAccountsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            ScFundingAccountsReadRequest(
                token = req.token,
                id = req.id!!
            )
        )

        return ScFundingAccountsUpdateReadResponse(error = readResponse.error, readResponse.fundingAccount)
    }
}