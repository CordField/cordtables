import { Component, Host, h, State, Listen } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse, SiteTextLanguage, SiteTextString, SiteTextTranslation } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';
import { capitalize, capitalizePhrase } from '../../../../common/utility';
import { t } from '../../../../core/site-text.service';
import { siteTextService } from "../../../../core/site-text.service";

type SiteTextStringUpdateInput = {
  id: string;
  column: string;
  newValue: string;
};

type SiteTextStringUpdateRequest = {
  token: string;
  site_text_string: SiteTextStringUpdateInput;
};

type SiteTextStringUpdateResponse = {
  error: ErrorType;
  site_text_string: SiteTextString;
};

type SiteTextStringCreateInput = {
  english: string;
  comment: string;
};

type SiteTextStringCreateRequest = {
  token: string;
  site_text_string: SiteTextStringCreateInput;
};

type SiteTextStringCreateResponse = {
  error: ErrorType;
  site_text_string: SiteTextString;
};

type SiteTextStringDeleteRequest = {
  token: string;
  id: string;
};

type SiteTextStringDeleteResponse = GenericResponse;

type SiteTextStringListResponse = {
  data: SiteTextString[];
  error: ErrorType
}

type SiteTextStringListRequest = {
  token: string;
}

@Component({
  tag: 'site-text-strings',
  styleUrl: 'site-text-strings.css',
  shadow: true,
})
export class SiteTextStrings {
  @State() siteTextStrings: SiteTextString[];
  @State() newSiteText: SiteTextStringCreateInput = {
    english: '',
    comment: '',
  };
  
  @Listen('searchResults')
  async showSearchResults(event: CustomEvent<any>) {
    this.siteTextStrings = event.detail as SiteTextString[];
  }
  newEnglish: string;
  newComment: string;

  handleUpdate = async (id: string, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextStringUpdateRequest, SiteTextStringUpdateResponse>('common/site-text-strings/update-read', {
      token: globals.globalStore.state.token,
      site_text_string: {
        column,
        id,
        newValue,
      },
    });

    if (updateResponse.error == ErrorType.NoError) {
      const oldValue = globals.globalStore.state.siteTextStrings.find((siteTextString: SiteTextString) => siteTextString.id === id).english;
      this.siteTextStrings = this.siteTextStrings.map((siteTextString: SiteTextString) => {
        if (siteTextString.id === updateResponse.site_text_string.id) return updateResponse.site_text_string;
        else return siteTextString;
      });

      globals.globalStore.set(
        'siteTextStrings',
        globals.globalStore.state.siteTextStrings.map((siteTextString: SiteTextString) => {
          if (siteTextString.id === updateResponse.site_text_string.id) return updateResponse.site_text_string;
          else return siteTextString;
        }),
      );

      if (column === 'english') {
        const newTranslations = { ...globals.globalStore.state.siteTextTranslations };
        Object.keys(newTranslations).map(language => {
          newTranslations[language][newValue] = newTranslations[language][oldValue];
          delete newTranslations[language][oldValue];
          return null;
        });
        globals.globalStore.set('siteTextTranslations', newTranslations);
      }
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async (id: string): Promise<boolean> => {
    const response = await fetchAs<SiteTextStringDeleteRequest, SiteTextStringDeleteResponse>('common/site-text-strings/delete', {
      token: globals.globalStore.state.token,
      id,
    });

    if (response.error == ErrorType.NoError) {
      const key = globals.globalStore.state.siteTextStrings.find((siteTextString: SiteTextString) => siteTextString.id === id).english;
      const newTranslations = { ...globals.globalStore.state.siteTextTranslations };
      Object.keys(newTranslations).map(language => {
        delete newTranslations[language][key];
        return null;
      });
      globals.globalStore.set('siteTextTranslations', newTranslations);
      this.siteTextStrings = this.siteTextStrings.filter((siteTextString: SiteTextString) => siteTextString.id !== id);
      globals.globalStore.set(
        'siteTextStrings',
        globals.globalStore.state.siteTextStrings.filter((siteTextString: SiteTextString) => siteTextString.id !== id),
      );
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: response.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  getList = () => {
    fetchAs<SiteTextStringListRequest, SiteTextStringListResponse>('common/site-text-strings/list', {
      token: globals.globalStore.state.token,
    })
    .then(res => {
      if (res.error === ErrorType.NoError) this.siteTextStrings = res.data;
    })
    .catch(error => {
      console.debug('site text strings error ===>', error);
    });
  }

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'english',
      displayName: capitalize(t('english')),
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'comment',
      displayName: capitalize(t('comment')),
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
    this.getList();
  }

  handleInputChange = event => {
    this.newSiteText = { ...this.newSiteText, [event.target.name]: event.target.value };
  };

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    if (this.newSiteText.english === '') return;
    const createResponse = await fetchAs<SiteTextStringCreateRequest, SiteTextStringCreateResponse>('common/site-text-strings/create-read', {
      token: globals.globalStore.state.token,
      site_text_string: {
        english: this.newSiteText.english,
        comment: this.newSiteText.comment,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.set('siteTextStrings', [createResponse.site_text_string, ...globals.globalStore.state.siteTextStrings]);
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
      return;
    }
    this.newSiteText = { english: '', comment: '' };
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <search-form
          columnNames={[
            'id',
            'english',
            'comment',
            'created_at',
            'created_by',
            'modified_at',
            'modified_by',
            'owning_person',
            'owning_group',
          ]}
        ></search-form>
        {/* table abstraction */}
        {this.siteTextStrings && <cf-table rowData={this.siteTextStrings} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
            <form class="form-thing">
              <div id="new-site-text-string-holder" class="form-input-item form-thing">
                <span class="form-thing">
                  <label htmlFor="new-site-text-string">{capitalize(t('english'))}</label>
                </span>
                <span class="form-thing">
                  <input type="text" id="new-site-text-string" value={this.newSiteText.english} name="english" onInput={this.handleInputChange} />
                </span>
              </div>
              <div id="new-site-text-string-comment-holder" class="form-input-item form-thing">
                <span class="form-thing">
                  <label htmlFor="site-text-string-comment">{capitalize(t('comment'))}</label>
                </span>
                <span class="form-thing">
                  <input type="text" id="site-text-string-comment" name="comment" value={this.newSiteText.comment} onInput={this.handleInputChange} />
                </span>
              </div>
              <div class="form-thing">
                <span class="form-thing">
                  <input id="create-button" type="submit" value={capitalize(t('create'))} onClick={this.handleInsert} />
                </span>
              </div>
            </form>
          )}
      </Host>
    );
  }
}
