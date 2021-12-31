import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePartnerExRequest {
  token: string;
  partner: {
    organization: string;
    active: boolean;
    financial_reporting_types: string;
    is_innovations_client: boolean;
    pmc_entity_code: string;
    point_of_contact: string;
    types: string;
  };
}
class CreatePartnerExResponse extends GenericResponse {
  partner: ScPartner;
}

class ScPartnerListRequest {
  token: string;
}

class ScPartnerListResponse {
  error: ErrorType;
  partners: ScPartner[];
}

class ScPartnerUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScPartnerUpdateResponse {
  error: ErrorType;
  partner: ScPartner | null = null;
}

class DeletePartnerExRequest {
  id: string;
  token: string;
}

class DeletePartnerExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-partners',
  styleUrl: 'sc-partners.css',
  shadow: true,
})
export class ScPartners {
  @State() partnersResponse: ScPartnerListResponse;

  newOrganization: string;
  newActive: boolean;
  newFinancial_reporting_types: string;
  newIs_innovations_client: boolean;
  newPmc_entity_code: string;
  newPoint_of_contact: string;
  newTypes: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScPartnerUpdateRequest, ScPartnerUpdateResponse>('sc/partners/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.partnersResponse = { error: ErrorType.NoError, partners: this.partnersResponse.partners.map(partner => (partner.id === id ? updateResponse.partner : partner)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePartnerExRequest, DeletePartnerExResponse>('sc/partners/delete', {
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
    this.partnersResponse = await fetchAs<ScPartnerListRequest, ScPartnerListResponse>('sc/partners/list', {
      token: globals.globalStore.state.token,
    });
  }

  organizationChange(event) {
    this.newOrganization = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  financial_reporting_typesChange(event) {
    this.newFinancial_reporting_types = event.target.value;
  }
  is_innovations_clientChange(event) {
    this.newIs_innovations_client = event.target.value;
  }
  pmc_entity_codeChange(event) {
    this.newPmc_entity_code = event.target.value;
  }
  point_of_contactChange(event) {
    this.newPoint_of_contact = event.target.value;
  }
  typesChange(event) {
    this.newTypes = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePartnerExRequest, CreatePartnerExResponse>('sc/partners/create-read', {
      token: globals.globalStore.state.token,
      partner: {
        organization: this.newOrganization,
        active: this.newActive,
        financial_reporting_types: this.newFinancial_reporting_types,
        is_innovations_client: this.newIs_innovations_client,
        pmc_entity_code: this.newPmc_entity_code,
        point_of_contact: this.newPoint_of_contact,
        types: this.newTypes,
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
      field: 'organization',
      displayName: 'Organization',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'active',
      displayName: 'Active',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'financial_reporting_types',
      displayName: 'Financial Reporting Types',
      width: 200,
      editable: true,
      isMulti: true,
      selectOptions: [
        { display: 'Funded', value: 'Funded' },
        { display: 'FieldEngaged', value: 'FieldEngaged' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_innovations_client',
      displayName: 'Is Innovations Client',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'pmc_entity_code',
      displayName: 'PMC Entity Code',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'point_of_contact',
      displayName: 'Point Of Contact',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'types',
      displayName: 'Types',
      width: 200,
      editable: true,
      isMulti: true,
      selectOptions: [
        { display: 'Managing', value: 'Managing' },
        { display: 'Funding', value: 'Funding' },
        { display: 'Impact', value: 'Impact' },
        { display: 'Technical', value: 'Technical' },
        { display: 'Resource', value: 'Resource' },
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
        {this.partnersResponse && <cf-table rowData={this.partnersResponse.partners} columnData={this.columnData}></cf-table>}

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

            <div id="active-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="active">Active</label>
              </span>
              <span class="form-thing">
                <select id="active" name="active" onInput={event => this.activeChange(event)}>
                  <option value="">Select Active</option>
                  <option value="true" selected={this.newActive === true}>
                    True
                  </option>
                  <option value="false" selected={this.newActive === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="financial_reporting_types-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="financial_reporting_types">Financial Reporting Types</label>
              </span>
              <span class="form-thing">
                <select id="financial_reporting_types" name="financial_reporting_types" multiple onInput={event => this.financial_reporting_typesChange(event)}>
                  <option value="">Select Financial Reporting Types</option>
                  <option value="Funded" selected={this.newFinancial_reporting_types === 'Funded'}>
                    Funded
                  </option>
                  <option value="FieldEngaged" selected={this.newFinancial_reporting_types === 'FieldEngaged'}>
                    FieldEngaged
                  </option>
                </select>
              </span>
            </div>

            <div id="is_innovations_client-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_innovations_client">Is Innovations Client</label>
              </span>
              <span class="form-thing">
                <select id="is_innovations_client" name="is_innovations_client" onInput={event => this.is_innovations_clientChange(event)}>
                  <option value="">Select Is Innovations Client</option>
                  <option value="true" selected={this.newIs_innovations_client === true}>
                    True
                  </option>
                  <option value="false" selected={this.newIs_innovations_client === false}>
                    False
                  </option>
                </select>
              </span>
            </div>

            <div id="pmc_entity_code-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="pmc_entity_code">PMC Entity Code</label>
              </span>
              <span class="form-thing">
                <input type="text" id="pmc_entity_code" name="pmc_entity_code" onInput={event => this.pmc_entity_codeChange(event)} />
              </span>
            </div>

            <div id="point_of_contact-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="point_of_contact">Point Of Contact</label>
              </span>
              <span class="form-thing">
                <input type="text" id="point_of_contact" name="point_of_contact" onInput={event => this.point_of_contactChange(event)} />
              </span>
            </div>

            <div id="types-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="types">Types</label>
              </span>
              <span class="form-thing">
                <select id="types" name="types" multiple onInput={event => this.typesChange(event)}>
                  <option value="">Select Types</option>
                  <option value="Managing" selected={this.newTypes === 'Managing'}>
                    Managing
                  </option>
                  <option value="Funding" selected={this.newTypes === 'Funding'}>
                    Funding
                  </option>
                  <option value="Impact" selected={this.newTypes === 'Impact'}>
                    Impact
                  </option>
                  <option value="Technical" selected={this.newTypes === 'Technical'}>
                    Technical
                  </option>
                  <option value="Resource" selected={this.newTypes === 'Resource'}>
                    Resource
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
