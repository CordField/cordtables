export type ScBudgetRecord = {
    id?: number | undefined;
    neo4j_id?: string | undefined;
  
    budget?: number | undefined;
    change_to_plan?: number | undefined;
    active?: boolean | undefined;
    amount?: number | undefined;
    fiscal_year?: number | undefined;
    partnership?: number | undefined;
  
    created_at?: string | undefined;
    created_by?: number | undefined;
    modified_at?: string | undefined;
    modified_by?: number | undefined;
    owning_person?: number | undefined;
    owning_group?: number | undefined;
    peer?: number | undefined;
  }
  