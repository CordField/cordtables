package com.seedcompany.cordtables.components.tables.sc.partnerships

data class partnership(
    var id: String? = null,

    val project: Int? = null, // int not null references sc.projects(id),
    val partner: Int? = null, // int not null references sc.organizations(id),
    val change_to_plan: Int? = null, // int not null default 1 references sc.change_to_plans(id),
    val active: Boolean? = null, // bool,
    val agreement: Int? = null, // int references common.file_versions(id),

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

data class partnershipInput(
    var id: String? = null,

    val project: Int? = null, // int not null references sc.projects(id),
    val partner: Int? = null, // int not null references sc.organizations(id),
    val change_to_plan: Int? = null, // int not null default 1 references sc.change_to_plans(id),
    val active: Boolean? = null, // bool,
    val agreement: Int? = null, // int references common.file_versions(id),

    val created_at: String? = null,
    val created_by: Int? = null,
    val modified_at: String? = null,
    val modified_by: Int? = null,
    val owning_person: Int? = null,
    val owning_group: Int? = null,
)

//project int not null references sc.projects(id),
//partner int not null references sc.organizations(id),
//change_to_plan int not null default 1 references sc.change_to_plans(id),
//active bool,
//agreement int references common.file_versions(id),
