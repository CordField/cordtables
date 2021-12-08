package com.seedcompany.cordtables.services

data class LanguageIndexKey (
  var lang: String,
  var country: String,
  var name_type: String,
  var name: String,
)

data class SiteTextTranslationInput (
  val language: Int,
  val site_text: Int,
  val translation: String,
  val token: String,
)
