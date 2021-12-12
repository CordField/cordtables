import { Component, State, Host, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, SiteTextLanguage, SiteTextString } from '../../../../common/types';
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
@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: true,
})
export class SiteText {
  @State() columnData: ColumnDescription[];
  @State() rowData: Array<any>;

  handleSiteTextStringUpdate = async (id: number, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextStringUpdateRequest, SiteTextStringUpdateResponse>('common-site-text-strings/update-read', {
      token: globals.globalStore.state.token,
      site_text_string: {
        column,
        id: id,
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

      if(column === 'english') {
        const newTranslations = { ... globals.globalStore.state.siteTextTranslations };
        Object.keys(newTranslations).map(language => {
          newTranslations[language][newValue] = newTranslations[language][oldValue];
          delete newTranslations[language][oldValue];
        })
        globals.globalStore.set('siteTextTranslations', newTranslations);        
      }

      this.rowData = this.makeRows();
      return true;
    } else {
      return false;
    }
  };

  handleDelete = async (): Promise<boolean> => {
    return true;
  };

  handleSiteTextTranslationUpdate = async (): Promise<boolean> => {
    return true;
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
        displayName: t(siteTextLanguage.language_name),
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
      </div>
    );
  }
}
