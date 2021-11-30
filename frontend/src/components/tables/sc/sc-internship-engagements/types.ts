class ScInternshipEngagement {
  id?: number | undefined;

  project?: number | undefined;
  ethnologue?: number | undefined;
  change_to_plan?: number | undefined;
  active?: boolean | undefined; // bool,
  communications_complete?: string | undefined; //_date timestamp,
  complete_date?: string | undefined; // timestamp,
  country_of_origin?: number | undefined; // int references common.locations(id),
  disbursement_complete_date?: string | undefined; // timestamp,
  end_date?: string | undefined; // timestamp,
  end_date_override?: string | undefined; // timestamp,
  growth_plan?: number | undefined; // int references common.file_versions(id),
  initial_end_date?: string | undefined; // timestamp,
  intern?: number | undefined; // int references admin.people(id),
  last_reactivated_at?: string | undefined; // timestamp,
  mentor?: number | undefined; // int references admin.people(id),
  methodology?: string | undefined; // common.internship_methodology,
  paratext_registry?: string | undefined; // varchar(32),
  periodic_reports_directory?: number | undefined; // int references sc.periodic_reports_directory(id),
  position?: string | undefined; // common.internship_position,
  start_date?: string | undefined; // timestamp,
  start_date_override?: string | undefined; // timestamp,
  status?: string | undefined; // common.engagement_status,

  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
}

// project int not null references sc.projects(id),
// ethnologue int not null references sc.ethnologue(id),
// change_to_plan int not null default 1 references sc.change_to_plans(id),
// active bool,
// communications_complete_date timestamp,
// complete_date timestamp,
// country_of_origin int references common.locations(id),
// disbursement_complete_date timestamp,
// end_date timestamp,
// end_date_override timestamp,
// growth_plan int references common.file_versions(id),
// initial_end_date timestamp,
// intern int references admin.people(id),
// last_reactivated_at timestamp,
// mentor int references admin.people(id),
// methodology common.internship_methodology,
// paratext_registry varchar(32),
// periodic_reports_directory int references sc.periodic_reports_directory(id),
// position common.internship_position,
// start_date timestamp,
// start_date_override timestamp,
// status common.engagement_status,
