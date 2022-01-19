package com.seedcompany.cordtables.components.tables.common.org_chart_positions

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.orgChartPositionInput
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.Read
import com.seedcompany.cordtables.components.tables.common.org_chart_positions.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonOrgChartPositionsCreateRequest(
    val token: String? = null,
    val orgChartPosition: orgChartPositionInput,
)

data class CommonOrgChartPositionsCreateResponse(
    val error: ErrorType,
    val id: String? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonOrgChartPositionsCreate")
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

    @PostMapping("common/org-chart-positions/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonOrgChartPositionsCreateRequest): CommonOrgChartPositionsCreateResponse {

        // if (req.orgChartPosition.name == null) return CommonOrgChartPositionsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.org_chart_positions(organization, name,  created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
                    ?
                )
            returning id;
        """.trimIndent(),
            String::class.java,
            req.orgChartPosition.organization,
            req.orgChartPosition.name,
            req.token,
            req.token,
            req.token,
            util.adminGroupId
        )

//        req.language.id = id

        return CommonOrgChartPositionsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
