package com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people

import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_engagement_people.ScGlobalPartnerEngagementPeopleDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScGlobalPartnerEngagementPeopleDeleteRequest(
    val id: String,
    val token: String?,
)

data class ScGlobalPartnerEngagementPeopleDeleteResponse(
    val error: ErrorType,
    val id: String?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScGlobalPartnerEngagementPeopleDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/global-partner-engagement-people/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScGlobalPartnerEngagementPeopleDeleteRequest): ScGlobalPartnerEngagementPeopleDeleteResponse {

        if (req.token == null) return ScGlobalPartnerEngagementPeopleDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.global_partner_engagement_people"))
            return ScGlobalPartnerEngagementPeopleDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: String? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.global_partner_engagement_people where id = ? returning id"
                )
                deleteStatement.setString(1, req.id)

                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedLocationExId  = deleteStatementResult.getString("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScGlobalPartnerEngagementPeopleDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScGlobalPartnerEngagementPeopleDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
