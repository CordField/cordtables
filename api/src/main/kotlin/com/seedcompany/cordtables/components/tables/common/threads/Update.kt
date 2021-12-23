package com.seedcompany.cordtables.components.tables.common.threads

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonThreadsUpdateRequest(
        val token: String?,
        val id: String? = null,
        val column: String? = null,
        val value: Any? = null,
)

data class CommonThreadsUpdateResponse(
        val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonThreadsUpdate")
class Update(
        @Autowired
        val util: Utility,

        @Autowired
        val ds: DataSource,
) {
    @PostMapping("common-threads/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonThreadsUpdateRequest): CommonThreadsUpdateResponse {

        if (req.token == null) return CommonThreadsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonThreadsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonThreadsUpdateResponse(ErrorType.MissingId)
        println(req)

        when (req.column) {

            "content" -> {
                util.updateField(
                        token = req.token,
                        table = "common.threads",
                        column = "content",
                        id = req.id,
                        value = req.value
                )
            }
            "channel" -> {
                util.updateField(
                        token = req.token,
                        table = "common.threads",
                        column = "channel",
                        id = req.id,
                        value = req.value,
                        cast ="::integer"
                )
            }
            "owning_person" -> {
                util.updateField(
                        token = req.token,
                        table = "common.threads",
                        column = "owning_person",
                        id = req.id,
                        value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                        token = req.token,
                        table = "common.threads",
                        column = "owning_group",
                        id = req.id,
                        value = req.value
                )
            }
        }

        return CommonThreadsUpdateResponse(ErrorType.NoError)
    }
}
