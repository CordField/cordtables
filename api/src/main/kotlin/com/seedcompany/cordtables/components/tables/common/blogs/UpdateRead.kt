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

data class CommonBlogsUpdateReadRequest(
        val token: String?,
        val id: Int? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonBlogsUpdateReadResponse(
        val error: ErrorType,
        val blog: Blog? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonBlogsUpdateRead")
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
    @PostMapping("common-blogs/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonBlogsUpdateReadRequest): CommonBlogsUpdateReadResponse {

        val updateResponse = update.updateHandler(
                CommonBlogsUpdateRequest(
                        token = req.token,
                        column = req.column,
                        id = req.id,
                        value = req.value,
                )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonBlogsUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
                CommonBlogsReadRequest(
                        token = req.token,
                        id = req.id!!
                )
        )

        return CommonBlogsUpdateReadResponse(error = readResponse.error, readResponse.blog)
    }
}
