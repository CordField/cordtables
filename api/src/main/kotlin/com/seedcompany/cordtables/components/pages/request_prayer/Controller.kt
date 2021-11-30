package com.seedcompany.cordtables.components.pages.request_prayer

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.tables.common.prayer_notifications.prayerNotification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.ResultSet
import javax.sql.DataSource



@Controller("PrayerRequestsController")
class Controller (
    @Autowired
    val util: Utility,

    @Autowired
    val ds: DataSource,

    @Autowired
    val secureList: GetSecureListQuery,
) {
    // var jdbcTemplate: NamedParameterJdbcTemplate = NamedParameterJdbcTemplate(ds)
    val jdbcTemplate: JdbcTemplate = JdbcTemplate(ds)

    
    @PostMapping("prayer-requests/list")
    @ResponseBody
    fun list(@RequestBody req: CommonPrayerRequestsListRequest): CommonPrayerRequestsListResponse {
        var data: MutableList<PrayerRequestData> = mutableListOf()
        var rowMapper: RowMapper<PrayerRequestData> = RowMapper<PrayerRequestData> { resultSet: ResultSet, rowIndex: Int ->
            PrayerRequestData(
                resultSet.getInt("id"),
                resultSet.getString("subject"),
                resultSet.getInt("parentRequest"),
                resultSet.getString("content"),
                resultSet.getString("requestedBy"),
                resultSet.getInt("notify"),
                resultSet.getBoolean("myRequest")
            )
        }
        var sql = """
            SELECT a.id, a.parentRequest, a.subject, a.content, a.requestedBy,
            CASE
                WHEN created_by = (SELECT person FROM admin.tokens where token = '${req.token}') THEN TRUE
                ELSE FALSE
            END 
              AS myRequest,
            b.id as notify FROM
            (SELECT pr.id as id, pr.parent as parentRequest, pr.subject as subject, pr.content as content, pr.created_by as created_by, ap.public_full_name as requestedBy FROM common.prayer_requests as pr, admin.people as ap where pr.created_by=ap.id) as a
            LEFT JOIN (SELECT id, request FROM common.prayer_notifications WHERE person=(SELECT person FROM admin.tokens where token = '${req.token}')) as b ON a.id=b.request order by a.id desc
        """.trimIndent()
        var results = jdbcTemplate.query(sql, rowMapper)
        results.forEach { rec ->
            data.add(
                PrayerRequestData(
                    id = rec.id,
                    subject = rec.subject,
                    parentRequest = rec.parentRequest,
                    content = rec.content,
                    requestedBy = rec.requestedBy,
                    notify = rec.notify,
                    myRequest= rec.myRequest
                )
            )
        }
        return CommonPrayerRequestsListResponse(ErrorType.NoError, data)
    }

    
    @PostMapping("prayer-requests/create")
    @ResponseBody
    fun create(@RequestBody req: CommonPrayerRequestsCreateRequest): CommonPrayerRequestsCreateResponse{
        val id = jdbcTemplate.queryForObject(
            """
            insert into common.prayer_requests(parent, subject, content,  created_by, modified_by, owning_person, owning_group)
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
            req.prayerRequest.parent,
            req.prayerRequest.subject,
            req.prayerRequest.content,
            req.token,
            req.token,
            req.token,
        )
        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

    
    @PostMapping("prayer-requests/update")
    @ResponseBody
    fun update(@RequestBody req: CommonPrayerRequestsUpdateRequest): CommonPrayerRequestsCreateResponse{
        val id = jdbcTemplate.queryForObject(
            """
                UPDATE common.prayer_requests SET parent=?, subject=?, content=? WHERE id=? AND created_by=(
                    select person 
                    from admin.tokens 
                    where token = ?
               ) returning id;
            """.trimIndent(),
            Int::class.java,
            req.prayerRequest.parent,
            req.prayerRequest.subject,
            req.prayerRequest.content,
            req.prayerRequest.id,
            req.token,
        )
        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

    
    @PostMapping("prayer-requests/get")
    @ResponseBody
    fun get(@RequestBody req: PrayerRequestGetRequest): PrayerRequestsGetResponse{
        var data: PrayerRequestGetData? = null
        var rowMapper: RowMapper<PrayerRequestGetData> = RowMapper<PrayerRequestGetData> { resultSet: ResultSet, rowIndex: Int ->
            PrayerRequestGetData(
                resultSet.getInt("id"),
                resultSet.getString("subject"),
                resultSet.getInt("parent"),
                resultSet.getString("content"),
                resultSet.getInt("created_by"),
            )
        }
        var sql = """
            SELECT * FROM common.prayer_requests WHERE id=${req.id} and created_by=(
                SELECT person FROM admin.tokens WHERE token='${req.token}'
            )
        """.trimIndent()
        var result = jdbcTemplate.query(sql, rowMapper)
        if(result.size == 0) {
            return PrayerRequestsGetResponse(ErrorType.emptyReadResult, prayerRequest = data)
        }
        else{
            var reData = result.get(0)
            data = PrayerRequestGetData(
                id = reData.id,
                subject = reData.subject,
                parent = reData.parent,
                content = reData.content,
                created_by = reData.created_by
            )
            return PrayerRequestsGetResponse(ErrorType.NoError, prayerRequest = data)
        }
    }

    
    @PostMapping("prayer-requests/notify")
    @ResponseBody
    fun notify(@RequestBody req: PrayerRequestNotifyRequest): CommonPrayerRequestsCreateResponse{
        var existingNotifications: ArrayList<Int> = ArrayList()
        var id:Int = 0;
        var sql: String = "SELECT count(id) as cnt FROM common.prayer_notifications WHERE request=${req.selectedRequest} AND person=(select person from admin.tokens where token = '${req.token}')"
        var count = jdbcTemplate.queryForObject(sql, Long::class.java, )
        println(count)
        if (count != null && count > 0) {
            if(req.action == "remove"){
                id = jdbcTemplate.queryForObject(
                    """
                        DELETE FROM common.prayer_notifications WHERE request = ?::INTEGER AND  person = (
                            select person 
                            from admin.tokens 
                            where token = ?
                        )
                        returning id;
                    """.trimIndent(),
                    Int::class.java,
                    req.selectedRequest,
                    req.token,
                )
            }
        }
        else{
            if(req.action == "add"){
                id = jdbcTemplate.queryForObject(
                    """
                        INSERT INTO common.prayer_notifications (request, person,  created_by, modified_by, owning_person, owning_group) 
                            VALUES (
                                ?::INTEGER,
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
                    req.selectedRequest,
                    req.token,
                    req.token,
                    req.token,
                    req.token
                )
            }
        }

        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

    
    @PostMapping("prayer-requests/notifications")
    @ResponseBody
    fun notifications(@RequestBody req: PrayerRequestNotifyRequest): CommonPrayerRequestsCreateResponse{
        var id = 0
//        var rowMapper: RowMapper<prayerNotification> = RowMapper<prayerNotification> { resultSet: ResultSet, rowIndex: Int ->
//            prayerNotification(
//                resultSet.getInt("id"),
//                resultSet.getInt("request"),
//                resultSet.getInt("person"),
//
//                resultSet.getString("created_at"),
//                resultSet.getInt("created_by"),
//                resultSet.getString("modified_at"),
//                resultSet.getInt("modified_by"),
//                resultSet.getInt("owning_person"),
//                resultSet.getInt("owning_group"),
//
//                )
//        }
//
//        var resultsNotifications = jdbcTemplate.query("SELECT * FROM common.prayer_notifications WHERE person=(select person from admin.tokens where token = '${req.token}') ", rowMapper)
//        resultsNotifications.forEach { rec ->
//            rec.request?.let { existingNotifications.add(rec.request) }
//        }
//        for (item in req.selectedRequests) {
//
//        }

        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

}