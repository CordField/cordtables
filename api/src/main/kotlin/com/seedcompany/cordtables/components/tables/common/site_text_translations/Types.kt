package com.seedcompany.cordtables.components.tables.common.site_text_translations

data class SiteTextTranslationInput(
  var language: Int,
  var site_text: Int,
  var translation: String?
)

data class SiteTextTranslation(
  var id: String,
  var language: Int,
  var site_text: Int,
  var translation: String,

  val created_at: String? = null,
  val created_by: Int? = null,
  val modified_at: String? = null,
  val modified_by: Int? = null,
  val owning_person: Int? = null,
  val owning_group: Int? = null,
  val coordinates: String? = null,
)
