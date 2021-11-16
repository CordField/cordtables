package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.ScProductScriptureReferencesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScProductScriptureReferencesDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScProductScriptureReferencesDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("ScProductScriptureReferencesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-product-scripture-references/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScProductScriptureReferencesDeleteRequest): ScProductScriptureReferencesDeleteResponse {

        if (req.token == null) return ScProductScriptureReferencesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.product_scripture_references"))
            return ScProductScriptureReferencesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.product_scripture_references where id = ? returning id"
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

                return ScProductScriptureReferencesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScProductScriptureReferencesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}