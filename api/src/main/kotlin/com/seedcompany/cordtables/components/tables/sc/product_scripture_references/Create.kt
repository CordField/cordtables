package com.seedcompany.cordtables.components.tables.sc.product_scripture_references

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.productScriptureReferenceInput
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.Read
import com.seedcompany.cordtables.components.tables.sc.product_scripture_references.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class ScProductScriptureReferencesCreateRequest(
    val token: String? = null,
    val productScriptureReference: productScriptureReferenceInput,
)

data class ScProductScriptureReferencesCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("ScProductScriptureReferencesCreate")
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

    @PostMapping("sc/product-scripture-references/create")
    @ResponseBody
    fun createHandler(@RequestBody req: ScProductScriptureReferencesCreateRequest): ScProductScriptureReferencesCreateResponse {

        // if (req.productScriptureReference.name == null) return ScProductScriptureReferencesCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.product_scripture_references(product, scripture_reference, change_to_plan, active,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?::uuid,
                    ?::uuid,
                    ?::uuid,
                    ?,
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
                    ?::uuid
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.productScriptureReference.product,
            req.productScriptureReference.scripture_reference,
            req.productScriptureReference.change_to_plan,
            req.productScriptureReference.active,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return ScProductScriptureReferencesCreateResponse(error = ErrorType.NoError, id = id)
    }

}
