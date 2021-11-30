package com.seedcompany.cordtables.components.tables.sc.global_partner_assessments

import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.global_partner_assessments.ScGlobalPartnerAssessmentsDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScGlobalPartnerAssessmentsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScGlobalPartnerAssessmentsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("ScGlobalPartnerAssessmentsDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-global-partner-assessments/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScGlobalPartnerAssessmentsDeleteRequest): ScGlobalPartnerAssessmentsDeleteResponse {

        if (req.token == null) return ScGlobalPartnerAssessmentsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.global_partner_assessments"))
            return ScGlobalPartnerAssessmentsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.global_partner_assessments where id = ? returning id"
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

                return ScGlobalPartnerAssessmentsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScGlobalPartnerAssessmentsDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}