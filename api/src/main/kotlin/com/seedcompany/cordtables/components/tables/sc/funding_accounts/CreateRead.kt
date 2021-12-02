package com.seedcompany.cordtables.components.tables.sc.funding_accounts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.*
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsCreateRequest
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFundingAccountsCreateReadRequest(
    val token: String? = null,
    val fundingAccount: fundingAccountInput,
)

data class ScFundingAccountsCreateReadResponse(
    val error: ErrorType,
    val fundingAccount: fundingAccount? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFundingAccountsCreateRead")
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
    @PostMapping("sc-funding-accounts/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: ScFundingAccountsCreateReadRequest): ScFundingAccountsCreateReadResponse {

        val createResponse = create.createHandler(
            ScFundingAccountsCreateRequest(
                token = req.token,
                fundingAccount = req.fundingAccount
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return ScFundingAccountsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            ScFundingAccountsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return ScFundingAccountsCreateReadResponse(error = readResponse.error, fundingAccount = readResponse.fundingAccount)
    }
}