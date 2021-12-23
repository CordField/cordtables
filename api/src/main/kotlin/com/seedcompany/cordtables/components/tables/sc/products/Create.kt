package com.seedcompany.cordtables.components.tables.sc.products

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.products.productInput
import com.seedcompany.cordtables.components.tables.sc.products.Read
import com.seedcompany.cordtables.components.tables.sc.products.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class ScProductsCreateRequest(
    val token: String? = null,
    val product: productInput,
)

data class ScProductsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductsCreate")
class Create(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val update: Update,

    @Autowired
    val read: Read,
) {
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    @PostMapping("sc/products/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProductsCreateRequest): ScProductsCreateResponse {

        // if (req.product.name == null) return ScProductsCreateResponse(error = ErrorType.InputMissingToken, null)

        println(req.product.mediums.toString().replace("[", "{").replace("]", "}"))

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.products(neo4j_id, name, change_to_plan, active, mediums, methodologies, purposes, type,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?,
                    ?,
                    ?,
                    ARRAY[?]::common.product_mediums[],
                    ARRAY[?]::common.product_methodologies[],
                    ARRAY[?]::common.product_purposes[],
                    ?::common.product_type,
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    (
                      select person 
                      from admin.tokens 
                      where token = ?
                    ),
                    1
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.product.neo4j_id,
            req.product.name,
            req.product.change_to_plan,
            req.product.active,
            req.product.mediums.toString().replace("[", "{").replace("]", "}"),
            req.product.methodologies,
            req.product.purposes,
            req.product.type,
            req.token,
            req.token,
            req.token,
        )

//        req.language.id = id

        return ScProductsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
