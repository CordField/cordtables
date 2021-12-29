import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProductExRequest {
  token: string;
  product: {
    neo4j_id: string;
    name: string;
    change_to_plan: number;
    active: boolean;
    mediums: string[];
    methodologies: string;
    purposes: string;
    type: string;
  };
}
class CreateProductExResponse extends GenericResponse {
  product: ScProduct;
}

class ScProductListRequest {
  token: string;
}

class ScProductListResponse {
  error: ErrorType;
  products: ScProduct[];
}

class ScProductUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScProductUpdateResponse {
  error: ErrorType;
  product: ScProduct | null = null;
}

class DeleteProductExRequest {
  id: number;
  token: string;
}

class DeleteProductExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-products',
  styleUrl: 'sc-products.css',
  shadow: true,
})
export class ScProducts {
  @State() productsResponse: ScProductListResponse;

  newNeo4j_id: string;
  newName: string;
  newChange_to_plan: number;
  newActive: boolean;
  newMediums: string[] = [];
  newMethodologies: string;
  newPurposes: string;
  newType: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScProductUpdateRequest, ScProductUpdateResponse>('sc/products/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.productsResponse = { error: ErrorType.NoError, products: this.productsResponse.products.map(product => (product.id === id ? updateResponse.product : product)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteProductExRequest, DeleteProductExResponse>('sc/products/delete', {
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
    this.productsResponse = await fetchAs<ScProductListRequest, ScProductListResponse>('sc/products/list', {
      token: globals.globalStore.state.token,
    });
  }

  neo4j_idChange(event) {
    this.newNeo4j_id = event.target.value;
  }

  nameChange(event) {
    this.newName = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  mediumsChange(event) {
    var options = event.target.options;
    var value = [];
    for (var i = 0, l = options.length; i < l; i++) {
      if (options[i].selected) {
        value.push(options[i].value);
      }
    }
    this.newMediums = value;
  }

  methodologiesChange(event) {
    this.newMethodologies = event.target.value;
  }

  purposesChange(event) {
    this.newPurposes = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateProductExRequest, CreateProductExResponse>('sc/products/create-read', {
      token: globals.globalStore.state.token,
      product: {
        neo4j_id: this.newNeo4j_id,
        name: this.newName,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        mediums: this.newMediums,
        methodologies: this.newMethodologies,
        purposes: this.newPurposes,
        type: this.newType,
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
      field: 'neo4j_id',
      displayName: 'neo4j_id',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'name',
      displayName: 'Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'change_to_plan',
      displayName: 'Change To Plan',
      width: 200,
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
      field: 'mediums',
      displayName: 'Mediums',
      width: 200,
      editable: true,
      isMulti: true,
      selectOptions: [
        { display: 'A', value: 'A' },
        { display: 'B', value: 'B' },
        { display: 'C', value: 'C' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'methodologies',
      displayName: 'Methodologies',
      width: 250,
      editable: true,
      isMulti: true,
      selectOptions: [
        { display: 'A', value: 'A' },
        { display: 'B', value: 'B' },
        { display: 'C', value: 'C' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'purposes',
      displayName: 'Purposes',
      width: 250,
      editable: true,
      isMulti: true,
      selectOptions: [
        { display: 'A', value: 'A' },
        { display: 'B', value: 'B' },
        { display: 'C', value: 'C' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'Film', value: 'Film' },
        { display: 'Literacy Material', value: 'Literacy Material' },
        { display: 'Scripture', value: 'Scripture' },
        { display: 'Song', value: 'Song' },
        { display: 'Story', value: 'Story' },
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
        {this.productsResponse && <cf-table rowData={this.productsResponse.products} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="neo4j_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="neo4j_id">neo4j_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="neo4j_id" name="neo4j_id" onInput={event => this.neo4j_idChange(event)} />
              </span>
            </div>

            <div id="name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="name">Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="name" name="name" onInput={event => this.nameChange(event)} />
              </span>
            </div>

            <div id="change_to_plan-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="change_to_plan">Change To Plan</label>
              </span>
              <span class="form-thing">
                <input type="number" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
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

            <div id="mediums-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="mediums">Mediums</label>
              </span>
              <span class="form-thing">
                <select id="mediums" name="mediums" multiple onInput={event => this.mediumsChange(event)}>
                  <option value="">Select Mediums</option>
                  <option value="A" selected={this.newMediums.includes('A')}>
                    A
                  </option>
                  <option value="B" selected={this.newMediums.includes('B')}>
                    B
                  </option>
                  <option value="C" selected={this.newMediums.includes('B')}>
                    C
                  </option>
                </select>
              </span>
            </div>

            <div id="methodologies-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="methodologies">Methodologies</label>
              </span>
              <span class="form-thing">
                <select id="methodologies" name="methodologies" multiple onInput={event => this.methodologiesChange(event)}>
                  <option value="">Select Methodologies</option>
                  <option value="A" selected={this.newMethodologies === 'A'}>
                    A
                  </option>
                  <option value="B" selected={this.newMethodologies === 'B'}>
                    B
                  </option>
                  <option value="C" selected={this.newMethodologies === 'C'}>
                    C
                  </option>
                </select>
              </span>
            </div>

            <div id="purposes-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="purposes">Purposes</label>
              </span>
              <span class="form-thing">
                <select id="purposes" name="purposes" multiple onInput={event => this.purposesChange(event)}>
                  <option value="">Select Purposes</option>
                  <option value="A" selected={this.newPurposes === 'A'}>
                    A
                  </option>
                  <option value="B" selected={this.newPurposes === 'B'}>
                    B
                  </option>
                  <option value="C" selected={this.newPurposes === 'C'}>
                    C
                  </option>
                </select>
              </span>
            </div>

            <div id="type-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="type">Type</label>
              </span>
              <span class="form-thing">
                <select id="type" name="type" onInput={event => this.typeChange(event)}>
                  <option value="">Select Type</option>
                  <option value="Film" selected={this.newType === 'Film'}>
                    Film
                  </option>
                  <option value="Literacy Material" selected={this.newType === 'Literacy Material'}>
                    Literacy Material
                  </option>
                  <option value="Scripture" selected={this.newType === 'Scripture'}>
                    Scripture
                  </option>
                  <option value="Song" selected={this.newType === 'Song'}>
                    Song
                  </option>
                  <option value="Story" selected={this.newType === 'Story'}>
                    Story
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
