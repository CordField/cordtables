alter table sc.funding_accounts alter column account_number set not null;
alter table sc.field_zones alter column name set not null;
alter table sc.field_regions alter column name set not null;
alter table sc.locations alter column name set not null;
alter table sc.locations alter column type set not null;
alter table sc.languages alter column ethnologue set not null;
alter table sc.languages alter column name set not null;
alter table sc.languages alter column display_name set not null;
alter table sc.periodic_reports alter column directory set not null;
alter table sc.periodic_reports alter column end_at set not null;
alter table sc.periodic_reports alter column report_file set not null;
alter table sc.periodic_reports alter column start_at set not null;
alter table sc.periodic_reports alter column type set not null;
alter table sc.projects alter column name set not null;
alter table sc.projects alter column change_to_plan set not null;
alter table sc.project_members alter column project set not null;
alter table sc.project_members alter column person set not null;
alter table sc.project_members alter column group_id set not null;
alter table sc.project_members alter column role set not null;
alter table sc.pinned_projects alter column person set not null;
alter table sc.pinned_projects alter column project set not null;
alter table sc.partnerships alter column project set not null;
alter table sc.partnerships alter column partner set not null;
alter table sc.partnerships alter column change_to_plan set not null;
alter table sc.budgets alter column change_to_plan set not null;
alter table sc.budgets alter column project set not null;
alter table sc.budget_records alter column budget set not null;
alter table sc.budget_records alter column change_to_plan set not null;
alter table sc.project_locations alter column change_to_plan set not null;
alter table sc.project_locations alter column location set not null;
alter table sc.project_locations alter column project set not null;
alter table sc.language_engagements alter column project set not null;
alter table sc.language_engagements alter column language set not null;
alter table sc.language_engagements alter column change_to_plan set not null;
alter table sc.products alter column name set not null;
alter table sc.products alter column change_to_plan set not null;
alter table sc.product_scripture_references alter column product set not null;
alter table sc.product_scripture_references alter column scripture_reference set not null;
alter table sc.product_scripture_references alter column change_to_plan set not null;
alter table sc.internship_engagements alter column project set not null;
alter table sc.internship_engagements alter column change_to_plan set not null;
