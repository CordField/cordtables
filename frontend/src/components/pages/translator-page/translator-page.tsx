import { Component, Host, State, h } from '@stencil/core';
import { ActionType, ErrorType, SiteTextTranslation, SiteTextString } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { ColumnDescription } from '../../../common/table-abstractions/types';

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

type LanguageSearchParam = {
  eth: string,
  country: string
}

type LanguageIndexSearchRequest = {
  lang: string,
  country: string
}

type LanguageIndexSearchResponse = {
  error: ErrorType,
  id: string,
}

type TranslationData = {
  site_text: string,
  english: string,
  comment: string,
  id: string,
  translation: string
}

type TranslationRequest = {
  token: string,
  language: string
}

type TranslationResponse = {
  error: ErrorType,
  data: Array<TranslationData>
}

@Component({
  tag: 'translator-page',
  styleUrl: 'translator-page.css',
  shadow: true,
})
export class TranslatorPage {

  @State() search: LanguageSearchParam ={ eth: '', country: ''}
  @State() loading: Boolean = false;
  @State() language: string = '';
  @State() translations: Array<TranslationData> = [];
  editableKeys = ['translation'];

  handleInputChange = async event => {
    this.search = { ...this.search, [event.target.name]: event.target.value };

    if(this.search.country.length === 2 && this.search.eth.length === 3) {
      this.searchLanguageIndex();  
    }
  };

  searchLanguageIndex = async () => {
    this.loading = true;
    try {
      const response = await fetchAs<LanguageIndexSearchRequest, LanguageIndexSearchResponse>('sil/language-index/search-eth', {
        lang: this.search.eth,
        country: this.search.country
      });
      if(response.error === ErrorType.NoError) {
        this.language = response.id;
        this.loadTranslations()
      }
    } catch(err) {
      this.loading = false;
      console.log(err)
    }
  }

  loadTranslations = async () => {
    try {
      const response = await fetchAs<TranslationRequest, TranslationResponse>('page/translator/read', {
        token: globals.globalStore.state.token,
        language: this.language,
      });
      if(response.error === ErrorType.NoError) {
        this.translations = response.data;
      }
      this.loading = false;
    } catch(err) {
      this.loading = false;
      console.log(err)
    }
  }

  handleUpdate = async (id: string, column: string, newValue: string): Promise<boolean> => {
    const updateResponse = await fetchAs<SiteTextTranslationUpdateRequest, SiteTextTranslationUpdateResponse>('common/site-text-translations/update-read', {
      token: globals.globalStore.state.token,
      site_text_translation: {
        language: this.language,
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
      return true;
    } else {
      return false;
    }
  };

  columnData: ColumnDescription[] = [
    {
      field: 'english',
      displayName: 'English',
      width: 250,
      editable: false,
    },
    {
      field: 'translation',
      displayName: 'Translation',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'comment',
      displayName: 'Comment',
      width: 250,
      editable: false,
    },
  ]

  render() {
    return (
      <Host>
        <slot></slot>
        <div class="main-wrap">
          <div class="form-wrap">
            <p>
              Enter ethnologue code and country code
            </p>
            <p>
              You will be translating for :
            </p>

            <div class="form">
              <div>
                <label htmlFor="eth">eth code</label>
                <input type="text" id="eth" name="eth" onInput={this.handleInputChange} />
              </div>
              <div>
                <label htmlFor="country">country</label>
                <input type="text" id="country" name="country" onInput={this.handleInputChange}/>
              </div>
            </div>
          </div>
          <div id="table-wrap">
            {this.translations && this.translations.length >0  && <cf-table rowData={this.translations} columnData={this.columnData}></cf-table>}
        </div>

        </div>
      </Host>
    );
  }
}
