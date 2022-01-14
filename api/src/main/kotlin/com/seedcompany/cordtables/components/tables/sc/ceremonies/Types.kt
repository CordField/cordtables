package com.seedcompany.cordtables.components.tables.sc.ceremonies

data class ceremony(
  var id: String? = null,

  val internship_engagement: String? = null,
  val language_engagement: String? = null,
  val ethnologue: String? = null,
  val actual_date: String? = null,
  val estimated_date: String? = null,
  val is_planned: Boolean? = null,
  val type: String? = null,

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)

data class ceremonyInput(
  var id: String? = null,
  val internship_engagement: String? = null,
  val language_engagement: String? = null,
  val ethnologue: String? = null,
  val actual_date: String? = null,
  val estimated_date: String? = null,
  val is_planned: Boolean? = null,
  val type: String? = null,
  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)
