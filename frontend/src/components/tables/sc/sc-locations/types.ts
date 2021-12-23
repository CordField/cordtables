export type ScLocation = {
  id?: string | undefined;
  name?: string | undefined;

  default_region?: number | undefined;
  funding_account?: number | undefined;
  iso_alpha_3?: string | undefined;
  type?: string | undefined;

  created_at?: string | undefined;
  created_by?: number | undefined;
  modified_at?: string | undefined;
  modified_by?: number | undefined;
  owning_person?: number | undefined;
  owning_group?: number | undefined;
};
