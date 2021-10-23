export class ColumnDescription {
  field: keyof any;
  displayName: string;
  width: number;
  editable: boolean;
  updateFn?: (id: number, columnName: any, value: any) => Promise<boolean>;
  selectOptions?: Array<{ display: string; value: any }> | null = null;
}
