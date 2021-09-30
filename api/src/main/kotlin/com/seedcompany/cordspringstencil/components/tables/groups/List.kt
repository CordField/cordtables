package com.seedcompany.cordspringstencil.components.tables.groups

import com.seedcompany.cordspringstencil.common.ErrorType
import com.seedcompany.cordspringstencil.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource
import kotlin.collections.List

data class GroupsListRequest(
    val token: String? = null,
)

data class GroupsListReturn(
    val error: ErrorType,
    val groups: List<out String>?,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordfield.org", "https://cordfield.org"])
@Controller()
class List (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
){

    @PostMapping("groups/list")
    @ResponseBody
    fun listHandler(@RequestBody req: GroupsListRequest):GroupsListReturn{

        if (req.token == null) return GroupsListReturn(ErrorType.TokenNotFound, null)

        val isAdmin = util.isAdmin(req.token)

        println(isAdmin)

        return GroupsListReturn(ErrorType.NoError, null)
    }

}