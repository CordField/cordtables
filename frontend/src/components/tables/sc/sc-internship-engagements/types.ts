class ScInternshipEngagement {
  id?: string | undefined;

  project?: string | undefined;
  ethnologue?: string | undefined;
  change_to_plan?: string | undefined;
  active?: boolean | undefined; // bool,
  communications_complete?: string | undefined; //_date timestamp,
  complete_date?: string | undefined; // timestamp,
  country_of_origin?: string | undefined; // int references common.locations(id),
  disbursement_complete_date?: string | undefined; // timestamp,
  end_date?: string | undefined; // timestamp,
  end_date_override?: string | undefined; // timestamp,
  growth_plan?: string | undefined; // int references common.file_versions(id),
  initial_end_date?: string | undefined; // timestamp,
  intern?: string | undefined; // int references admin.people(id),
  last_reactivated_at?: string | undefined; // timestamp,
  mentor?: string | undefined; // int references admin.people(id),
  methodology?: string | undefined; // common.internship_methodology,
  paratext_registry?: string | undefined; // varchar(32),
  periodic_reports_directory?: string | undefined; // int references sc.periodic_reports_directory(id),
  position?: string | undefined; // common.internship_position,
  start_date?: string | undefined; // timestamp,
  start_date_override?: string | undefined; // timestamp,
  status?: string | undefined; // common.engagement_status,

  created_at?: string | undefined;
  created_by?: string | undefined;
  modified_at?: string | undefined;
  modified_by?: string | undefined;
  owning_person?: string | undefined;
  owning_group?: string | undefined;
}
