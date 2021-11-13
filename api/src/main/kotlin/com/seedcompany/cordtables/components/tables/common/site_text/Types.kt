package com.seedcompany.cordtables.components.tables.common.site_text

data class CommonSiteText(
        var id: Int? = null,

        val ethnologue: Int? = null,
        val text_id: String? = null,
        val text_translation: String? = null,

        val created_at: String? = null,
        val created_by: Int? = null,
        val modified_at: String? = null,
        val modified_by: Int? = null,
        val owning_person: Int? = null,
        val owning_group: Int? = null,
        val coordinates: String? = null,
)

data class CommonSiteTextInput(
        val ethnologue: Int,
        val text_id: String,
        val text_translation: String,
)