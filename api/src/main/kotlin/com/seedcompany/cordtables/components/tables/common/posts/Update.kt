package com.seedcompany.cordtables.components.tables.common.posts

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonPostsUpdateRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonPostsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPostsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-posts/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonPostsUpdateRequest): CommonPostsUpdateResponse {

        if (req.token == null) return CommonPostsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonPostsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonPostsUpdateResponse(ErrorType.MissingId)
        println(req)

        when (req.column) {

            "content" -> {
                util.updateField(
                        token = req.token,
                        table = "common.posts",
                        column = "content",
                        id = req.id,
                        value = req.value
                )
            }
            "thread" -> {
                util.updateField(
                        token = req.token,
                        table = "common.posts",
                        column = "thread",
                        id = req.id,
                        value = req.value,
                        cast ="::integer"
                )
            }
            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.posts",
                        column = "owning_person",
                        id = req.id,
                        value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.posts",
                        column = "owning_group",
                        id = req.id,
                        value = req.value
                )
            }
        }

        return CommonPostsUpdateResponse(ErrorType.NoError)
    }
}
