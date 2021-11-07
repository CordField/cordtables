package com.seedcompany.cordtables.components.tables.sc.global_partner_engagements

import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagements.ScGlobalPartnerEngagementsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScGlobalPartnerEngagementsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScGlobalPartnerEngagementsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScGlobalPartnerEngagementsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-engagement/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScGlobalPartnerEngagementsDeleteRequest): ScGlobalPartnerEngagementsDeleteResponse {

        if (req.token == null) return ScGlobalPartnerEngagementsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.global_partner_engagements"))
            return ScGlobalPartnerEngagementsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.global_partner_engagements where id = ? returning id"
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

                return ScGlobalPartnerEngagementsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScGlobalPartnerEngagementsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}