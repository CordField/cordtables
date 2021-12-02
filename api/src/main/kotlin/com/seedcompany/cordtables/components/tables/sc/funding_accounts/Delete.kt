package com.seedcompany.cordtables.components.tables.sc.funding_accounts

import com.seedcompany.cordtables.components.tables.sc.funding_accounts.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.funding_accounts.ScFundingAccountsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScFundingAccountsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScFundingAccountsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScFundingAccountsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-funding-accounts/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScFundingAccountsDeleteRequest): ScFundingAccountsDeleteResponse {

        if (req.token == null) return ScFundingAccountsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.funding_accounts"))
            return ScFundingAccountsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.funding_accounts where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScFundingAccountsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScFundingAccountsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}