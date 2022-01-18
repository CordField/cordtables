export class ColumnDescription {
  field: keyof any;
  displayName: string;
  width: number;
  editable: boolean;
  isMulti?: boolean;
  updateFn?: (id: string, columnName: any, value: any) => Promise<boolean>;
  deleteFn?: (id: string) => Promise<boolean>;
  selectOptions?: Array<{ display: string; value: any }> | null = null;
  foreignKey?: string | null = null;
  foreignTableColumn?: string | null = null;
}

export type CellType = 'header' | 'data' | 'action';
