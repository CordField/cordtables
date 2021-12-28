import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProductScriptureReferenceExRequest {
  token: string;
  productScriptureReference: {
    product: number;
    scripture_reference: number;
    change_to_plan: number;
    active: boolean;
  };
}
class CreateProductScriptureReferenceExResponse extends GenericResponse {
  productScriptureReference: ScProductScriptureReference;
}

class ScProductScriptureReferenceListRequest {
  token: string;
}

class ScProductScriptureReferenceListResponse {
  error: ErrorType;
  productScriptureReferences: ScProductScriptureReference[];
}

class ScProductScriptureReferenceUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class ScProductScriptureReferenceUpdateResponse {
  error: ErrorType;
  productScriptureReference: ScProductScriptureReference | null = null;
}

class DeleteProductScriptureReferenceExRequest {
  id: number;
  token: string;
}

class DeleteProductScriptureReferenceExResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'sc-product-scripture-references',
  styleUrl: 'sc-product-scripture-references.css',
  shadow: true,
})
export class ScProductScriptureReferences {
  @State() productScriptureReferencesResponse: ScProductScriptureReferenceListResponse;

  newProduct: number;
  newScripture_reference: number;
  newChange_to_plan: number;
  newActive: boolean;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScProductScriptureReferenceUpdateRequest, ScProductScriptureReferenceUpdateResponse>('sc/product-scripture-references/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.productScriptureReferencesResponse = {
        error: ErrorType.NoError,
        productScriptureReferences: this.productScriptureReferencesResponse.productScriptureReferences.map(productScriptureReference =>
          productScriptureReference.id === id ? updateResponse.productScriptureReference : productScriptureReference,
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
    const deleteResponse = await fetchAs<DeleteProductScriptureReferenceExRequest, DeleteProductScriptureReferenceExResponse>('sc/product-scripture-references/delete', {
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
    this.productScriptureReferencesResponse = await fetchAs<ScProductScriptureReferenceListRequest, ScProductScriptureReferenceListResponse>(
      'sc/product-scripture-references/list',
      {
        token: globals.globalStore.state.token,
      },
    );
  }

  productChange(event) {
    this.newProduct = event.target.value;
  }

  scripture_referenceChange(event) {
    this.newScripture_reference = event.target.value;
  }

  change_to_planChange(event) {
    this.newChange_to_plan = event.target.value;
  }

  activeChange(event) {
    this.newActive = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateProductScriptureReferenceExRequest, CreateProductScriptureReferenceExResponse>('sc/product-scripture-references/create-read', {
      token: globals.globalStore.state.token,
      productScriptureReference: {
        product: this.newProduct,
        scripture_reference: this.newScripture_reference,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
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
      field: 'product',
      displayName: 'Product',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'scripture_reference',
      displayName: 'Scripture Reference',
      width: 250,
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
        {this.productScriptureReferencesResponse && <cf-table rowData={this.productScriptureReferencesResponse.productScriptureReferences} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="product-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="product">Product</label>
              </span>
              <span class="form-thing">
                <input type="number" id="product" name="product" onInput={event => this.productChange(event)} />
              </span>
            </div>

            <div id="scripture_reference-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="scripture_reference">Scripture Reference</label>
              </span>
              <span class="form-thing">
                <input type="text" id="scripture_reference" name="scripture_reference" onInput={event => this.scripture_referenceChange(event)} />
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

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}
