import { fetchAs } from '../common/utility';
import { GenericResponse, SiteTextLanguage } from '../common/types';

type SiteTextLanguageRequest = {};

type SiteTextLanguageResponse = GenericResponse & { data: SiteTextLanguage }

export class SiteTextService {
  public async loadSiteTextLanguages(): Promise<void> {
    const result = await fetchAs<SiteTextLanguageRequest, SiteTextLanguageResponse>
      ('services/site-text-languages/read', {});
  }

  public loadSiteTextTranslations(): void {}
}

export const siteTextService = new SiteTextService();
