package com.seedcompany.cordtables.components.tables.common.work_estimates

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.admin.GetSecureListQueryRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.SQLException
import javax.sql.DataSource


data class CommonWorkEstimateListRequest(
    val token: String?,
    val ticket: String? = null
)

data class CommonWorkEstimateListResponse(
    val error: ErrorType,
    val work_estimate: MutableList<CommonWorkEstimates>?
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
@Controller("CommonWorkEstimatesList")
class List(
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {

    var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)

    @PostMapping("common/work-estimates/list")
    @ResponseBody
    fun listHandler(@RequestBody req: CommonWorkEstimateListRequest): CommonWorkEstimateListResponse{
        var data: MutableList<CommonWorkEstimates> = mutableListOf()
        if (req.token == null) return CommonWorkEstimateListResponse(ErrorType.TokenNotFound, mutableListOf())
        if (req.ticket == null) return CommonWorkEstimateListResponse(ErrorType.MissingTicketId, mutableListOf())

        val paramSource = MapSqlParameterSource()
        paramSource.addValue("token", req.token)
        paramSource.addValue("ticket", req.ticket)

      val query = """
        
        WITH ROW_LEVEL_ACCESS AS
        	(SELECT ROW
        		FROM ADMIN.GROUP_ROW_ACCESS AS A
        		INNER JOIN ADMIN.GROUP_MEMBERSHIPS AS B ON A.GROUP_ID = B.GROUP_ID
        		INNER JOIN ADMIN.TOKENS AS C ON B.PERSON = C.PERSON
        		WHERE A.TABLE_NAME = 'common.work_estimates'
        			AND C.TOKEN = '${req.token}' ),
        	PUBLIC_ROW_LEVEL_ACCESS AS
        	(SELECT ROW
        		FROM ADMIN.GROUP_ROW_ACCESS AS A
        		INNER JOIN ADMIN.GROUP_MEMBERSHIPS AS B ON A.GROUP_ID = B.GROUP_ID
        		INNER JOIN ADMIN.TOKENS AS C ON B.PERSON = C.PERSON
        		WHERE A.TABLE_NAME = 'common.work_estimates'
        			AND C.TOKEN = 'public' ),
        	COLUMN_LEVEL_ACCESS AS
        	(SELECT COLUMN_NAME
        		FROM ADMIN.ROLE_COLUMN_GRANTS A
        		INNER JOIN ADMIN.ROLE_MEMBERSHIPS B ON A.ROLE = B.ROLE
        		INNER JOIN ADMIN.TOKENS C ON B.PERSON = C.PERSON
        		WHERE A.TABLE_NAME = 'common.work_estimates'
        			AND C.TOKEN = '${req.token}' ),
        	PUBLIC_COLUMN_LEVEL_ACCESS AS
        	(SELECT COLUMN_NAME
        		FROM ADMIN.ROLE_COLUMN_GRANTS A
        		INNER JOIN ADMIN.ROLE_MEMBERSHIPS B ON A.ROLE = B.ROLE
        		INNER JOIN ADMIN.TOKENS C ON B.PERSON = C.PERSON
        		WHERE A.TABLE_NAME = 'common.work_estimates'
        			AND C.TOKEN = 'public' )
        SELECT CASE
        											WHEN 'id' in
        																	(SELECT COLUMN_NAME
        																		FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.ID
        											WHEN
        																	(SELECT EXISTS
        																			(SELECT ID
        																				FROM ADMIN.ROLE_MEMBERSHIPS
        																				WHERE PERSON =
        																						(SELECT PERSON
        																							FROM ADMIN.TOKENS
        																							WHERE TOKEN = '${req.token}')
        																					AND ROLE =
        																						(SELECT ID
        																							FROM ADMIN.ROLES
        																							WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.ID
        											WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        																	(SELECT PERSON
        																		FROM ADMIN.TOKENS
        																		WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.ID
        											WHEN 'id' in
        																	(SELECT COLUMN_NAME
        																		FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.ID
        											ELSE NULL
        							END AS ID ,
        							admin.people.public_full_name,
        	CASE
        					WHEN 'person' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN PERSON
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN PERSON
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN PERSON
        					WHEN 'person' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN PERSON
        					ELSE NULL
        	END AS PERSON ,
        	CASE
        					WHEN 'ticket' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN TICKET
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN TICKET
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN TICKET
        					WHEN 'ticket' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN TICKET
        					ELSE NULL
        	END AS TICKET ,
        	CASE
        					WHEN 'hours' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN HOURS
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN HOURS
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN HOURS
        					WHEN 'hours' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN HOURS
        					ELSE NULL
        	END AS HOURS ,
        	CASE
        					WHEN 'minutes' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN MINUTES
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN MINUTES
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN MINUTES
        					WHEN 'minutes' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN MINUTES
        					ELSE NULL
        	END AS MINUTES ,
        	CASE
        					WHEN 'total_time' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN TOTAL_TIME
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN TOTAL_TIME
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN TOTAL_TIME
        					WHEN 'total_time' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN TOTAL_TIME
        					ELSE NULL
        	END AS TOTAL_TIME ,
        	CASE
        					WHEN 'comment' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMENT
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMENT
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMENT
        					WHEN 'comment' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMENT
        					ELSE NULL
        	END AS COMMENT ,
        	CASE
        					WHEN 'created_at' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.CREATED_AT
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.CREATED_AT
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.CREATED_AT
        					WHEN 'created_at' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.CREATED_AT
        					ELSE NULL
        	END AS CREATED_AT ,
        	CASE
        					WHEN 'created_by' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.CREATED_BY
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.CREATED_BY
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.CREATED_BY
        					WHEN 'created_by' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.CREATED_BY
        					ELSE NULL
        	END AS CREATED_BY ,
        	CASE
        					WHEN 'modified_at' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.MODIFIED_AT
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.MODIFIED_AT
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.MODIFIED_AT
        					WHEN 'modified_at' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.MODIFIED_AT
        					ELSE NULL
        	END AS MODIFIED_AT ,
        	CASE
        					WHEN 'modified_by' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.MODIFIED_BY
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.MODIFIED_BY
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.MODIFIED_BY
        					WHEN 'modified_by' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.MODIFIED_BY
        					ELSE NULL
        	END AS MODIFIED_BY ,
        	CASE
        					WHEN 'owning_person' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.OWNING_PERSON
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.OWNING_PERSON
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.OWNING_PERSON
        					WHEN 'owning_person' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.OWNING_PERSON
        					ELSE NULL
        	END AS OWNING_PERSON ,
        	CASE
        					WHEN 'owning_group' in
        											(SELECT COLUMN_NAME
        												FROM COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.OWNING_GROUP
        					WHEN
        											(SELECT EXISTS
        													(SELECT ID
        														FROM ADMIN.ROLE_MEMBERSHIPS
        														WHERE PERSON =
        																(SELECT PERSON
        																	FROM ADMIN.TOKENS
        																	WHERE TOKEN = '${req.token}')
        															AND ROLE =
        																(SELECT ID
        																	FROM ADMIN.ROLES
        																	WHERE NAME = 'Administrator'))) THEN COMMON.WORK_ESTIMATES.OWNING_GROUP
        					WHEN COMMON.WORK_ESTIMATES.OWNING_PERSON =
        											(SELECT PERSON
        												FROM ADMIN.TOKENS
        												WHERE TOKEN = '${req.token}') THEN COMMON.WORK_ESTIMATES.OWNING_GROUP
        					WHEN 'owning_group' in
        											(SELECT COLUMN_NAME
        												FROM PUBLIC_COLUMN_LEVEL_ACCESS) THEN COMMON.WORK_ESTIMATES.OWNING_GROUP
        					ELSE NULL
        	END AS OWNING_GROUP
        FROM COMMON.WORK_ESTIMATES
        LEFT JOIN ADMIN.PEOPLE ON (admin.people.id = common.work_estimates.person)
        WHERE 1 = 1
        	AND TICKET = '${req.ticket}'
        ORDER BY ID
      """.trimIndent()

      try {
            val jdbcResult = jdbcTemplate.queryForRowSet(query, paramSource)
            while (jdbcResult.next()) {

                var id: String? = jdbcResult.getString("id")
                if (jdbcResult.wasNull()) id = null

                var person: String? = jdbcResult.getString("person")
                if (jdbcResult.wasNull()) person = null

                var public_full_name: String? = jdbcResult.getString("public_full_name")
                if (jdbcResult.wasNull()) public_full_name = null;

                var ticket: String? = jdbcResult.getString("ticket")
                if (jdbcResult.wasNull()) ticket = null

                var hours: Int? = jdbcResult.getInt("hours")
                if (jdbcResult.wasNull()) hours = null

                var minutes : Int? = jdbcResult.getInt("minutes")
                if (jdbcResult.wasNull()) minutes = null

                var totalTime : Number? = jdbcResult.getFloat("total_time")
                if (jdbcResult.wasNull()) totalTime = null

                var comment : String? = jdbcResult.getString("comment")
                if (jdbcResult.wasNull()) comment = null

                var createdAt: String? = jdbcResult.getString("created_at")
                if (jdbcResult.wasNull()) createdAt = null

                var createdBy: String? = jdbcResult.getString("created_by")
                if (jdbcResult.wasNull()) createdBy = null

                var modifiedAt: String? = jdbcResult.getString("modified_at")
                if (jdbcResult.wasNull()) modifiedAt = null

                var modifiedBy: String? = jdbcResult.getString("modified_by")
                if (jdbcResult.wasNull()) modifiedBy = null

                var owningPerson: String? = jdbcResult.getString("owning_person")
                if (jdbcResult.wasNull()) owningPerson = null

                var owningGroup: String? = jdbcResult.getString("owning_group")
                if (jdbcResult.wasNull()) owningGroup = null

                data.add(
                    CommonWorkEstimates(
                        id = id,
                        person = person,
                        public_full_name = public_full_name,
                        ticket = ticket,
                        hours = hours,
                        minutes = minutes,
                        total_time = totalTime,
                        comment = comment,
                        created_at = createdAt,
                        created_by = createdBy,
                        modified_at = modifiedAt,
                        modified_by = modifiedBy,
                        owning_person = owningPerson,
                        owning_group = owningGroup
                    )
                )
            }
        } catch (e: SQLException) {
            println("error while listing ${e.message}")
            return CommonWorkEstimateListResponse(ErrorType.SQLReadError, mutableListOf())
        }
        return CommonWorkEstimateListResponse(ErrorType.NoError, data)
    }
}
