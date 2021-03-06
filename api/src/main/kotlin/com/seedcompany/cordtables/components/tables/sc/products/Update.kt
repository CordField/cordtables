package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

data class ScProductsUpdateRequest(
    val token: String?,
    val id: String? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProductsUpdateResponse(
    val error: ErrorType,
)


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    @PostMapping("sc/products/update")
    @ResponseBody
    fun updateHandler(@RequestBody req: ScProductsUpdateRequest): ScProductsUpdateResponse {

        if (req.token == null) return ScProductsUpdateResponse(ErrorType.TokenNotFound)
        if (req.column == null) return ScProductsUpdateResponse(ErrorType.InputMissingColumn)
        if (req.id == null) return ScProductsUpdateResponse(ErrorType.MissingId)

        if (req.column == "mediums"){
            jdbcTemplate.update(
                """
                    update sc.products
                    set 
                        mediums = ?::common.product_mediums[],
                        modified_by = 
                            (
                              select person 
                              from admin.tokens 
                              where token = ?
                            ),
                        modified_at = CURRENT_TIMESTAMP
                    where id = ?;
                """.trimIndent(),
                req.value,
                req.token,
                req.id,
            )
        }

        if (req.column == "methodologies"){
            jdbcTemplate.update(
                """
                    update sc.products
                    set 
                        methodologies = ?::common.product_methodologies[],
                        modified_by = 
                            (
                              select person 
                              from admin.tokens 
                              where token = ?
                            ),
                        modified_at = CURRENT_TIMESTAMP
                    where id = ?;
                """.trimIndent(),
                req.value,
                req.token,
                req.id,
            )
        }

        if (req.column == "purposes"){
            jdbcTemplate.update(
                """
                    update sc.products
                    set 
                        purposes = ?::common.product_purposes[],
                        modified_by = 
                            (
                              select person 
                              from admin.tokens 
                              where token = ?
                            ),
                        modified_at = CURRENT_TIMESTAMP
                    where id = ?;
                """.trimIndent(),
                req.value,
                req.token,
                req.id,
            )
        }

        when (req.column) {

            "name" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "name",
                    id = req.id,
                    value = req.value,
                )
            }
            "change_to_plan" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "change_to_plan",
                    id = req.id,
                    value = req.value
                )
            }
            "active" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "active",
                    id = req.id,
                    value = req.value,
                    cast = "::BOOLEAN"
                )
            }
            "type" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "type",
                    id = req.id,
                    value = req.value,
                    cast = "::common.product_type"
                )
            }

            "owning_person" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "owning_person",
                    id = req.id,
                    value = req.value
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "owning_group",
                    id = req.id,
                    value = req.value
                )
            }
        }

        return ScProductsUpdateResponse(ErrorType.NoError)
    }

}
