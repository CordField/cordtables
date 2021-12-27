import { fetchAs } from '../common/utility';
import { ErrorType, GenericResponse, SiteTextLanguage, SiteTextString, SiteTextTranslation, SiteTextTranslationList, AppState } from '../common/types';

import { globals } from './global.store';

type SiteTextLanguageResponse = GenericResponse & {
  data: [SiteTextLanguage];
};

type SiteTextStringResponse = GenericResponse & {
  data: [SiteTextString];
};

type SiteTextTranslationListResponse = GenericResponse & {
  data: [SiteTextTranslationList];
};

export class SiteTextService {
  public loadSiteTextLanguages(): Promise<Array<SiteTextLanguage>> {
    return new Promise((resolve, reject) => {
      fetchAs<{}, SiteTextLanguageResponse>('common/site-texts/languages/list', {})
        .then(res => {
          if (res.error === ErrorType.NoError) resolve(res.data);
          reject();
        })
        .catch(error => {
          console.debug('site text langauges error ===>', error);
          reject();
        });
    });
  }

  public loadSiteTextStrings(): Promise<Array<SiteTextString>> {
    return new Promise((resolve, reject) => {
      fetchAs<{}, SiteTextStringResponse>('common/site-texts/strings/list', {})
        .then(res => {
          if (res.error === ErrorType.NoError) resolve(res.data);
          reject();
        })
        .catch(error => {
          console.debug('site text strings error ===>', error);
          reject();
        });
    });
  }

  public loadSiteTextTranslations(): Promise<Array<SiteTextTranslationList>> {
    return new Promise((resolve, reject) => {
      fetchAs<{}, SiteTextTranslationListResponse>('common/site-texts/translations/list', {})
        .then(res => {
          if (res.error === ErrorType.NoError) resolve(res.data);
          reject();
        })
        .catch(error => {
          console.debug('site text strings error ===>', error);
          reject();
        });
    });
  }

  private findString(strings: SiteTextString[], site_text: number): string {
    const siteTextString = strings.find((item: SiteTextString) => item.id === site_text);
    return siteTextString.english;
  }

  public makeReadableTranslations(strings: SiteTextString[], data: SiteTextTranslationList[]) {
    const translationObject = {};
    data.forEach((item: SiteTextTranslationList) => {
      translationObject[item.language] = {};
      item.translations.forEach((tr: SiteTextTranslation) => {
        translationObject[item.language][this.findString(strings, tr.site_text)] = tr.translation;
      });
    });
    return translationObject;
  }

  public async load() {
    const languagePromise = this.loadSiteTextLanguages();
    const stringPromise = this.loadSiteTextStrings();
    const translationsPromise = this.loadSiteTextTranslations();
    const data = await Promise.all([languagePromise, stringPromise, translationsPromise]);
    globals.globalStore.set('siteTextLanguages', data[0]);
    globals.globalStore.set('siteTextStrings', data[1]);
    const translationObject = this.makeReadableTranslations(data[1], data[2]);
    globals.globalStore.set('siteTextTranslations', translationObject);
    globals.globalStore.set('appState', AppState.TranslationLoaded);
  }
}

export const siteTextService = new SiteTextService();

export const t = (key: string) => {
  let language: any = globals.globalStore.state.language;
  if (language === 'default') return key;
  language = parseInt(language);
  if(!globals.globalStore.state.siteTextTranslations[language]) return key;
  const translation = globals.globalStore.state.siteTextTranslations[language][key];
  if (!translation) {
    console.debug(`${key} doesn't exist in translations`);
  }
  return translation ? translation : key;
};
