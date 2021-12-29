package com.seedcompany.cordtables.components.tables.common.posts

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

data class CommonPostsDeleteRequest(
        val id: Int,
        val token: String?,
)

data class CommonPostsDeleteResponse(
        val error: ErrorType,
        val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPostsDelete")
class Delete(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common/posts/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonPostsDeleteRequest): CommonPostsDeleteResponse {

        if (req.token == null) return CommonPostsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.posts"))
            return CommonPostsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedThreadId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                        "delete from common.posts where id = ? returning id"
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

                return CommonPostsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return CommonPostsDeleteResponse(ErrorType.NoError,deletedThreadId)
    }
}
