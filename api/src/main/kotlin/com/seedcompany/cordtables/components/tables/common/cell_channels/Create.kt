package com.seedcompany.cordtables.components.tables.common.cell_channels

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.tables.sc.languages.Read
import com.seedcompany.cordtables.components.tables.sc.languages.Update
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource


data class CommonCellChannelsCreateRequest(
        val token: String? = null,
        val cell_channel: CellChannelInput,
)

data class CommonCellChannelsCreateResponse(
        val error: ErrorType,
        val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonCellChannelsCreate")
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

    @PostMapping("common-cell-channels/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonCellChannelsCreateRequest): CommonCellChannelsCreateResponse {

        if (req.token == null) return CommonCellChannelsCreateResponse(error = ErrorType.InputMissingToken, null)


        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
                """
            insert into common.cell_channels(table_name, column_name, row, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
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
                    1
                )
            returning id;
        """.trimIndent(),
                Int::class.java,
                req.cell_channel.table_name,
                req.cell_channel.column_name,
                req.cell_channel.row,
                req.token,
                req.token,
                req.token,
        )


        return CommonCellChannelsCreateResponse(error = ErrorType.NoError, id = id)
    }
}

