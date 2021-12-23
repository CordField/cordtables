package com.seedcompany.cordtables.components.tables.common.site_text_strings

data class SiteTextStringInput(
  var english: String,
  var comment: String?
)

data class SiteTextString(
  var id: String,
  var english: String,
  var comment: String?,

  val created_at: String? = null,
  val created_by: Int? = null,
  val modified_at: String? = null,
  val modified_by: Int? = null,
  val owning_person: Int? = null,
  val owning_group: Int? = null,
  val coordinates: String? = null,
)
