package com.seedcompany.cordtables.components.tables.sc.budgets

import software.amazon.ion.Decimal
import java.math.BigDecimal

data class budget(
  var id: String? = null,

  val change_to_plan: String? = null,
  val project: String? = null,
  val status: String? = null,
  val universal_template: String? = null,
  val universal_template_file_url: String? = null,
  val sensitivity: String? = null,
  val total: String? = null,

  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)

data class budgetInput(
  var id: String? = null,
  val change_to_plan: String? = null,
  val project: String? = null,
  val status: String? = null,
  val universal_template: String? = null,
  val universal_template_file_url: String? = null,
  val sensitivity: String? = null,
  val total: String? = null,
  val created_at: String? = null,
  val created_by: String? = null,
  val modified_at: String? = null,
  val modified_by: String? = null,
  val owning_person: String? = null,
  val owning_group: String? = null,
)
