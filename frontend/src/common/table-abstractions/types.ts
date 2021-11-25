export class ColumnDescription {
  field: keyof any;
  displayName: string;
  width: number;
  editable: boolean;
  isMulti?: boolean;
  updateFn?: (id: number, columnName: any, value: any) => Promise<boolean>;
  deleteFn?: (id: number) => Promise<boolean>;
  selectOptions?: Array<{ display: string; value: any }> | null = null;
  
}

export type CellType = 'header' | 'data' | 'action';
