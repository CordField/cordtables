package com.seedcompany.cordtables.components.tables.common.chats

import com.seedcompany.cordtables.common.CommonSensitivity
import com.seedcompany.cordtables.common.ErrorType
import com.seedcompany.cordtables.common.Utility
import com.seedcompany.cordtables.common.enumContains
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsCreateRequest
import com.seedcompany.cordtables.components.tables.common.chats.CommonChatsCreateResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import javax.sql.DataSource

data class CommonChatsCreateRequest(
    val token: String? = null,
    val chat: ChatInput,
)

data class CommonChatsCreateResponse(
    val error: ErrorType,
    val id: Int? = null,
)

@CrossOrigin(origins = ["http://localhost:3333", "https://dev.cordtables.com", "https://cordtables.com"])
@Controller("CommonChatsCreate")
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

    @PostMapping("common-chats/create")
    @ResponseBody
    fun createHandler(@RequestBody req: CommonChatsCreateRequest): CommonChatsCreateResponse {

        if (req.token == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingToken, null)
        if (req.chat == null) return CommonChatsCreateResponse(error = ErrorType.MissingId, null)
        if (req.chat.name == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingName, null)
        if (req.chat.display_name == null) return CommonChatsCreateResponse(error = ErrorType.InputMissingName, null)

        // check enums and error out if needed

        if (req.chat.sensitivity != null && !enumContains<CommonSensitivity>(req.chat.sensitivity)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.egids_level != null && !enumContains<EgidsScale>(req.chat.egids_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.least_reached_progress_jps_level != null && !enumContains<EgidsScale>(req.chat.least_reached_progress_jps_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.partner_interest_level != null && !enumContains<EgidsScale>(req.chat.partner_interest_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.multiple_languages_leverage_linguistic_level != null && !enumContains<EgidsScale>(req.chat.multiple_languages_leverage_linguistic_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.multiple_languages_leverage_joint_training_level != null && !enumContains<EgidsScale>(req.chat.multiple_languages_leverage_joint_training_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.lang_comm_int_in_language_development_level != null && !enumContains<EgidsScale>(req.chat.lang_comm_int_in_language_development_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.lang_comm_int_in_scripture_translation_level != null && !enumContains<EgidsScale>(req.chat.lang_comm_int_in_scripture_translation_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.access_to_scripture_in_lwc_level != null && !enumContains<EgidsScale>(req.chat.access_to_scripture_in_lwc_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.begin_work_geo_challenges_level != null && !enumContains<EgidsScale>(req.chat.begin_work_geo_challenges_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        if (req.chat.begin_work_rel_pol_obstacles_level != null && !enumContains<EgidsScale>(req.chat.begin_work_rel_pol_obstacles_level)) {
            return CommonChatsCreateResponse(
                error = ErrorType.ValueDoesNotMap
            )
        }

        // create row with required fields, use id to update cells afterwards one by one
        val id = jdbcTemplate.queryForObject(
            """
            insert into sc.languages(name, display_name, created_by, modified_by, owning_person, owning_group)
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
                    1
                )
            returning id;
        """.trimIndent(),
            Int::class.java,
            req.chat.name,
            req.chat.display_name,
            req.token,
            req.token,
            req.token,
        )

        req.chat.id = id

        val updateResponse = update.updateHandler(
            CommonChatsUpdateRequest(
                token = req.token,
                chat = req.chat,
            )
        )

        if (updateResponse.error != ErrorType.NoError) {
            return CommonChatsCreateResponse(updateResponse.error)
        }

        return CommonChatsCreateResponse(error = ErrorType.NoError, id = id)
    }


}