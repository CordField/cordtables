package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.components.tables.sc.products.ScProductsUpdateRequest
import com.seedcompany.cordtables.components.tables.sc.products.Update as CommonUpdate
import com.seedcompany.cordtables.common.LocationType
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.sc.products.ScProductsUpdateResponse
import com.seedcompany.cordtables.components.tables.sc.products.productInput
import com.seedcompany.cordtables.components.tables.sc.locations.ScLocationInput
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
    val id: Int? = null,
    val column: String? = null,
    val value: Any? = null,
)

data class ScProductsUpdateResponse(
    val error: ErrorType,
)



@Controller("ScProductsUpdate")
class Update(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)
    @PostMapping("sc-products/update")
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
            "neo4j_id" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "neo4j_id",
                    id = req.id,
                    value = req.value,
                )
            }
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
                    value = req.value,
                    cast = "::INTEGER"
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
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
            "owning_group" -> {
                util.updateField(
                    token = req.token,
                    table = "sc.products",
                    column = "owning_group",
                    id = req.id,
                    value = req.value,
                    cast = "::INTEGER"
                )
            }
        }

        return ScProductsUpdateResponse(ErrorType.NoError)
    }

}