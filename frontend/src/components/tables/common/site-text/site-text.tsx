import { Component, State, Host, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse, SiteTextLanguage, SiteTextString, SiteTextTranslation } from '../../../../common/types';
import { globals } from '../../../../core/global.store';
import { t } from '../../../../core/site-text.service';
import { capitalize } from '../../../../common/utility';
import { fetchAs } from '../../../../common/utility';

type SiteTextStringUpdateInput = {
  id: number;
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

type SiteTextTranslationUpdateInput = {
  language: number;
  site_text: number;
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

type SiteTextStringDeleteRequest = {
  token: string;
  id: number;
};

type SiteTextStringDeleteResponse = GenericResponse;

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

@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: true,
})
export class SiteText {
  @State() columnData: ColumnDescription[];
  @State() rowData: Array<any>;
  @State() newSiteText: SiteTextStringCreateInput = {
    english: '',
    comment: '',
  };

  handleInsert = async event => {
    event.preventDefault();
    if (this.newSiteText.english === '') return;
    const createResponse = await fetchAs<SiteTextStringCreateRequest, SiteTextStringCreateResponse>('common-site-text-strings/create-read', {
      token: globals.globalStore.state.token,
      site_text_string: {
        english: this.newSiteText.english,
        comment: this.newSiteText.comment,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.set('siteTextStrings', [createResponse.site_text_string, ...globals.globalStore.state.siteTextStrings]);
      this.rowData = this.makeRows();
    } else {
      return;
    }
    this.newSiteText = { english: '', comment: '' };
  };

  handleInputChange = event => {
    this.newSiteText = { ...this.newSiteText, [event.target.name]: event.target.value };
  };

  handleSiteTextStringUpdate = async (id: number, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextStringUpdateRequest, SiteTextStringUpdateResponse>('common-site-text-strings/update-read', {
      token: globals.globalStore.state.token,
      site_text_string: {
        column,
        id,
        newValue,
      },
    });

    if (updateResponse.error == ErrorType.NoError) {
      const oldValue = globals.globalStore.state.siteTextStrings.find((siteTextString: SiteTextString) => siteTextString.id === id).english;

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

      this.rowData = this.makeRows();
      return true;
    } else {
      return false;
    }
  };

  handleDelete = async (id: number): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextStringDeleteRequest, SiteTextStringDeleteResponse>('common-site-text-strings/delete', {
      token: globals.globalStore.state.token,
      id,
    });

    if (updateResponse.error == ErrorType.NoError) {
      const key = globals.globalStore.state.siteTextStrings.find((siteTextString: SiteTextString) => siteTextString.id === id).english;
      const newTranslations = { ...globals.globalStore.state.siteTextTranslations };
      Object.keys(newTranslations).map(language => {
        delete newTranslations[language][key];
        return null;
      });
      globals.globalStore.set('siteTextTranslations', newTranslations);
      globals.globalStore.set(
        'siteTextStrings',
        globals.globalStore.state.siteTextStrings.filter((siteTextString: SiteTextString) => siteTextString.id !== id),
      );
      this.rowData = this.makeRows();
      return true;
    } else {
      return false;
    }
  };

  handleSiteTextTranslationUpdate = async (id: number, column: number, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextTranslationUpdateRequest, SiteTextTranslationUpdateResponse>('common-site-text-translations/update-read', {
      token: globals.globalStore.state.token,
      site_text_translation: {
        language: column,
        site_text: id,
        newValue,
      },
    });

    if (updateResponse.error == ErrorType.NoError) {
      const key = globals.globalStore.state.siteTextStrings.find((siteTextString: SiteTextString) => siteTextString.id === id).english;
      const newTranslations = { ...globals.globalStore.state.siteTextTranslations };
      Object.keys(newTranslations).map(language => {
        if (language === column.toString()) {
          newTranslations[language][key] = newValue;
        }
        return null;
      });
      globals.globalStore.set('siteTextTranslations', newTranslations);
      this.rowData = this.makeRows();
      return true;
    } else {
      return false;
    }
  };

  makeColumns = (): ColumnDescription[] => {
    const columnData: ColumnDescription[] = [
      {
        field: 'id',
        displayName: 'ID',
        width: 50,
        editable: false,
        deleteFn: this.handleDelete,
      },
      {
        field: 'english',
        displayName: capitalize(t('english')),
        width: 50,
        editable: true,
        deleteFn: this.handleDelete,
        updateFn: this.handleSiteTextStringUpdate,
      },
      {
        field: 'comment',
        displayName: capitalize(t('comment')),
        width: 200,
        editable: true,
        updateFn: this.handleSiteTextStringUpdate,
      },
    ];
    const languageColumns: ColumnDescription[] = globals.globalStore.state.siteTextLanguages.map((siteTextLanguage: SiteTextLanguage) => {
      return {
        field: siteTextLanguage.language,
        displayName: capitalize(t(siteTextLanguage.language_name)),
        width: 50,
        editable: true,
        updateFn: this.handleSiteTextTranslationUpdate,
      };
    });
    return columnData.concat(languageColumns);
  };

  makeRows = () => {
    return globals.globalStore.state.siteTextStrings.map((siteTextString: SiteTextString) => {
      const row = {
        id: siteTextString.id,
        english: siteTextString.english,
        comment: siteTextString.comment,
      };

      globals.globalStore.state.siteTextLanguages.forEach((siteTextLanguage: SiteTextLanguage) => {
        row[siteTextLanguage.language] = globals.globalStore.state.siteTextTranslations[siteTextLanguage.language][siteTextString.english];
      });

      return row;
    });
  };

  componentDidLoad() {
    this.columnData = this.makeColumns();
    this.rowData = this.makeRows();
  }

  render() {
    return (
      <div class="site-text">
        <div class="language-select-wrapper">
          <h4>Select Site Language</h4>
          <language-select />
        </div>
        <div class="translations">
          <h4>Site Text Translations</h4>
          {this.columnData && this.columnData.length > 0 && <cf-table rowData={this.rowData} columnData={this.columnData} />}
        </div>
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
                <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
              </span>
            </div>
          </form>
        )}
      </div>
    );
  }
}
