import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePeriodicReportExRequest {
  token: string;
  periodicReport: {
    directory: string;
    end_at: string;
    report_file: string;
    start_at: string;
    type: string;
    skipped_reason: string;
  };
}
class CreatePeriodicReportExResponse extends GenericResponse {
  periodicReport: ScPeriodicReport;
}

class ScPeriodicReportListRequest {
  token: string;
}

class ScPeriodicReportListResponse {
  error: ErrorType;
  periodicReports: ScPeriodicReport[];
}

class ScPeriodicReportUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScPeriodicReportUpdateResponse {
  error: ErrorType;
  periodicReport: ScPeriodicReport | null = null;
}

class DeletePeriodicReportExRequest {
  id: string;
  token: string;
}

class DeletePeriodicReportExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-periodic-reports',
  styleUrl: 'sc-periodic-reports.css',
  shadow: true,
})
export class ScPeriodicReports {
  @State() periodicReportsResponse: ScPeriodicReportListResponse;

  newDirectory: string;
  newEnd_at: string;
  newReport_file: string;
  newStart_at: string;
  newType: string;
  newSkipped_reason: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPeriodicReportUpdateRequest, ScPeriodicReportUpdateResponse>('sc/periodic-reports/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.periodicReportsResponse = {
        error: ErrorType.NoError,
        periodicReports: this.periodicReportsResponse.periodicReports.map(periodicReport => (periodicReport.id === id ? updateResponse.periodicReport : periodicReport)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePeriodicReportExRequest, DeletePeriodicReportExResponse>('sc/periodic-reports/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  async getList() {
    this.periodicReportsResponse = await fetchAs<ScPeriodicReportListRequest, ScPeriodicReportListResponse>('sc/periodic-reports/list', {
      token: globals.globalStore.state.token,
    });
  }

  directoryChange(event) {
    this.newDirectory = event.target.value;
  }

  end_atChange(event) {
    this.newEnd_at = event.target.value;
  }

  report_fileChange(event) {
    this.newReport_file = event.target.value;
  }

  start_atChange(event) {
    this.newStart_at = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }

  skipped_reasonChange(event) {
    this.newSkipped_reason = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePeriodicReportExRequest, CreatePeriodicReportExResponse>('sc/periodic-reports/create-read', {
      token: globals.globalStore.state.token,
      periodicReport: {
        directory: this.newDirectory,
        end_at: this.newEnd_at,
        report_file: this.newReport_file,
        start_at: this.newStart_at,
        type: this.newType,
        skipped_reason: this.newSkipped_reason
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  };

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'directory',
      displayName: 'Directory',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'end_at',
      displayName: 'End At',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'report_file',
      displayName: 'Report File',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'start_at',
      displayName: 'Start At',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 250,
      editable: true,
      selectOptions: [
        {display:'Financial', value: 'Financial'},
        {display:'Narrative', value: 'Narrative'},
        {display:'Progress', value: 'Progress'}
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'skipped_reason',
      displayName: 'Skipped Reason',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },



    {
      field: 'created_at',
      displayName: 'Created At',
      width: 250,
      editable: false,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 100,
      editable: false,
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 250,
      editable: false,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 100,
      editable: false,
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
  ];

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.periodicReportsResponse && <cf-table rowData={this.periodicReportsResponse.periodicReports} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="directory-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="directory">Directory</label>
              </span>
              <span class="form-thing">
                <input type="text" id="directory" name="directory" onInput={event => this.directoryChange(event)} />
              </span>
            </div>

            <div id="end_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="end_at">End At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="end_at" name="end_at" onInput={event => this.end_atChange(event)} />
              </span>
            </div>

            <div id="report_file-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="report_file">Report File</label>
              </span>
              <span class="form-thing">
                <input type="text" id="report_file" name="report_file" onInput={event => this.report_fileChange(event)} />
              </span>
            </div>

            <div id="start_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="start_at">Start At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="start_at" name="start_at" onInput={event => this.start_atChange(event)} />
              </span>
            </div>


            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <select id="type" name="type" onInput={event => this.typeChange(event)}>
                  <option value="">Select Type</option>
                  <option value="Financial" selected={this.newType === 'Financial'}>Financial</option>
                  <option value="Narrative" selected={this.newType === 'Narrative'}>Narrative</option>
                  <option value="Progress" selected={this.newType === 'Progress'}>Progress</option>
                </select>
              </span>
            </div>




            <div id="skipped_reason-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="skipped_reason">Skipped Reason</label>
              </span>
              <span class="form-thing">
                <input type="text" id="skipped_reason" name="skipped_reason" onInput={event => this.skipped_reasonChange(event)} />
              </span>
            </div>



            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}
