package com.seedcompany.cordspringstencil.components.tables.grouprowaccess

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GroupRowAccessCreateRequest(
    val token: String? = null,
    val group: Int? = null,
    val tableName: String? = null,
    val row: Int? = null,
)

data class GroupRowAccessCreateReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller("GroupRowAccessCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("grouprowaccess/create")
    @ResponseBody
    fun createHandler(@RequestBody req: GroupRowAccessCreateRequest): GroupRowAccessCreateReturn {

        if (req.token == null) return GroupRowAccessCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupRowAccessCreateReturn(ErrorType.AdminOnly)

        if (req.group == null) return GroupRowAccessCreateReturn(ErrorType.InputMissingGroup)
        if (req.tableName == null || req.tableName.isEmpty()) return GroupRowAccessCreateReturn(ErrorType.InputMissingTableName)
        if (req.row == null) return GroupRowAccessCreateReturn(ErrorType.InputMissingRow)

        this.ds.connection.use { conn ->

            //language=SQL
            val checkRowStatement = conn.prepareStatement(
                """
                select exists(select id from public.group_row_access where group_id = ? and table_name = ? and row = ?)
            """.trimIndent()
            )

            checkRowStatement.setInt(1, req.group)
            checkRowStatement.setObject(2, req.tableName, java.sql.Types.OTHER)
            checkRowStatement.setInt(3, req.row)

            val result = checkRowStatement.executeQuery();

            if (result.next()) {
                val rowFound = result.getBoolean(1)

                if (rowFound) {
                    return GroupRowAccessCreateReturn(ErrorType.MembershipAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                        insert into public.group_row_access(group_id, table_name, row, created_by, modified_by) 
                            values(?, ?, ?,
                                (
                                  select person 
                                  from public.tokens 
                                  where token = ?
                                ),
                                (
                                  select person 
                                  from public.tokens 
                                  where token = ?
                                )
                        );
                        """.trimIndent()
                    )

                    statement.setInt(1, req.group)
                    statement.setObject(2, req.tableName, java.sql.Types.OTHER)
                    statement.setInt(3, req.row)
                    statement.setString(4, req.token)
                    statement.setString(5, req.token)

                    statement.execute()

                }
            }


        }

        return GroupRowAccessCreateReturn(ErrorType.NoError)
    }

}