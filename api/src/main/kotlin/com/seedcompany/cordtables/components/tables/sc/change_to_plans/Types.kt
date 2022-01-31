package com.seedcompany.cordtables.components.tables.sc.change_to_plans

data class changeToPlan(
  var id: String? = null,
  val status: String? = null,
  val summary: String? = null,
  val type: String? = null,
  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)

data class changeToPlanInput(
  var id: String? = null,
  val status: String? = null,
  val summary: String? = null,
  val type: String? = null,
  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)
