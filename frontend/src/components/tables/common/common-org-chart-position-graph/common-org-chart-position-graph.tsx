import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateOrgChartPositionGraphExRequest {
  token: string;
  orgChartPositionGraph: {
    from_position: number;
    to_position: number;
    relationship_type: string;
  };
}
class CreateOrgChartPositionGraphExResponse extends GenericResponse {
  orgChartPositionGraph: CommonOrgChartPositionGraph;
}

class CommonOrgChartPositionGraphListRequest {
  token: string;
}

class CommonOrgChartPositionGraphListResponse {
  error: ErrorType;
  orgChartPositionGraphs: CommonOrgChartPositionGraph[];
}

class CommonOrgChartPositionGraphUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonOrgChartPositionGraphUpdateResponse {
  error: ErrorType;
  orgChartPositionGraph: CommonOrgChartPositionGraph | null = null;
}

class DeleteOrgChartPositionGraphExRequest {
  id: number;
  token: string;
}

class DeleteOrgChartPositionGraphExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'common-org-chart-position-graph',
  styleUrl: 'common-org-chart-position-graph.css',
  shadow: true,
})
export class CommonOrgChartPositionGraphs {
  @State() orgChartPositionGraphsResponse: CommonOrgChartPositionGraphListResponse;

  newFrom_position: number;
  newTo_position: number;
  newRelationship_type: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonOrgChartPositionGraphUpdateRequest, CommonOrgChartPositionGraphUpdateResponse>('common/org-chart-position-graph/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.orgChartPositionGraphsResponse = {
        error: ErrorType.NoError,
        orgChartPositionGraphs: this.orgChartPositionGraphsResponse.orgChartPositionGraphs.map(orgChartPositionGraph =>
          orgChartPositionGraph.id === id ? updateResponse.orgChartPositionGraph : orgChartPositionGraph,
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
    const deleteResponse = await fetchAs<DeleteOrgChartPositionGraphExRequest, DeleteOrgChartPositionGraphExResponse>('common/org-chart-position-graph/delete', {
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
    this.orgChartPositionGraphsResponse = await fetchAs<CommonOrgChartPositionGraphListRequest, CommonOrgChartPositionGraphListResponse>('common/org-chart-position-graph/list', {
      token: globals.globalStore.state.token,
    });
  }

  from_positionChange(event) {
    this.newFrom_position = event.target.value;
  }

  to_positionChange(event) {
    this.newTo_position = event.target.value;
  }

  relationship_typeChange(event) {
    this.newRelationship_type = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateOrgChartPositionGraphExRequest, CreateOrgChartPositionGraphExResponse>('common/org-chart-position-graph/create-read', {
      token: globals.globalStore.state.token,
      orgChartPositionGraph: {
        from_position: this.newFrom_position,
        to_position: this.newTo_position,
        relationship_type: this.newRelationship_type,
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
      field: 'from_position',
      displayName: 'From Position',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'relationship_type',
      displayName: 'Relationship Type',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'Reports To', value: 'Reports To' },
        { display: 'Works With', value: 'Works With' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'to_position',
      displayName: 'To Position',
      width: 200,
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
        {this.orgChartPositionGraphsResponse && <cf-table rowData={this.orgChartPositionGraphsResponse.orgChartPositionGraphs} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="from_position-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="from_position">From Position</label>
              </span>
              <span class="form-thing">
                <input type="number" id="from_position" name="from_position" onInput={event => this.from_positionChange(event)} />
              </span>
            </div>

            <div id="to_position-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="to_position">To Position</label>
              </span>
              <span class="form-thing">
                <input type="text" id="to_position" name="to_position" onInput={event => this.to_positionChange(event)} />
              </span>
            </div>

            <div id="relationship_type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="relationship_type">Relationship Type</label>
              </span>
              <span class="form-thing">
                <select id="relationship_type" name="relationship_type" onInput={event => this.relationship_typeChange(event)}>
                  <option value="">Select Relationship Type</option>
                  <option value="Reports To" selected={this.newRelationship_type === 'Reports To'}>
                    Reports To
                  </option>
                  <option value="Works With" selected={this.newRelationship_type === 'Works With'}>
                    Works With
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
