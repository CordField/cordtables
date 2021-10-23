package com.seedcompany.cordtables.components.tables.languageex

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
import kotlin.reflect.full.memberProperties


data class DeleteLanguageExResponse(
    val error: ErrorType,
    val id: Int?
)
data class DeleteLanguageExRequest(
    val id: Int,
    val token: String?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("LanguageExDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-languages/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: DeleteLanguageExRequest): DeleteLanguageExResponse {

        if (req.token == null) return DeleteLanguageExResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.languages_ex"))
            return DeleteLanguageExResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLanguageExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.languages_ex where id = ? returning id"
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

                return DeleteLanguageExResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return DeleteLanguageExResponse(ErrorType.NoError,deletedLanguageExId)
    }
}