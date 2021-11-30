package com.seedcompany.cordtables.components.tables.common.threads

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

data class CommonThreadsDeleteRequest(
        val id: Int,
        val token: String?,
)

data class CommonThreadsDeleteResponse(
        val error: ErrorType,
        val id: Int?
)


@Controller("CommonThreadsDelete")
class Delete(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-threads/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonThreadsDeleteRequest): CommonThreadsDeleteResponse {

        if (req.token == null) return CommonThreadsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.threads"))
            return CommonThreadsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedThreadId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.threads where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedThreadId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return CommonThreadsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonThreadsDeleteResponse(ErrorType.NoError,deletedThreadId)
    }
}