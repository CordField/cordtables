package com.seedcompany.cordtables.components.tables.sc.languages

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

data class ScLanguagesDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScLanguagesDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("ScLanguagesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-languages/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScLanguagesDeleteRequest): ScLanguagesDeleteResponse {

        if (req.token == null) return ScLanguagesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.languages"))
            return ScLanguagesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLanguageExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.languages where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                  deletedLanguageExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScLanguagesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return ScLanguagesDeleteResponse(ErrorType.NoError,deletedLanguageExId)
    }
}