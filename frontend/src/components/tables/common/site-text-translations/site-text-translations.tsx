import { Component, Host, h, State, Listen } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse, SiteTextLanguage, SiteTextString, SiteTextTranslation } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';
import { capitalize, capitalizePhrase } from '../../../../common/utility';
import { t } from '../../../../core/site-text.service';
import { siteTextService } from "../../../../core/site-text.service";

type SiteTextTranslationUpdateInput = {
  id: string;
  column: string;
  newValue: string;
};

type SiteTextTranslationUpdateRequest = {
  token: string;
  site_text_translation: SiteTextTranslationUpdateInput;
};

type SiteTextTranslationUpdateResponse = {
  error: ErrorType;
  site_text_translation: SiteTextTranslation;
};

type SiteTextTranslationDeleteRequest = {
  token: string;
  id: string;
};

type SiteTextTranslationDeleteResponse = GenericResponse;

type SiteTextTranslationTableListRequest = {
  token: string;
}

type SiteTextTranslationTableListResponse = {
  data: SiteTextTranslation[];
  error: ErrorType;
} 

@Component({
  tag: 'site-text-translations',
  styleUrl: 'site-text-translations.css',
  shadow: true,
})
export class SiteTextStrings {
  @State() siteTextTranslations: SiteTextTranslation[];
  
  @Listen('searchResults')
  async showSearchResults(event: CustomEvent<any>) {
    this.siteTextTranslations = event.detail as SiteTextTranslation[];
  }

  handleUpdate = async (id: string, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextTranslationUpdateRequest, SiteTextTranslationUpdateResponse>('common/site-text-translations/record/update-read', {
      token: globals.globalStore.state.token,
      site_text_translation: {
        column,
        id,
        newValue,
      },
    });

    if (updateResponse.error == ErrorType.NoError) {
      this.siteTextTranslations = this.siteTextTranslations.map((st: SiteTextTranslation) => {
        if(st.id === id) {
          return {
            ...st,
            [column]: newValue,
          }
        }
        return st;
      });
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async (id: string): Promise<boolean> => {
    const response = await fetchAs<SiteTextTranslationDeleteRequest, SiteTextTranslationDeleteResponse>('common/site-text-translations/delete', {
      token: globals.globalStore.state.token,
      id,
    });

    if (response.error == ErrorType.NoError) {
      this.siteTextTranslations = this.siteTextTranslations.filter((siteTextTranslation: SiteTextTranslation) => siteTextTranslation.id !== id);
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: response.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  getList = () => {
    fetchAs<SiteTextTranslationTableListRequest, SiteTextTranslationTableListResponse>('common/site-text-translations/list', {
      token: globals.globalStore.state.token,
    })
    .then(res => {
      if(res.error == ErrorType.NoError) {
        this.siteTextTranslations = res.data;
      }
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
      field: 'language',
      displayName: capitalize(t('language')),
      width: 200,
      editable: false,
      updateFn: this.handleUpdate,
    },
    {
      field: 'site_text',
      displayName: capitalize(t('site_text')),
      width: 200,
      editable: false,
      updateFn: this.handleUpdate,
    },
    {
      field: 'translation',
      displayName: capitalize(t('translation')),
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
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <search-form
          columnNames={[
            'id',
            'language',
            'site_text',
            'translation',
            'created_at',
            'created_by',
            'modified_at',
            'modified_by',
            'owning_person',
            'owning_group',
          ]}
        ></search-form>
        {/* table abstraction */}
        {this.siteTextTranslations && <cf-table rowData={this.siteTextTranslations} columnData={this.columnData}></cf-table>}
        
      </Host>
    );
  }
}
