package com.seedcompany.cordtables.components.pages.request_prayer

import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.components.admin.GetSecureListQuery
import com.seedcompany.cordtables.components.tables.up.prayer_notifications.prayerNotification
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


@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
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

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("prayer-requests/list")
    @ResponseBody
    fun list(@RequestBody req: CommonPrayerRequestsListRequest): CommonPrayerRequestsListResponse {
        var data: MutableList<PrayerRequestData> = mutableListOf()
        var rowMapper: RowMapper<PrayerRequestData> = RowMapper<PrayerRequestData> { resultSet: ResultSet, rowIndex: Int ->
            PrayerRequestData(
                resultSet.getInt("id"),
                resultSet.getInt("language_id"),
                resultSet.getString("sensitivity"),
                resultSet.getInt("parent"),
                resultSet.getInt("translator"),
                resultSet.getString("location"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getBoolean("reviewed"),
                resultSet.getString("requestedBy"),
                resultSet.getInt("notify"),
                resultSet.getBoolean("myRequest")
            )
        }

        var sql = """
            SELECT a.id, a.language_id, a.sensitivity, a.parent, a.translator, a.location, a.title, a.content,  a.title, a.content, a.reviewed, a.requestedBy,
            CASE
                WHEN created_by = (SELECT person FROM admin.tokens where token = '${req.token}') THEN TRUE
                ELSE FALSE
            END 
              AS myRequest,
            b.id as notify FROM
            (SELECT pr.id as id, pr.language_id, pr.sensitivity, pr.parent as parent, pr.translator, pr.location,  pr.title as title, pr.content as content, pr.reviewed, pr.created_by as created_by, ap.public_full_name as requestedBy FROM 
            up.prayer_requests as pr, admin.people as ap where pr.created_by=ap.id) as a
            LEFT JOIN (SELECT id, request FROM up.prayer_notifications WHERE person=(SELECT person FROM admin.tokens where token = '${req.token}')) as b ON a.id=b.request order by a.id desc
        """.trimIndent()
        var results = jdbcTemplate.query(sql, rowMapper)
        results.forEach { rec ->
            data.add(
                PrayerRequestData(
                    id = rec.id,
                    language_id = rec.language_id,
                    sensitivity = rec.sensitivity,
                    parent = rec.parent,
                    translator = rec.translator,
                    location = rec.location,
                    title = rec.title,
                    content = rec.content,
                    reviewed = rec.reviewed,
                    requestedBy = rec.requestedBy,
                    notify = rec.notify,
                    myRequest= rec.myRequest
                )
            )
        }
        return CommonPrayerRequestsListResponse(ErrorType.NoError, data)
    }

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("prayer-requests/create")
    @ResponseBody
    fun create(@RequestBody req: CommonPrayerRequestsCreateRequest): CommonPrayerRequestsCreateResponse{
        val id = jdbcTemplate.queryForObject(
            """
            insert into up.prayer_requests(language_id, sensitivity, parent, translator, location, title, content, reviewed, created_by, modified_by, owning_person, owning_group)
                values(
                    ?,
                    ?::common.sensitivity,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?::boolean,
                    ?::up.prayer_type,
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
            req.prayerRequest.language_id,
            req.prayerRequest.sensitivity,
            req.prayerRequest.parent,
            req.prayerRequest.translator,
            req.prayerRequest.location,
            req.prayerRequest.title,
            req.prayerRequest.content,
            req.prayerRequest.reviewed,
            req.token,
            req.token,
            req.token,
        )
        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("prayer-requests/update")
    @ResponseBody
    fun update(@RequestBody req: CommonPrayerRequestsUpdateRequest): CommonPrayerRequestsCreateResponse{
        val id = jdbcTemplate.queryForObject(
            """
                UPDATE up.prayer_requests SET language_id=?, sensitivity=?::common.sensitivity, parent=?, translator=?, location=?,  title=?, content=?, reviewed=?::boolean, prayer_type=?::up.prayer_type WHERE id=? AND created_by=(
                    select person 
                    from admin.tokens 
                    where token = ?
               ) returning id;
            """.trimIndent(),
            Int::class.java,
            req.prayerRequest.language_id,
            req.prayerRequest.sensitivity,
            req.prayerRequest.parent,
            req.prayerRequest.translator,
            req.prayerRequest.location,
            req.prayerRequest.title,
            req.prayerRequest.content,
            req.prayerRequest.reviewed,
            req.prayerRequest.prayer_type,
            req.prayerRequest.id,
            req.token,
        )
        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("prayer-requests/get")
    @ResponseBody
    fun get(@RequestBody req: PrayerRequestGetRequest): PrayerRequestsGetResponse{
        var data: PrayerRequestGetData? = null
        var rowMapper: RowMapper<PrayerRequestGetData> = RowMapper<PrayerRequestGetData> { resultSet: ResultSet, rowIndex: Int ->
            PrayerRequestGetData(
                resultSet.getInt("id"),
                resultSet.getInt("language_id"),
                resultSet.getString("sensitivity"),
                resultSet.getInt("parent"),
                resultSet.getInt("translator"),
                resultSet.getString("location"),
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getBoolean("reviewed"),
                resultSet.getString("prayer_type"),
                resultSet.getInt("created_by"),
            )
        }
        var sql = """
            SELECT * FROM up.prayer_requests WHERE id=${req.id} and created_by=(
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
                language_id = reData.language_id,
                sensitivity = reData.sensitivity,
                parent = reData.parent,
                translator = reData.translator,
                location = reData.location,
                title = reData.title,
                content = reData.content,
                reviewed = reData.reviewed,
                prayer_type = reData.prayer_type,
                created_by = reData.created_by
            )
            return PrayerRequestsGetResponse(ErrorType.NoError, prayerRequest = data)
        }
    }

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
    @PostMapping("prayer-requests/notify")
    @ResponseBody
    fun notify(@RequestBody req: PrayerRequestNotifyRequest): CommonPrayerRequestsCreateResponse{
        var existingNotifications: ArrayList<Int> = ArrayList()
        var id:Int = 0;
        var sql: String = "SELECT count(id) as cnt FROM up.prayer_notifications WHERE request=${req.selectedRequest} AND person=(select person from admin.tokens where token = '${req.token}')"
        var count = jdbcTemplate.queryForObject(sql, Long::class.java, )
        println(count)
        if (count != null && count > 0) {
            if(req.action == "remove"){
                id = jdbcTemplate.queryForObject(
                    """
                        DELETE FROM up.prayer_notifications WHERE request = ?::INTEGER AND  person = (
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
                        INSERT INTO up.prayer_notifications (request, person,  created_by, modified_by, owning_person, owning_group) 
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

    @CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com", "*"])
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
//        var resultsNotifications = jdbcTemplate.query("SELECT * FROM up.prayer_notifications WHERE person=(select person from admin.tokens where token = '${req.token}') ", rowMapper)
//        resultsNotifications.forEach { rec ->
//            rec.request?.let { existingNotifications.add(rec.request) }
//        }
//        for (item in req.selectedRequests) {
//
//        }

        return CommonPrayerRequestsCreateResponse(error = ErrorType.NoError, id = id)
    }

}
