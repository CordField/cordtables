import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePeopleToOrgRelationshipExRequest {
  token: string;
  peopleToOrgRelationship: {
    org: number;
    person: number;
    relationship_type: string;
    begin_at: string;
    end_at: string;
  };
}
class CreatePeopleToOrgRelationshipExResponse extends GenericResponse {
  peopleToOrgRelationship: CommonPeopleToOrgRelationship;
}

class CommonPeopleToOrgRelationshipListRequest {
  token: string;
}

class CommonPeopleToOrgRelationshipListResponse {
  error: ErrorType;
  peopleToOrgRelationships: CommonPeopleToOrgRelationship[];
}

class CommonPeopleToOrgRelationshipUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class CommonPeopleToOrgRelationshipUpdateResponse {
  error: ErrorType;
  peopleToOrgRelationship: CommonPeopleToOrgRelationship | null = null;
}

class DeletePeopleToOrgRelationshipExRequest {
  id: string;
  token: string;
}

class DeletePeopleToOrgRelationshipExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'common-people-to-org-relationships',
  styleUrl: 'common-people-to-org-relationships.css',
  shadow: true,
})
export class CommonPeopleToOrgRelationships {
  @State() peopleToOrgRelationshipsResponse: CommonPeopleToOrgRelationshipListResponse;

  newOrg: number;
  newPerson: number;
  newRelationship_type: string;
  newBegin_at: string;
  newEnd_at: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonPeopleToOrgRelationshipUpdateRequest, CommonPeopleToOrgRelationshipUpdateResponse>(
      'common/people-to-org-relationships/update-read',
      {
        token: globals.globalStore.state.token,
        column: columnName,
        id: id,
        value: value !== '' ? value : null,
      },
    );

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.peopleToOrgRelationshipsResponse = {
        error: ErrorType.NoError,
        peopleToOrgRelationships: this.peopleToOrgRelationshipsResponse.peopleToOrgRelationships.map(peopleToOrgRelationship =>
          peopleToOrgRelationship.id === id ? updateResponse.peopleToOrgRelationship : peopleToOrgRelationship,
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
    const deleteResponse = await fetchAs<DeletePeopleToOrgRelationshipExRequest, DeletePeopleToOrgRelationshipExResponse>('common/people-to-org-relationships/delete', {
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
    this.peopleToOrgRelationshipsResponse = await fetchAs<CommonPeopleToOrgRelationshipListRequest, CommonPeopleToOrgRelationshipListResponse>(
      'common/people-to-org-relationships/list',
      {
        token: globals.globalStore.state.token,
      },
    );
  }

  orgChange(event) {
    this.newOrg = event.target.value;
  }

  personChange(event) {
    this.newPerson = event.target.value;
  }

  relationship_typeChange(event) {
    this.newRelationship_type = event.target.value;
  }

  begin_atChange(event) {
    this.newBegin_at = event.target.value;
  }

  end_atChange(event) {
    this.newEnd_at = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePeopleToOrgRelationshipExRequest, CreatePeopleToOrgRelationshipExResponse>('common/people-to-org-relationships/create-read', {
      token: globals.globalStore.state.token,
      peopleToOrgRelationship: {
        org: this.newOrg,
        person: this.newPerson,
        relationship_type: this.newRelationship_type,
        begin_at: this.newBegin_at,
        end_at: this.newEnd_at,
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
      field: 'org',
      displayName: 'Organization',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'person',
      displayName: 'Person',
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
        { display: 'Vendor', value: 'Vendor' },
        { display: 'Customer', value: 'Customer' },
        { display: 'Investor', value: 'Investor' },
        { display: 'Associate', value: 'Associate' },
        { display: 'Employee', value: 'Employee' },
        { display: 'Member', value: 'Member' },
        { display: 'Executive', value: 'Executive' },
        { display: 'President/CEO', value: 'President/CEO' },
        { display: 'Board of Directors', value: 'Board of Directors' },
        { display: 'Retired', value: 'Retired' },
        { display: 'Other', value: 'Other' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'begin_at',
      displayName: 'Begin At',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'end_at',
      displayName: 'End At',
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
        {this.peopleToOrgRelationshipsResponse && <cf-table rowData={this.peopleToOrgRelationshipsResponse.peopleToOrgRelationships} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="org-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="org">Organization</label>
              </span>
              <span class="form-thing">
                <input type="number" id="org" name="org" onInput={event => this.orgChange(event)} />
              </span>
            </div>

            <div id="person-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="person">Person</label>
              </span>
              <span class="form-thing">
                <input type="number" id="person" name="person" onInput={event => this.personChange(event)} />
              </span>
            </div>

            <div id="relationship_type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="relationship_type">Relationship Type</label>
              </span>
              <span class="form-thing">
                <select id="relationship_type" name="relationship_type" onInput={event => this.relationship_typeChange(event)}>
                  <option value="">Select Relationship Type</option>
                  <option value="Vendor" selected={this.newRelationship_type === 'Vendor'}>
                    Vendor
                  </option>
                  <option value="Customer" selected={this.newRelationship_type === 'Customer'}>
                    Customer
                  </option>
                  <option value="Investor" selected={this.newRelationship_type === 'Investor'}>
                    Investor
                  </option>
                  <option value="Associate" selected={this.newRelationship_type === 'Associate'}>
                    Associate
                  </option>
                  <option value="Employee" selected={this.newRelationship_type === 'Employee'}>
                    Employee
                  </option>
                  <option value="Member" selected={this.newRelationship_type === 'Member'}>
                    Member
                  </option>
                  <option value="Executive" selected={this.newRelationship_type === 'Executive'}>
                    Executive
                  </option>
                  <option value="President/CEO" selected={this.newRelationship_type === 'President/CEO'}>
                    President/CEO
                  </option>
                  <option value="Board of Directors" selected={this.newRelationship_type === 'Board of Directors'}>
                    Board of Directors
                  </option>
                  <option value="Retired" selected={this.newRelationship_type === 'Retired'}>
                    Retired
                  </option>
                  <option value="Other" selected={this.newRelationship_type === 'Other'}>
                    Other
                  </option>
                </select>
              </span>
            </div>

            <div id="begin_at-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="begin_at">Begin At</label>
              </span>
              <span class="form-thing">
                <input type="text" id="begin_at" name="begin_at" onInput={event => this.begin_atChange(event)} />
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

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}
