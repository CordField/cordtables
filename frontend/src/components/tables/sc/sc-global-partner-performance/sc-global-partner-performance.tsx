import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGlobalPartnerPerformanceExRequest {
  token: string;
  globalPartnerPerformance: {
    organization: string;
    reporting_performance: string;
    financial_performance: string;
    translation_performance: string;
  };
}
class CreateGlobalPartnerPerformanceExResponse extends GenericResponse {
  globalPartnerPerformance: ScGlobalPartnerPerformance;
}

class ScGlobalPartnerPerformanceListRequest {
  token: string;
}

class ScGlobalPartnerPerformanceListResponse {
  error: ErrorType;
  globalPartnerPerformances: ScGlobalPartnerPerformance[];
}

class ScGlobalPartnerPerformanceUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScGlobalPartnerPerformanceUpdateResponse {
  error: ErrorType;
  globalPartnerPerformance: ScGlobalPartnerPerformance | null = null;
}

class DeleteGlobalPartnerPerformanceExRequest {
  id: string;
  token: string;
}

class DeleteGlobalPartnerPerformanceExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-global-partner-performance',
  styleUrl: 'sc-global-partner-performance.css',
  shadow: true,
})
export class ScGlobalPartnerPerformances {
  @State() globalPartnerPerformancesResponse: ScGlobalPartnerPerformanceListResponse;

  newOrganization: string;
  newReporting_performance: string;
  newFinancial_performance: string;
  newTranslation_performance: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScGlobalPartnerPerformanceUpdateRequest, ScGlobalPartnerPerformanceUpdateResponse>('sc/global-partner-performance/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.globalPartnerPerformancesResponse = {
        error: ErrorType.NoError,
        globalPartnerPerformances: this.globalPartnerPerformancesResponse.globalPartnerPerformances.map(globalPartnerPerformance =>
          globalPartnerPerformance.id === id ? updateResponse.globalPartnerPerformance : globalPartnerPerformance,
        ),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGlobalPartnerPerformanceExRequest, DeleteGlobalPartnerPerformanceExResponse>('sc/global-partner-performance/delete', {
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
    this.globalPartnerPerformancesResponse = await fetchAs<ScGlobalPartnerPerformanceListRequest, ScGlobalPartnerPerformanceListResponse>('sc/global-partner-performance/list', {
      token: globals.globalStore.state.token,
    });
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  reporting_performanceChange(event) {
    this.newReporting_performance = event.target.value;
  }

  financial_performanceChange(event) {
    this.newFinancial_performance = event.target.value;
  }

  translation_performanceChange(event) {
    this.newTranslation_performance = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGlobalPartnerPerformanceExRequest, CreateGlobalPartnerPerformanceExResponse>('sc/global-partner-performance/create-read', {
      token: globals.globalStore.state.token,
      globalPartnerPerformance: {
        organization: this.newOrganization,
        reporting_performance: this.newReporting_performance,
        financial_performance: this.newFinancial_performance,
        translation_performance: this.newTranslation_performance,
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
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'organization',
      displayName: 'Organization',
      width: 150,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'reporting_performance',
      displayName: 'Reporting Performance',
      width: 200,
      editable: true,
      selectOptions: [
        { display: '1', value: '1' },
        { display: '2', value: '2' },
        { display: '3', value: '3' },
        { display: '4', value: '4' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'financial_performance',
      displayName: 'Financial Performance',
      width: 200,
      editable: true,
      selectOptions: [
        { display: '1', value: '1' },
        { display: '2', value: '2' },
        { display: '3', value: '3' },
        { display: '4', value: '4' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'translation_performance',
      displayName: 'Translation Performance',
      width: 200,
      editable: true,
      selectOptions: [
        { display: '1', value: '1' },
        { display: '2', value: '2' },
        { display: '3', value: '3' },
        { display: '4', value: '4' },
      ],
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
        {this.globalPartnerPerformancesResponse && <cf-table rowData={this.globalPartnerPerformancesResponse.globalPartnerPerformances} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="organization-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="organization">Organization</label>
              </span>
              <span class="form-thing">
                <input type="text" id="organization" name="organization" onInput={event => this.organizationChange(event)} />
              </span>
            </div>

            <div id="reporting_performance-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="reporting_performance">Reporting Performance</label>
              </span>
              <span class="form-thing">
                <select id="reporting_performance" name="reporting_performance" onInput={event => this.reporting_performanceChange(event)}>
                  <option value="">Select Reporting Performance</option>
                  <option value="1" selected={this.newReporting_performance === '1'}>
                    1
                  </option>
                  <option value="2" selected={this.newReporting_performance === '2'}>
                    2
                  </option>
                  <option value="3" selected={this.newReporting_performance === '3'}>
                    3
                  </option>
                  <option value="4" selected={this.newReporting_performance === '4'}>
                    4
                  </option>
                </select>
              </span>
            </div>

            <div id="financial_performance-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="financial_performance">Financial Performance</label>
              </span>
              <span class="form-thing">
                <select id="financial_performance" name="financial_performance" onInput={event => this.financial_performanceChange(event)}>
                  <option value="">Select Financial Performance</option>
                  <option value="1" selected={this.newFinancial_performance === '1'}>
                    1
                  </option>
                  <option value="2" selected={this.newFinancial_performance === '2'}>
                    2
                  </option>
                  <option value="3" selected={this.newFinancial_performance === '3'}>
                    3
                  </option>
                  <option value="4" selected={this.newFinancial_performance === '4'}>
                    4
                  </option>
                </select>
              </span>
            </div>

            <div id="translation_performance-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="translation_performance">Translation Performance</label>
              </span>
              <span class="form-thing">
                <select id="translation_performance" name="translation_performance" onInput={event => this.translation_performanceChange(event)}>
                  <option value="">Select Translation Performance</option>
                  <option value="1" selected={this.newTranslation_performance === '1'}>
                    1
                  </option>
                  <option value="2" selected={this.newTranslation_performance === '2'}>
                    2
                  </option>
                  <option value="3" selected={this.newTranslation_performance === '3'}>
                    3
                  </option>
                  <option value="4" selected={this.newTranslation_performance === '4'}>
                    4
                  </option>
                </select>
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
