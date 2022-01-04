package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.MimeTypes

data class CommonFileVersion(
    var id: String? = null,
    val category: String? = null,
    val mime_type: MimeTypes? = null,
    val name: String? = null,
    val file: String? = null,
    val file_url: String? = null,
    val file_size: Int? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

data class CommonFileVersionInput(
    var id: String? = null,
    val category: String? = null,
    val mime_type: String? = null,
    val name: String? = null,
    val file: String? = null,
    val file_url: String? = null,
    val file_size: Int? = null,
    val created_at: String? = null,
    val created_by: String? = null,
    val modified_at: String? = null,
    val modified_by: String? = null,
    val owning_person: String? = null,
    val owning_group: String? = null,
)

