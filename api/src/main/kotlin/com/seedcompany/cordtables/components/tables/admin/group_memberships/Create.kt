package com.seedcompany.cordtables.components.tables.admin.group_memberships

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class GroupMembershipCreateRequest(
    val token: String? = null,
    val group: Int? = null,
    val person: Int? = null,
)

data class GroupMembershipCreateReturn(
    val error: ErrorType,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("GroupMembershipsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {

    @PostMapping("groupmemberships/create")
    @ResponseBody
    fun createHandler(@RequestBody req: GroupMembershipCreateRequest): GroupMembershipCreateReturn {

        if (req.token == null) return GroupMembershipCreateReturn(ErrorType.TokenNotFound)
        if (!util.isAdmin(req.token)) return GroupMembershipCreateReturn(ErrorType.AdminOnly)

        if (req.group == null) return GroupMembershipCreateReturn(ErrorType.InputMissingGroup)
        if (req.person == null) return GroupMembershipCreateReturn(ErrorType.InputMissingPerson)

        this.ds.connection.use { conn ->

            //language=SQL
            val checkRowStatement = conn.prepareStatement(
                """
                select exists(select id from admin.group_memberships where group_id = ? and person = ?)
            """.trimIndent()
            )

            checkRowStatement.setInt(1, req.group)
            checkRowStatement.setInt(2, req.person)

            val result = checkRowStatement.executeQuery();

            if (result.next()) {
                val rowFound = result.getBoolean(1)

                if (rowFound) {
                    return GroupMembershipCreateReturn(ErrorType.MembershipAlreadyExists)
                } else {

                    //language=SQL
                    val statement = conn.prepareStatement(
                        """
                        insert into admin.group_memberships(group_id, person, created_by, modified_by) 
                            values(?, ?,
                                (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                ),
                                (
                                  select person 
                                  from admin.tokens 
                                  where token = ?
                                )
                        );
                        """.trimIndent()
                                )

                    statement.setInt(1, req.group)
                    statement.setInt(2, req.person)
                    statement.setString(3, req.token)
                    statement.setString(4, req.token)

                    statement.execute()

                }
            }


        }

        return GroupMembershipCreateReturn(ErrorType.NoError)
    }

}