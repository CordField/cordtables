package com.seedcompany.cordtables.components.tables.common.stages

import com.seedcompany.cordtables.components.tables.common.stages.Delete as CommonDelete
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.stages.CommonStagesDeleteRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource

data class CommonStagesDeleteRequest(
    val id: Int,
    val token: String?,
)

data class CommonStagesDeleteResponse(
    val error: ErrorType,
    val id: Int?
)


@Controller("CommonStagesDelete")
class Delete(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-stages/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: CommonStagesDeleteRequest): CommonStagesDeleteResponse {

        if (req.token == null) return CommonStagesDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "common.stages"))
            return CommonStagesDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        println("req: $req")
        var deletedLocationExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from common.stages where id = ? returning id"
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

                return CommonStagesDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }

        return CommonStagesDeleteResponse(ErrorType.NoError,deletedLocationExId)
    }
}