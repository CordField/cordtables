package com.seedcompany.cordtables.components.tables.sc.budget_records

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

data class ScBudgetRecordsDeleteRequest(
    val id: Int,
    val token: String?,
)

data class ScBudgetRecordsDeleteResponse(
    val error: ErrorType,
    val id: Int?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScBudgetRecordsDelete")
class Delete(
    @Autowired
    val util: Utility,
    @Autowired
    val ds: DataSource,
) {
  
    @PostMapping("sc/budget-records/delete")
    @ResponseBody
    fun deleteHandler(@RequestBody req: ScBudgetRecordsDeleteRequest): ScBudgetRecordsDeleteResponse {

        if (req.token == null) return ScBudgetRecordsDeleteResponse(ErrorType.TokenNotFound, null)
        if(!util.userHasDeletePermission(req.token, "sc.budget-records"))
            return ScBudgetRecordsDeleteResponse(ErrorType.DoesNotHaveDeletePermission, null)

        var deletedBudgetRecordExId: Int? = null

        this.ds.connection.use { conn ->
            try {

                val deleteStatement = conn.prepareCall(
                    "delete from sc.budget_records where id = ? returning id"
                )
                deleteStatement.setInt(1, req.id)

                deleteStatement.setInt(1,req.id)


                val deleteStatementResult = deleteStatement.executeQuery()

                if (deleteStatementResult.next()) {
                    deletedBudgetRecordExId  = deleteStatementResult.getInt("id")
                }
            }
            catch (e:SQLException ){
                println(e.message)

                return ScBudgetRecordsDeleteResponse(ErrorType.SQLDeleteError, null)
            }
        }
        return ScBudgetRecordsDeleteResponse(ErrorType.NoError,deletedBudgetRecordExId)
    }
}
