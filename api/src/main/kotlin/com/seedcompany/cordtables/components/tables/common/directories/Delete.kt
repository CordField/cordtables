package com.seedcompany.cordtables.components.tables.common.directories


import com.seedcompany.cordtables.components.tables.common.directories.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.directories.CommonDirectoriesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonDirectoriesDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonDirectoriesDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonDirectoriesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val commonDelete: CommonDelete,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-directories/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonDirectoriesDeleteRequest): CommonDirectoriesDeleteResponse {

        if (req.token == null) return CommonDirectoriesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.directories"))
            return CommonDirectoriesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.directories where id = ? returning id"
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

                return CommonDirectoriesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        val deleteResponse = commonDelete.deleteHandler(
            CommonDirectoriesDeleteRequest(
                token = req.token,
                id = req.id
            )
        )

        if(deleteResponse.error != ErrorType.NoError) {
            return CommonDirectoriesDeleteResponse(deleteResponse.error, deleteResponse.id)
        }

        return CommonDirectoriesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}