package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductScriptureReferencesUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProductScriptureReferencesUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductScriptureReferencesUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    @PostMapping("sc-product-scripture-references/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProductScriptureReferencesUpdateRequest): ScProductScriptureReferencesUpdateResponse {

        if (req.token == null) return ScProductScriptureReferencesUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScProductScriptureReferencesUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProductScriptureReferencesUpdateResponse(ErrorType.MissingId)

        when (req.column) {
            "product" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "product",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "scripture_reference" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "scripture_reference",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }


            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "owning_person",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.product_scripture_references",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::uuid"
                )
            }
        }

        return ScProductScriptureReferencesUpdateResponse(ErrorType.NoError)
    }

}
