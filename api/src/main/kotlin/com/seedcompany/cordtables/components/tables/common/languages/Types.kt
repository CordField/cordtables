package com.seedcompany.cordtables.components.tables.common.languages

data class Language (
  var id: String? = null,

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)

data class LanguageInput (
  var id: String? = null,

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)
