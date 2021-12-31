package com.seedcompany.cordtables.components.tables.common.site_text_translations

data class SiteTextTranslationInput(
  var language: String,
  var site_text: String,
  var translation: String?
)

data class SiteTextTranslation(
  var id: String,
  var language: String,
  var site_text: String,
  var translation: String,

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
  val coordinates: String? = null,
)
