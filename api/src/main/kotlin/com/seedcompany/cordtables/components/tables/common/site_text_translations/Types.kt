package com.seedcompany.cordtables.components.tables.common.site_text_translations

data class CommonSiteTextTranslation(
        var id: Int? = null,

        val site_text_id: Int? = null,
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

data class CommonSiteTextTranslationInput(
        var id: Int? = null,
        val site_text_id: Int,
        val text_id: String,
        val text_translation: String,
)