package com.seedcompany.cordtables.components.tables.sc.known_languages_by_person

import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.known_languages_by_person.ScKnownLanguagesByPersonDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class ScKnownLanguagesByPersonDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScKnownLanguagesByPersonDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScKnownLanguagesByPersonDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/known-languages-by-person/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScKnownLanguagesByPersonDeleteRequest): ScKnownLanguagesByPersonDeleteResponse {

        if (req.token == null) return ScKnownLanguagesByPersonDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.known_languages_by_person"))
            return ScKnownLanguagesByPersonDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.known_languages_by_person where id = ? returning id"
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

                return ScKnownLanguagesByPersonDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return ScKnownLanguagesByPersonDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}
