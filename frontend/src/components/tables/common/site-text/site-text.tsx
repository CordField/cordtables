import { Component, State, Host, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse, SiteTextLanguage, SiteTextString, SiteTextTranslation } from '../../../../common/types';
import { globals } from '../../../../core/global.store';
import { t } from '../../../../core/site-text.service';
import { capitalize, capitalizePhrase } from '../../../../common/utility';
import { fetchAs } from '../../../../common/utility';
import * as ion from '@ionic/core';

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

type SiteTextTranslationUpdateInput = {
  language: string;
  site_text: string;
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
  id: string;
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

type LanguageIndex = {
  id: string;
  lang: string;
  country: string;
  name_type: string;
  name: string;
};

type LanguageIndexListRequest = {
  token: string;
  search: string;
  page?: number;
  resultsPerPage?: number;
};

type LanguageIndexListResponse = {
  error: ErrorType;
  size: number;
  languageIndexes: Array<LanguageIndex> | null;
};

type LanguageIndexListResponseWithPage = LanguageIndexListResponse & { page: number };

type SiteTextLanguageCreateRequest = {
  token: string;
  language: string;
};

const resultsPerPage = 10;

@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: false,
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
    const createResponse = await fetchAs<SiteTextStringCreateRequest, SiteTextStringCreateResponse>('common/site-text-strings/create-read', {
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

  handleSiteTextStringUpdate = async (id: string, column: string, newValue: string): Promise<boolean> => {
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

  handleDelete = async (id: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextStringDeleteRequest, SiteTextStringDeleteResponse>('common/site-text-strings/delete', {
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

  handleSiteTextTranslationUpdate = async (id: string, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextTranslationUpdateRequest, SiteTextTranslationUpdateResponse>('common/site-text-translations/update-read', {
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
        width: 250,
        editable: false,
        deleteFn: this.handleDelete,
      },
      {
        field: 'english',
        displayName: capitalize(t('english')),
        width: 250,
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
        width: 100,
        editable: true,
        updateFn: this.handleSiteTextTranslationUpdate,
      };
    });
    return columnData.concat(languageColumns);
  };

  makeRows = () => {
    console.debug('globalStore siteTextStrings', globals.globalStore.state.siteTextStrings);
    console.debug('globals.globalStore.state.siteTextTranslations', globals.globalStore.state.siteTextTranslations);
    return globals.globalStore.state.siteTextStrings.map((siteTextString: SiteTextString) => {
      const row = {
        id: siteTextString.id,
        english: siteTextString.english,
        comment: siteTextString.comment,
      };

      globals.globalStore.state.siteTextLanguages.forEach((siteTextLanguage: SiteTextLanguage) => {
        const value = globals.globalStore.state.siteTextTranslations[siteTextLanguage.language]
          ? globals.globalStore.state.siteTextTranslations[siteTextLanguage.language][siteTextString.english]
          : undefined;
        row[siteTextLanguage.language] = { value, displayValue: value === undefined ? '' : value };
      });

      return row;
    });
  };

  loadLanguages = (page: number, search: string = ''): Promise<LanguageIndexListResponseWithPage> => {
    this.loading = true;

    return new Promise((resolve, reject) => {
      fetchAs<LanguageIndexListRequest, LanguageIndexListResponse>('sil/language-index/list', {
        token: globals.globalStore.state.token,
        search,
        page,
        resultsPerPage,
      })
        .then(res => {
          this.loading = false;
          if (res.error === ErrorType.NoError) {
            this.totalRows = res.size;
            this.page = page;
            this.languages = [...this.languages, ...res.languageIndexes];
          }
          resolve({ ...res, page });
        })
        .catch(err => {
          console.debug('error ====> ', err);
          reject();
        });
    });
  };

  componentDidLoad() {
    this.columnData = this.makeColumns();
    this.rowData = this.makeRows();
    console.debug('rowData', this.rowData);
    this.loadLanguages(1);
  }

  @State() search: string = '';
  @State() page = 1;
  @State() languages: Array<LanguageIndex> = [];
  @State() totalRows: number = 0;
  @State() loading: boolean = false;
  private infiniteScroll: HTMLIonInfiniteScrollElement;

  handleSearch = (event: CustomEvent) => {
    this.search = event.detail.value;
    this.languages = [];
    if (this.infiniteScroll !== undefined && this.infiniteScroll !== null) this.infiniteScroll.disabled = false;
    this.loadLanguages(1, event.detail.value);
  };

  loadMore = async () => {
    const search = this.search;
    if (this.loading === false && this.page * resultsPerPage < this.totalRows) {
      const res = await this.loadLanguages(this.page + 1, search);
      if (this.infiniteScroll !== undefined && this.infiniteScroll !== null) this.infiniteScroll.complete();
      if (res.error === ErrorType.NoError && (res.page + 1) * resultsPerPage > res.size) {
        if (this.infiniteScroll !== undefined && this.infiniteScroll !== null) this.infiniteScroll.disabled = true;
      }
    }
  };

  addSiteTextLanguage = (language: LanguageIndex) => {
    return async (): Promise<boolean> => {
      const response = await fetchAs<SiteTextLanguageCreateRequest, GenericResponse>('common/site-text-languages/create', {
        token: globals.globalStore.state.token,
        language: language.id,
      });

      if (response.error == ErrorType.NoError) {
        const newSiteTextLanguages = [...globals.globalStore.state.siteTextLanguages, { language: language.id, language_name: language.name }];
        globals.globalStore.set('siteTextLanguages', newSiteTextLanguages);
        this.columnData = this.makeColumns();
        this.rowData = this.makeRows();
        return true;
      } else {
        return false;
      }
    };
  };

  removeSiteTextLanguage = id => {
    return async (): Promise<boolean> => {
      const response = await fetchAs<SiteTextLanguageCreateRequest, GenericResponse>('common/site-text-languages/delete', {
        token: globals.globalStore.state.token,
        language: id,
      });

      if (response.error == ErrorType.NoError) {
        const newSiteTextLanguages = globals.globalStore.state.siteTextLanguages.filter(s => s.language !== id);
        globals.globalStore.set('siteTextLanguages', newSiteTextLanguages);
        this.columnData = this.makeColumns();
        this.rowData = this.makeRows();
        return true;
      } else {
        return false;
      }
    };
  };

  render() {
    return (
      <Host>
        <div class="site-text">
          <div class="language-select-wrapper">
            <h4>{capitalizePhrase(`${t('select')} ${t('site')} ${t('language')}`)}</h4>
            <language-select />
          </div>
          <div class="language-add-wrapper">
            <ion-text color="dark">
              <h4>Add Site Text Languages</h4>
            </ion-text>
            <div class="searchbar-wrapper">
              <ion-searchbar showClearButton="never" value={this.search} onIonChange={this.handleSearch}></ion-searchbar>
            </div>
            <ion-list-header lines="inset">
              <ion-grid>
                <ion-row>
                  <ion-col size="2">LANG</ion-col>
                  <ion-col size="2">COUNTRY</ion-col>
                  <ion-col size="3">NAME TYPE</ion-col>
                  <ion-col size="3">NAME</ion-col>
                  <ion-col size="2">ACTION</ion-col>
                </ion-row>
              </ion-grid>
            </ion-list-header>

            <div class="language-content-wrapper">
              <ion-content>
                <ion-list>
                  {this.languages.length > 0 &&
                    this.languages.map((language: LanguageIndex) => (
                      <ion-item>
                        <ion-grid>
                          <ion-row>
                            <ion-col size="2">{language.lang}</ion-col>
                            <ion-col size="2">{language.country}</ion-col>
                            <ion-col size="3">{language.name_type}</ion-col>
                            <ion-col size="3">{language.name}</ion-col>
                            <ion-col size="2">
                              {globals.globalStore.state.siteTextLanguages.find((s: SiteTextLanguage) => s.language === language.id) === undefined ? (
                                <ion-button expand="block" onClick={this.addSiteTextLanguage(language)}>
                                  ADD
                                </ion-button>
                              ) : (
                                <ion-button expand="block" onClick={this.removeSiteTextLanguage(language.id)}>
                                  REMOVE
                                </ion-button>
                              )}
                            </ion-col>
                          </ion-row>
                        </ion-grid>
                      </ion-item>
                    ))}
                </ion-list>
                <ion-infinite-scroll ref={el => (this.infiniteScroll = el)} onIonInfinite={this.loadMore}>
                  <ion-infinite-scroll-content loadingSpinner="bubbles" loadingText="Loading more data..."></ion-infinite-scroll-content>
                </ion-infinite-scroll>
              </ion-content>
            </div>
          </div>
          <div class="translations">
            <h4>{capitalizePhrase(`${t('site')} ${t('text')} ${t('translations')}`)}</h4>
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
                  <input id="create-button" type="submit" value={capitalize(t('create'))} onClick={this.handleInsert} />
                </span>
              </div>
            </form>
          )}
        </div>
      </Host>
    );
  }
}
