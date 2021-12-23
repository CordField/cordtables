package com.seedcompany.cordtables.components.tables.sc.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScPostsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScPostsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScPostsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc/posts/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScPostsUpdateRequest): ScPostsUpdateResponse {

        if (req.token == null) return ScPostsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScPostsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScPostsUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "directory" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "directory",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "type" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "type",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.post_type"
                )
            }
            "shareability" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "shareability",
                    id = req.id,
                    value = req.value,
                    cast = "::sc.post_shareability"
                )
            }
            "body" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "body",
                    id = req.id,
                    value = req.value,
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.posts",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScPostsUpdateResponse(ErrorType.NoError)
    }

}
