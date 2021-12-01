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

data class CommonSiteTextUpdateReadRequest(
        val token: String,
        val site_text: CommonSiteTextInput,
)

data class CommonSiteTextUpdateReadResponse(
        val error: ErrorType,
        val site_text: CommonSiteText? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextUpdateRead")
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
    @PostMapping("common-site-texts/update-read")
    @ResponseBody
    fun updateReadHandler(@RequestBody req: CommonSiteTextUpdateReadRequest): CommonSiteTextUpdateReadResponse {

        val updateResponse = update.updateHandler(
            CommonSiteTextUpdateRequest(
                    token = req.token,
                    site_text = req.site_text,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonSiteTextUpdateReadResponse(updateResponse.error)
        }

        val readResponse = read.readHandler(
            CommonSiteTextReadRequest(
                    token = req.token,
                    id = req.site_text!!.id
            )
        )

        return CommonSiteTextUpdateReadResponse(error = readResponse.error, readResponse.site_text)
    }
}
