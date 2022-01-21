package com.seedcompany.cordtables.components.tables.common.blogs

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonBlogsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/blogs/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonBlogsUpdateRequest): CommonBlogsUpdateResponse {

      if (req.token == null) return CommonBlogsUpdateResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogsUpdateResponse(ErrorType.AdminOnly)
        if (req.column == null) return CommonBlogsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonBlogsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "group_id" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blogs",
                    column = "title",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blogs",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.blogs",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return CommonBlogsUpdateResponse(ErrorType.NoError)
    }

}
