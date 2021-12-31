import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateProductExRequest {
  token: string;
  product: {
    name: string;
    change_to_plan: string;
    active: boolean;
    mediums: string;
    methodology: string;
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
  id: string;
}

class ScProductUpdateResponse {
  error: ErrorType;
  product: ScProduct | null = null;
}

class DeleteProductExRequest {
  id: string;
  token: string;
}

class DeleteProductExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'sc-products',
  styleUrl: 'sc-products.css',
  shadow: true,
})
export class ScProducts {
  @State() productsResponse: ScProductListResponse;

  newName: string;
  newChange_to_plan: string;
  newActive: boolean;
  newMediums: string;
  newMethodology: string;
  newPurposes: string;
  newType: string;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
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

  // async getFilesList() {
  //   this.filesResponse = await fetchAs<CommonFileListRequest, CommonFileListResponse>('common-files/list', {
  //     token: globals.globalStore.state.token,
  //   });
  // }

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
    // var options = event.target.options;
    // var value = [];
    // for (var i = 0, l = options.length; i < l; i++) {
    //   if (options[i].selected) {
    //     value.push(options[i].value);
    //     //console.log(options[i].value);
    //   }
    // }
    this.newMediums = event.target.value;
  }

  methodologyChange(event) {
    this.newMethodology = event.target.value;
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
        name: this.newName,
        change_to_plan: this.newChange_to_plan,
        active: this.newActive,
        mediums: this.newMediums,
        methodology: this.newMethodology,
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
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
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
        { display: 'Print', value: 'Print' },
        { display: 'Web', value: 'Web' },
        { display: 'EBook', value: 'EBook' },
        { display: 'App', value: 'App' },
        { display: 'TrainedStoryTellers', value: 'TrainedStoryTellers' },
        { display: 'Audio', value: 'Audio' },
        { display: 'Video', value: 'Video' },
        { display: 'Other', value: 'Other' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'methodology',
      displayName: 'Methodology',
      width: 250,
      editable: true,
      isMulti: false,
      selectOptions: [
        { display: 'Paratext', value: 'Paratext' },
        { display: 'OtherWritten', value: 'OtherWritten' },
        { display: 'Render', value: 'Render' },
        { display: 'Audacity', value: 'Audacity' },
        { display: 'AdobeAudition', value: 'AdobeAudition' },
        { display: 'OtherOralTranslation', value: 'OtherOralTranslation' },
        { display: 'StoryTogether', value: 'StoryTogether' },
        { display: 'SeedCompanyMethod', value: 'SeedCompanyMethod' },
        { display: 'OneStory', value: 'OneStory' },
        { display: 'Craft2Tell', value: 'Craft2Tell' },
        { display: 'OtherOralStories', value: 'OtherOralStories' },
        { display: 'Film', value: 'Film' },
        { display: 'SignLanguage', value: 'SignLanguage' },
        { display: 'OtherVisual', value: 'OtherVisual' },
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
        { display: 'EvangelismChurchPlanting', value: 'EvangelismChurchPlanting' },
        { display: 'ChurchLife', value: 'ChurchLife' },
        { display: 'ChurchMaturity', value: 'ChurchMaturity' },
        { display: 'SocialIssues', value: 'SocialIssues' },
        { display: 'Discipleship', value: 'Discipleship' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'type',
      displayName: 'Type',
      width: 250,
      editable: true,
      selectOptions: [
        { display: 'BibleStories', value: 'BibleStories' },
        { display: 'JesusFilm', value: 'JesusFilm' },
        { display: 'Songs', value: 'Songs' },
        { display: 'LiteracyMaterials', value: 'LiteracyMaterials' },
        { display: 'EthnoArts', value: 'EthnoArts' },
        { display: 'OldTestamentPortions', value: 'OldTestamentPortions' },
        { display: 'OldTestamentFull', value: 'OldTestamentFull' },
        { display: 'Gospel', value: 'Gospel' },
        { display: 'NewTestamentFull', value: 'NewTestamentFull' },
        { display: 'FullBible', value: 'FullBible' },
        { display: 'IndividualBooks', value: 'IndividualBooks' },
        { display: 'Genesis', value: 'Genesis' },
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
                <input type="text" id="change_to_plan" name="change_to_plan" onInput={event => this.change_to_planChange(event)} />
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
                  <option value="Print" selected={this.newMediums == 'Print'}>
                    Print
                  </option>
                  <option value="Web" selected={this.newMediums == 'Web'}>
                    Web
                  </option>
                  <option value="EBook" selected={this.newMediums == 'EBook'}>
                    EBook
                  </option>
                  <option value="App" selected={this.newMediums == 'App'}>
                    App
                  </option>
                  <option value="TrainedStoryTellers" selected={this.newMediums == 'TrainedStoryTellers'}>
                    TrainedStoryTellers
                  </option>
                  <option value="Audio" selected={this.newMediums == 'Audio'}>
                    Audio
                  </option>
                  <option value="Video" selected={this.newMediums == 'Video'}>
                    Video
                  </option>
                  <option value="Other" selected={this.newMediums == 'Other'}>
                    Other
                  </option>
                </select>
              </span>
            </div>

            <div id="methodology-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="methodology">Methodology</label>
              </span>
              <span class="form-thing">
                <select id="methodology" name="methodology" onInput={event => this.methodologyChange(event)}>
                  <option value="">Select Methodologies</option>
                  <option value="Paratext" selected={this.newMethodology === 'Paratext'}>
                    Paratext
                  </option>
                  <option value="OtherWritten" selected={this.newMethodology === 'OtherWritten'}>
                    OtherWritten
                  </option>
                  <option value="Render" selected={this.newMethodology === 'Render'}>
                    Render
                  </option>
                  <option value="Audacity" selected={this.newMethodology === 'Audacity'}>
                    Audacity
                  </option>
                  <option value="AdobeAudition" selected={this.newMethodology === 'AdobeAudition'}>
                    AdobeAudition
                  </option>
                  <option value="OtherOralTranslation" selected={this.newMethodology === 'OtherOralTranslation'}>
                    OtherOralTranslation
                  </option>
                  <option value="StoryTogether" selected={this.newMethodology === 'StoryTogether'}>
                    StoryTogether
                  </option>
                  <option value="SeedCompanyMethod" selected={this.newMethodology === 'SeedCompanyMethod'}>
                    SeedCompanyMethod
                  </option>
                  <option value="OneStory" selected={this.newMethodology === 'OneStory'}>
                    OneStory
                  </option>
                  <option value="Craft2Tell" selected={this.newMethodology === 'Craft2Tell'}>
                    Craft2Tell
                  </option>
                  <option value="OtherOralStories" selected={this.newMethodology === 'OtherOralStories'}>
                    OtherOralStories
                  </option>
                  <option value="Film" selected={this.newMethodology === 'Film'}>
                    Film
                  </option>
                  <option value="SignLanguage" selected={this.newMethodology === 'SignLanguage'}>
                    SignLanguage
                  </option>
                  <option value="OtherVisual" selected={this.newMethodology === 'OtherVisual'}>
                    OtherVisual
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
                  <option value="EvangelismChurchPlanting" selected={this.newPurposes === 'EvangelismChurchPlanting'}>
                    EvangelismChurchPlanting
                  </option>
                  <option value="ChurchLife" selected={this.newPurposes === 'ChurchLife'}>
                    ChurchLife
                  </option>
                  <option value="ChurchMaturity" selected={this.newPurposes === 'ChurchMaturity'}>
                    ChurchMaturity
                  </option>
                  <option value="SocialIssues" selected={this.newPurposes === 'SocialIssues'}>
                    SocialIssues
                  </option>
                  <option value="Discipleship" selected={this.newPurposes === 'Discipleship'}>
                    Discipleship
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
                  <option value="BibleStories" selected={this.newType === 'BibleStories'}>
                    BibleStories
                  </option>
                  <option value="JesusFilm" selected={this.newType === 'JesusFilm'}>
                    JesusFilm
                  </option>
                  <option value="Songs" selected={this.newType === 'Songs'}>
                    Songs
                  </option>
                  <option value="LiteracyMaterials" selected={this.newType === 'LiteracyMaterials'}>
                    LiteracyMaterials
                  </option>
                  <option value="EthnoArts" selected={this.newType === 'EthnoArts'}>
                    EthnoArts
                  </option>
                  <option value="OldTestamentPortions" selected={this.newType === 'OldTestamentPortions'}>
                    OldTestamentPortions
                  </option>
                  <option value="OldTestamentFull" selected={this.newType === 'OldTestamentFull'}>
                    OldTestamentFull
                  </option>
                  <option value="Gospel" selected={this.newType === 'Gospel'}>
                    Gospel
                  </option>
                  <option value="NewTestamentFull" selected={this.newType === 'NewTestamentFull'}>
                    NewTestamentFull
                  </option>
                  <option value="FullBible" selected={this.newType === 'FullBible'}>
                    FullBible
                  </option>
                  <option value="IndividualBooks" selected={this.newType === 'IndividualBooks'}>
                    IndividualBooks
                  </option>
                  <option value="Genesis" selected={this.newType === 'Genesis'}>
                    Genesis
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
