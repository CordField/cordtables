import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreatePeopleExRequest {
  token: string;
  people: {
    about: string;
    phone: string;
    picture: string;
    private_first_name: string;
    private_last_name: string;
    public_first_name: string;
    public_last_name: string;
    primary_location: string;
    private_full_name: string;
    public_full_name: string;
    sensitivity_clearance: string;
    time_zone: string;
    title: string;
    status: string;
  };
}
class CreatePeopleExResponse extends GenericResponse {
  people: AdminPeople;
}

class AdminPeopleListRequest {
  token: string;
}

class AdminPeopleListResponse {
  error: ErrorType;
  peoples: AdminPeople[];
}


class AdminPeopleUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class AdminPeopleUpdateResponse {
  error: ErrorType;
  people: AdminPeople | null = null;
}

class DeletePeopleExRequest {
  id: string;
  token: string;
}

class DeletePeopleExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'admin-people',
  styleUrl: 'admin-people.css',
  shadow: true,
})
export class AdminPeoples {

  @State() peoplesResponse: AdminPeopleListResponse;

  newAbout: string;
  newPhone: string;
  newPicture: string;
  newPrivate_first_name: string;
  newPrivate_last_name: string;
  newPublic_first_name: string;
  newPublic_last_name: string;
  newPrimary_location: string;
  newPrivate_full_name: string;
  newPublic_full_name: string;
  newSensitivity_clearance: string;
  newTime_zone: string;
  newTitle: string;
  newStatus: string;
  
  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminPeopleUpdateRequest, AdminPeopleUpdateResponse>('admin-people/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.peoplesResponse = { error: ErrorType.NoError, peoples: this.peoplesResponse.peoples.map(people => (people.id === id ? updateResponse.people : people)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeletePeopleExRequest, DeletePeopleExResponse>('admin-people/delete', {
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
    this.peoplesResponse = await fetchAs<AdminPeopleListRequest, AdminPeopleListResponse>('admin-people/list', {
      token: globals.globalStore.state.token,
    });
  }

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }


  aboutChange(event) {
    this.newAbout = event.target.value;
  }

  phoneChange(event) {
    this.newPhone = event.target.value;
  }

  pictureChange(event) {
    this.newPicture = event.target.value;
  }

  private_first_nameChange(event) {
    this.newPrivate_first_name = event.target.value;
  }

  private_last_nameChange(event) {
    this.newPrivate_last_name = event.target.value;
  }

  public_first_nameChange(event) {
    this.newPublic_first_name = event.target.value;
  }

  public_last_nameChange(event) {
    this.newPublic_last_name = event.target.value;
  }

  primary_locationChange(event) {
    this.newPrimary_location = event.target.value;
  }

  private_full_nameChange(event) {
    this.newPrivate_full_name = event.target.value;
  }

  public_full_nameChange(event) {
    this.newPublic_full_name = event.target.value;
  }

  sensitivity_clearanceChange(event) {
    this.newSensitivity_clearance = event.target.value;
  }

  time_zoneChange(event) {
    this.newTime_zone = event.target.value;
  }

  titleChange(event) {
    this.newTitle = event.target.value;
  }

  statusChange(event) {
    this.newStatus = event.target.value;
  }


  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreatePeopleExRequest, CreatePeopleExResponse>('admin-people/create-read', {
      token: globals.globalStore.state.token,
      people: {
        about: this.newAbout,
        phone: this.newPhone,
        picture: this.newPicture,
        private_first_name: this.newPrivate_first_name,
        private_last_name: this.newPrivate_last_name,
        public_first_name: this.newPublic_first_name,
        public_last_name: this.newPublic_last_name,
        primary_location: this.newPrimary_location,
        private_full_name: this.newPrivate_full_name,
        public_full_name: this.newPublic_full_name,
        sensitivity_clearance: this.newSensitivity_clearance,
        time_zone: this.newTime_zone,
        title: this.newTitle,
        status: this.newStatus,
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
      field: 'about',
      displayName: 'About',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'phone',
      displayName: 'Phone',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'picture',
      displayName: 'Picture',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'private_first_name',
      displayName: 'Private First Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'private_last_name',
      displayName: 'Private Last Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'public_first_name',
      displayName: 'Public First Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'public_last_name',
      displayName: 'Public Last Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'primary_location',
      displayName: 'Primary Location',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'private_full_name',
      displayName: 'Private Full Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'public_full_name',
      displayName: 'Public Full Name',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'sensitivity_clearance',
      displayName: 'Sensitivity Clearance',
      width:200,
      editable: true,
      selectOptions: [
        {display: "Low", value: "Low"},
        {display: "Medium", value: "Medium"},
        {display: "High", value: "High"},
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'time_zone',
      displayName: 'Time Zone',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'title',
      displayName: 'Title',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'status',
      displayName: 'Status',
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
        {this.peoplesResponse && <cf-table rowData={this.peoplesResponse.peoples} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="about-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="about">About</label>
              </span>
              <span class="form-thing">
                <input type="text" id="about" name="about" onInput={event => this.aboutChange(event)} />
              </span>
            </div>

            <div id="phone-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="phone">Phone</label>
              </span>
              <span class="form-thing">
                <input type="text" id="phone" name="phone" onInput={event => this.phoneChange(event)} />
              </span>
            </div>

            <div id="picture-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="picture">Picture</label>
              </span>
              <span class="form-thing">
                <input type="text" id="picture" name="picture" onInput={event => this.pictureChange(event)} />
              </span>
            </div>

            <div id="private_first_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="private_first_name">Private First Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="private_first_name" name="private_first_name" onInput={event => this.private_first_nameChange(event)} />
              </span>
            </div>

            <div id="private_last_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="private_last_name">Private Last Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="private_last_name" name="private_last_name" onInput={event => this.private_last_nameChange(event)} />
              </span>
            </div>

            <div id="public_first_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="public_first_name">public_first_name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="public_first_name" name="public_first_name" onInput={event => this.public_first_nameChange(event)} />
              </span>
            </div>

            <div id="public_last_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="public_last_name">Public Last Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="public_last_name" name="public_last_name" onInput={event => this.public_last_nameChange(event)} />
              </span>
            </div>

            <div id="primary_location-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="primary_location">Primary Location</label>
              </span>
              <span class="form-thing">
                <input type="text" id="primary_location" name="primary_location" onInput={event => this.primary_locationChange(event)} />
              </span>
            </div>

            <div id="private_full_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="private_full_name">Private Full Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="private_full_name" name="private_full_name" onInput={event => this.private_full_nameChange(event)} />
              </span>
            </div>

            <div id="public_full_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="public_full_name">Public Full Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="public_full_name" name="public_full_name" onInput={event => this.public_full_nameChange(event)} />
              </span>
            </div>

            <div id="sensitivity_clearance-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="sensitivity_clearance">Sensitivity Clearance</label>
              </span>
              <span class="form-thing">
                <select id="sensitivity_clearance" name="sensitivity_clearance" onInput={event => this.sensitivity_clearanceChange(event)}>
                    <option value="">Select Sensitivity Clearance</option>
                    <option value="Low" selected={this.newSensitivity_clearance === "Low"}>Low</option>
                    <option value="Medium" selected={this.newSensitivity_clearance === "Medium"}>Medium</option>
                    <option value="High" selected={this.newSensitivity_clearance === "High"}>High</option>
                </select>
              </span>
            </div>

            <div id="time_zone-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="time_zone">Time Zone</label>
              </span>
              <span class="form-thing">
                <input type="text" id="time_zone" name="time_zone" onInput={event => this.time_zoneChange(event)} />
              </span>
            </div>

            <div id="title-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="title">title</label>
              </span>
              <span class="form-thing">
                <input type="text" id="title" name="title" onInput={event => this.titleChange(event)} />
              </span>
            </div>

            <div id="status-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="status">status</label>
              </span>
              <span class="form-thing">
                <input type="text" id="status" name="status" onInput={event => this.statusChange(event)} />
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