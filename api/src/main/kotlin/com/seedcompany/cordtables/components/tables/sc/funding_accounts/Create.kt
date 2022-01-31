package com.seedcompany.cordtables.components.tables.sc.funding_accounts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.fundingAccountInput
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.Read
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScFundingAccountsCreateRequest(
    val token: String? = null,
    val fundingAccount: fundingAccountInput,
)

data class ScFundingAccountsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFundingAccountsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc/funding-accounts/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScFundingAccountsCreateRequest): ScFundingAccountsCreateResponse {

        // if (req.fundingAccount.name == null) return ScFundingAccountsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.funding_accounts(account_number, name,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.fundingAccount.account_number,
            req.fundingAccount.name,
            req.token,
            req.token,
            req.token,
            util.adminGroupId()
        )

        return ScFundingAccountsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
