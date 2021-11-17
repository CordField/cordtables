import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';


class CommonDiscussionChannels {
  id : number;
  name : string;
  created_at : string;
  created_by : number;
  modified_at : string;
  modified_by : number;
  owning_person : number;
  owning_group : number;
}

class CreateDiscussionChannelRequest {
  token: string;
  discussionchannel: {
    name: string;
  };

}

class CreateDiscussionChannelResponse extends GenericResponse {
  discussionchannel: CommonDiscussionChannels;
}

class CommonDiscussionChannelListRequest {
  token: string;
}

class CommonDiscussionChannelListResponse {
  error: ErrorType;
  discussionchannel: CommonDiscussionChannels[];
}

class CommonDiscussionChannelUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: number;
}

class CommonDiscussionChannelUpdateResponse {
  error: ErrorType;
  discussionchannel: CommonDiscussionChannels | null = null;
}

class DeleteDiscussionChannelRequest {
  id: number;
  token: string;
}

class DeleteDiscussionChannelResponse extends GenericResponse {
  id: number;
}

@Component({
  tag: 'discussion-channels',
  styleUrl: 'discussion-channels.css',
  shadow: true,
})
export class DiscussionChannels {
  @State() discussionchannelsResponse: CommonDiscussionChannelListResponse;
  newDiscussionChannelName: string;

  handleUpdate = async (id: number, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<CommonDiscussionChannelUpdateRequest, CommonDiscussionChannelUpdateResponse>('common-discussion-channels/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.discussionchannelsResponse = { error: ErrorType.NoError, discussionchannel: this.discussionchannelsResponse.discussionchannel.map(discussionchannel => (discussionchannel.id === id ? updateResponse.discussionchannel : discussionchannel)) };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
};

handleDelete = async id => {
  const deleteResponse = await fetchAs<DeleteDiscussionChannelRequest, DeleteDiscussionChannelResponse>('common-dicsussion-channels/delete', {
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
  this.discussionchannelsResponse = await fetchAs<CommonDiscussionChannelListRequest, CommonDiscussionChannelListResponse>('common-discussion-channels/list', {
    token: globals.globalStore.state.token,
  });
}

discussionchannelNameChange(event) {
  this.newDiscussionChannelName = event.target.value;
}

handleInsert = async (event: MouseEvent) => {
  event.preventDefault();
  event.stopPropagation();

  const createResponse = await fetchAs<CreateDiscussionChannelRequest, CreateDiscussionChannelResponse>('common-discussion-channels/create-read', {
    token: globals.globalStore.state.token,
    discussionchannel: {
      name: this.newDiscussionChannelName,
    },
    },
  );

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
    field: 'name',
    displayName: 'Name',
    width: 200,
    editable: false,
    deleteFn: this.handleDelete,
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

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.discussionchannelsResponse && <cf-table rowData={this.discussionchannelsResponse.discussionchannel} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="discussionchannel-name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="discussionchannel-name">New Discussion Channel Name</label>
              </span>
              <span class="form-thing">
                <input type="text" id="discussionchannel-name" name="discussion-channel-name" onInput={event => this.discussionchannelNameChange(event)} />
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
