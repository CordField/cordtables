class ScInternshipEngagement {
  id?: string | undefined;

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
