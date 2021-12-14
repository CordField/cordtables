
import { Component, Host, h } from '@stencil/core';
import { globals } from '../../core/global.store';

@Component({
  tag: 'language-select',
  styleUrl: 'language-select.css',
  shadow: true,
})
export class LanguageSelect {

  changeSiteLanguage = (event) => {
    globals.globalStore.set('language', event.target.value);
  }

  render() {
    return (
      <select onInput={this.changeSiteLanguage}>
        <option value="default" selected={globals.globalStore.state.language === "default"}>--</option>
        {globals.globalStore.state.siteTextLanguages.map(option => (
          <option value={option.language} selected={option.language === globals.globalStore.state.language}>{option.language_name}</option>
        ))}
    </select>
    );
  }
}
