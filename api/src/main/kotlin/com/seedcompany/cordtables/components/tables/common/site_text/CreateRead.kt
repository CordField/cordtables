package com.seedcompany.cordtables.components.tables.common.site_text

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonSiteTextCreateReadRequest(
        val token: String? = null,
        val site_text: CommonSiteTextInput,
)

data class CommonSiteTextCreateReadResponse(
        val error: ErrorType,
        val site_text: CommonSiteText? = null,
)


@Controller("CommonSiteTextCreateRead")
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
    @PostMapping("common-site-texts/create-read")
    @ResponseBody
    fun createReadHandler(@RequestBody req: CommonSiteTextCreateReadRequest): CommonSiteTextCreateReadResponse {

        val createResponse = create.createHandler(
                CommonSiteTextCreateRequest(
                        token = req.token,
                        site_text = req.site_text
                )
        )

        if (createResponse.error != ErrorType.NoError) {
            return CommonSiteTextCreateReadResponse(error = createResponse.error)
        }

        val readResponse = read.readHandler(
            CommonSiteTextReadRequest(
                    token = req.token,
                    id = createResponse!!.id
            )
        )

        return CommonSiteTextCreateReadResponse(error = readResponse.error, site_text = readResponse.site_text)
    }
}