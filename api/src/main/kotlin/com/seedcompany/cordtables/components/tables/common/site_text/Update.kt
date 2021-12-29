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

data class CommonSiteTextUpdateRequest(
    val token: String,
    val site_text: CommonSiteTextInput,
)

data class CommonSiteTextUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonSiteTextUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common-site-texts/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonSiteTextUpdateRequest): CommonSiteTextUpdateResponse {

        if (req.token == null) return CommonSiteTextUpdateResponse(ErrorType.TokenNotFound)
        if (req.site_text.id == null) return CommonSiteTextUpdateResponse(ErrorType.MissingId)

        if (req.site_text.ethnologue != null) util.updateField(
            token = req.token,
            table = "common.site_text",
            column = "ethnologue",
            id = req.site_text.id!!,
            value = req.site_text.ethnologue,
        )

        return CommonSiteTextUpdateResponse(ErrorType.NoError)
    }

}
