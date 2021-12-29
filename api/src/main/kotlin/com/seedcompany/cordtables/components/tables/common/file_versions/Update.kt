package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsUpdateRequest
import com.seedcompany.cordtables.components.tables.common.file_versions.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionInput
import com.seedcompany.cordtables.components.tables.sc.languages.ScLanguagesUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFileVersionsUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonFileVersionsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonFileVersionsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/file-versions/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonFileVersionsUpdateRequest): CommonFileVersionsUpdateResponse {

        if (req.token == null) return CommonFileVersionsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonFileVersionsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonFileVersionsUpdateResponse(ErrorType.MissingId)


        when (req.column) {
            "category" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "category",
                    id = req.id,
                    value = req.value,
                )
            }
            "mime_type" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "mime_type",
                    id = req.id,
                    value = req.value,
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "file" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "file",
                    id = req.id,
                    value = req.value,
                )
            }
            "file_url" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "file_url",
                    id = req.id,
                    value = req.value,
                )
            }
            "file_size" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "file_size",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.file_versions",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return CommonFileVersionsUpdateResponse(ErrorType.NoError)
    }

}
