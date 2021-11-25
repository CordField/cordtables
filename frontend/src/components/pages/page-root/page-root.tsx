import { Component, Host, h, Prop } from '@stencil/core';
import { MatchResults } from '@stencil/router';

@Component({
  tag: 'page-root',
  styleUrl: 'page-root.css',
  shadow: true,
})
export class PageRoot {
  @Prop() match: MatchResults;

  render() {
    console.log('Page: ', this.match.params.page);
    return (
      <Host>
        <slot></slot>
        {this.match.params.page === 'groups' && <groups-page></groups-page>}
        {this.match.params.page === 'organizations' && <organizations-page></organizations-page>}
        {this.match.params.page === 'roles' && <roles-page></roles-page>}
        {this.match.params.page === 'tickets' && <tickets-page></tickets-page>}
      </Host>
    );
  }
}
