import { Component, Prop, h } from '@stencil/core';
import { MatchResults } from '@stencil/router';
import { globals } from '../../../core/global.store';

@Component({
  tag: 'app-profile',
  styleUrl: 'app-profile.css',
  shadow: true,
})
export class AppProfile {
  @Prop() match: MatchResults;

  normalize(name: string): string {
    if (name) {
      return name.substr(0, 1).toUpperCase() + name.substr(1).toLowerCase();
    }
    return '';
  }

  render() {
    return (
      <div class="app-profile">
        <p>isLoggedIn: {globals.globalStore.state.isLoggedIn.toString()}</p>
        <p>Email: {globals.globalStore.state.email}</p>
        <p>Token: {globals.globalStore.state.token}</p>
      </div>
    );
  }
}
