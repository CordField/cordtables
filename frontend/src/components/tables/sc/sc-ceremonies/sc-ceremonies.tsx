import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateCermonyExRequest {
  token: string;
  cermony: {
    internship_engagement: string;
    language_engagement: string;
    ethnologue: string;
    actual_date: string;
    estimated_date: string;
    is_planned: boolean;
    type: string;
  };
}
class CreateCermonyExResponse extends GenericResponse {
  cermony: ScCermony;
}

class ScCermonyListRequest {
  token: string;
}

class ScCermonyListResponse {
  error: ErrorType;
  cermonies: ScCermony[];
}

class ScCermonyUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class ScCermonyUpdateResponse {
  error: ErrorType;
  cermony: ScCermony | null = null;
}

class DeleteCermonyExRequest {
  id: string;
  token: string;
}

class DeleteCermonyExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-ceremonies',
  styleUrl: 'sc-ceremonies.css',
  shadow: true,
})
export class ScCermonies {
  @State() cermoniesResponse: ScCermonyListResponse;

  newInternship_engagement: string;
  newLanguage_engagement: string;
  newEthnologue: string;
  newActual_date: string;
  newEstimated_date: string;
  newIs_planned: boolean;
  newType: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<ScCermonyUpdateRequest, ScCermonyUpdateResponse>('sc/ceremonies/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.cermoniesResponse = {
        error: ErrorType.NoError,
        cermonies: this.cermoniesResponse.cermonies.map(cermony => (cermony.id === id ? updateResponse.cermony : cermony)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteCermonyExRequest, DeleteCermonyExResponse>('sc/ceremonies/delete', {
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
    this.cermoniesResponse = await fetchAs<ScCermonyListRequest, ScCermonyListResponse>('sc/ceremonies/list', {
      token: globals.globalStore.state.token,
    });
  }


  internship_engagementChange(event) {
    this.newInternship_engagement = event.target.value;
  }

  language_engagementChange(event) {
    this.newLanguage_engagement = event.target.value;
  }

  ethnologueChange(event) {
    this.newEthnologue = event.target.value;
  }

  actual_dateChange(event) {
    this.newActual_date = event.target.value;
  }

  estimated_dateChange(event) {
    this.newEstimated_date = event.target.value;
  }

  is_plannedChange(event) {
    this.newIs_planned = event.target.value;
  }

  typeChange(event) {
    this.newType = event.target.value;
  }



  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateCermonyExRequest, CreateCermonyExResponse>('sc/ceremonies/create-read', {
      token: globals.globalStore.state.token,
      cermony: {
        internship_engagement: this.newInternship_engagement,
        language_engagement: this.newLanguage_engagement,
        ethnologue: this.newEthnologue,
        actual_date: this.newActual_date,
        estimated_date: this.newEstimated_date,
        is_planned: this.newIs_planned,
        type: this.newType
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

  internship_engagement: string;
    language_engagement: string;
    ethnologue: string;
    actual_date: string;
    estimated_date: string;
    is_planned: boolean;
    type: string;


  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'internship_engagement',
      displayName: 'Internship Engagement',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'language_engagement',
      displayName: 'Language Engagement',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'ethnologue',
      displayName: 'Ethnologue',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'actual_date',
      displayName: 'Actual Date',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'estimated_date',
      displayName: 'Estimated Date',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'is_planned',
      displayName: 'Is Planned',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'True', value: 'true' },
        { display: 'False', value: 'false' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'Dedication', value: 'Dedication' },
        { display: 'Certification', value: 'Certification' },
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
        {this.cermoniesResponse && <cf-table rowData={this.cermoniesResponse.cermonies} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="internship_engagement-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="internship_engagement">Internship Engagement</label>
              </span>
              <span class="form-thing">
                <input type="text" id="internship_engagement" name="internship_engagement" onInput={event => this.internship_engagementChange(event)} />
              </span>
            </div>

            <div id="language_engagement-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="language_engagement">Language Engagement</label>
              </span>
              <span class="form-thing">
                <input type="text" id="language_engagement" name="language_engagement" onInput={event => this.language_engagementChange(event)} />
              </span>
            </div>

            <div id="language_ethnologueengagement-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="ethnologue">Ethnologue</label>
              </span>
              <span class="form-thing">
                <input type="text" id="ethnologue" name="ethnologue" onInput={event => this.ethnologueChange(event)} />
              </span>
            </div>

            <div id="actual_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="actual_date">Actual Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="actual_date" name="actual_date" onInput={event => this.actual_dateChange(event)} />
              </span>
            </div>

            <div id="estimated_date-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="estimated_date">Estimated Date</label>
              </span>
              <span class="form-thing">
                <input type="text" id="estimated_date" name="estimated_date" onInput={event => this.estimated_dateChange(event)} />
              </span>
            </div>



            <div id="is_planned-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="is_planned">Is Planned</label>
              </span>
              <span class="form-thing">
                <select id="is_planned" name="is_planned" onInput={event => this.is_plannedChange(event)}>
                  <option value="">Select Is Planned</option>
                  <option value="true" selected={this.newIs_planned === true}>
                    True
                  </option>
                  <option value="false" selected={this.newIs_planned === false}>
                    False
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
                  <option value="Dedication" selected={this.newType === 'Dedication'}>Dedication</option>
                  <option value="Certification" selected={this.newType === 'Certification'}>Certification</option>
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
