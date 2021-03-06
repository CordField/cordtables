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

data class CommonPostsUpdateReadRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonPostsUpdateReadResponse(
        val error: ErrorType,
        val post: Post? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonPostsUpdateRead")
class UpdateRead(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,

        @Autowired
        val update: Update,

        @Autowired
        val read: Read,
) {
    @PostMapping("common/posts/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonPostsUpdateReadRequest): CommonPostsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonPostsUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonPostsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonPostsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonPostsUpdateReadResponse(error = readResponse.error, readResponse.post)
    }
}
