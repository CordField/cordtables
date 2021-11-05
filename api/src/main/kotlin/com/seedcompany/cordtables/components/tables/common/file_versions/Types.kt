package com.seedcompany.cordtables.components.tables.common.file_versions

import com.seedcompany.cordtables.common.MimeTypes

data class CommonFileVersion(
    var id: Int? = null,
    val category: String? = null,
    val mime_type: MimeTypes? = null,
    val name: String? = null,
    val file: Int? = null,
    val file_url: String? = null,
    val file_size: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

data class CommonFileVersionInput(
    var id: Int? = null,
    val category: String? = null,
    val mime_type: String? = null,
    val name: String? = null,
    val file: Int? = null,
    val file_url: String? = null,
    val file_size: Int? = null,
    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
    val peer: Int? = null,
)

