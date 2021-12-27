
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
      <select class="language-select" onInput={this.changeSiteLanguage}>
        <option value="default" selected={globals.globalStore.state.language.toString() === "default"}>--</option>
        {globals.globalStore.state.siteTextLanguages.map(option => (
          <option value={option.language} selected={option.language.toString() === globals.globalStore.state.language.toString()}>{option.language_name}</option>
        ))}
    </select>
    );
  }
}
