package com.seedcompany.cordtables.components.tables.common.blogs

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.blogs.*
import com.seedcompany.cordtables.components.tables.common.blogs.CommonBlogsCreateRequest
import com.seedcompany.cordtables.components.tables.common.blogs.Create
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonBlogsCreateReadRequest(
    val token: String? = null,
    val blog: blogInput,
)

data class CommonBlogsCreateReadResponse(
    val error: ErrorType,
    val blog: blog? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonBlogsCreateRead")
class CreateRead(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val create: Create,

    @Autowired
    val read: Read,
) {
    @PostMapping("common/blogs/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonBlogsCreateReadRequest): CommonBlogsCreateReadResponse {

      if (req.token == null) return CommonBlogsCreateReadResponse(ErrorType.InputMissingToken)
      if (!util.isAdmin(req.token)) return CommonBlogsCreateReadResponse(ErrorType.AdminOnly)

        val createResponse = create.createHandler(
            CommonBlogsCreateRequest(
                token = req.token,
                blog = req.blog
            )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonBlogsCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonBlogsReadRequest(
                token = req.token,
                id = createResponse!!.id
            )
        )

        return CommonBlogsCreateReadResponse(error = readResponse.error, blog = readResponse.blog)
    }
}
