package com.seedcompany.cordtables.components.tables.common.files

import com.seedcompany.cordtables.components.tables.common.files.CommonFilesUpdateRequest
import com.seedcompany.cordtables.components.tables.common.files.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.file_versions.CommonFileVersionsUpdateResponse
import com.seedcompany.cordtables.components.tables.common.files.CommonFileInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonFilesUpdateRequest(
    val token: String?,
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class CommonFilesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonFilesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("common/files/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: CommonFilesUpdateRequest): CommonFilesUpdateResponse {

        if (req.token == null) return CommonFilesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return CommonFilesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return CommonFilesUpdateResponse(ErrorType.MissingId)


        when (req.column) {
            "directory" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "directory",
                    id = req.id,
                    value = req.value,
                )
            }
            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "created_at" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "created_at",
                    id = req.id,
                    value = req.value,
                )
            }
            "created_by" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "created_by",
                    id = req.id,
                    value = req.value,
                )
            }
            "modified_at" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "modified_at",
                    id = req.id,
                    value = req.value,
                )
            }
            "modified_by" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "modified_by",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "common.files",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                )
            }
        }

        return CommonFilesUpdateResponse(ErrorType.NoError)
    }

}
