package com.seedcompany.cordtables.components.pages.translator

import com.seedcompany.cordtables.common.ErrorType

data class TranslatorReadRequest(
  val token: String? = null,
  val language:String,
)

data class TranslatorData(
  val id: String,
  val english: String,
  val comment: String?=null,
  val translation: String?=null,
)

data class TranslatorReadResponse(
  val error: ErrorType,
  val data: MutableList<TranslatorData>?=null,
)
