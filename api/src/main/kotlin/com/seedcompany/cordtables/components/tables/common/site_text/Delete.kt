package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonSiteTextDeleteRequest(
        val id: Int,
        val token: String?,
)

data class CommonSiteTextDeleteResponse(
        val error: ErrorType,
        val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonSiteTextDelete")
class Delete(
        @Autowired
        val util: Utility,
        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-site-texts/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonSiteTextDeleteRequest): CommonSiteTextDeleteResponse {

        if (req.token == null) return CommonSiteTextDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.locations"))
            return CommonSiteTextDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.site_text where id = ? returning id"
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

                return CommonSiteTextDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonSiteTextDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}