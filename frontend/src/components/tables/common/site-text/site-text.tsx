import { Component, State, Host, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { SiteTextLanguage, SiteTextString } from '../../../../common/types';
import { globals } from '../../../../core/global.store';
@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: true,
})

export class SiteText {
  @State() columnData: ColumnDescription[];

  constructor() {
    this.columnData = []
  }

  handleSiteTextStringUpdate = async (): Promise<boolean> => {
    return true;
  }

  handleDelete = async (): Promise<boolean>  => {
    return true;
  }

  handleSiteTextTranslationUpdate = async (): Promise<boolean> => {
    return true;
  }

  makeColumns = (): ColumnDescription[] => {
    const columnData: ColumnDescription[] = [
      {
        field: 'english',
        displayName: 'English',
        width: 50,
        editable: true,
        deleteFn: this.handleDelete,
        updateFn: this.handleSiteTextStringUpdate,
      },
      {
        field: 'comment',
        displayName: 'Comment',
        width: 200,
        editable: true,
        updateFn: this.handleSiteTextStringUpdate,
      },
    ];
    const languageColumns: ColumnDescription[] = globals.globalStore.state.siteTextLanguages.map((siteTextLanguage: SiteTextLanguage) => {
      return {
        field: siteTextLanguage.language,
        displayName: siteTextLanguage.language_name,
        width: 50,
        editable: true,
        updateFn: this.handleSiteTextTranslationUpdate
      }
    });
    return columnData.concat(languageColumns);
  }

  makeRows = () => {
  
  }

  componentDidLoad() {
    this.columnData = this.makeColumns();
    // this.makeRows();
  }

  render() {
    return (
      <div class="site-text">
        <div class="language-select-wrapper">
          <h4>Select Site Language</h4>
          <language-select />
        </div>
        <div class="translations">

        </div>
      </div>
    );
  }

}
