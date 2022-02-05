import { Component, Host, h, Prop } from '@stencil/core';
import { MatchResults } from '@stencil/router';

@Component({
  tag: 'page-root',
  styleUrl: 'page-root.css',
  scoped: true,
})
export class PageRoot {
  @Prop() match: MatchResults;

  render() {
    // console.log(this.match.params)
    return (
      <Host>
        <slot></slot>
        {this.match.params.page === 'groups' && <groups-page></groups-page>}
        {this.match.params.page === 'organizations' && <organizations-page></organizations-page>}
        {this.match.params.page === 'roles' && <roles-page></roles-page>}
        {this.match.params.page === 'slack' && <slack-page></slack-page>}
        {this.match.params.page === 'tickets' && <new-tickets-page></new-tickets-page>}
        {this.match.params.page === 'not-found' && <page-not-found></page-not-found>}

        {this.match.params.page === 'prayer-requests' && <page-prayer-requests></page-prayer-requests>}
        {this.match.params.page === 'request-prayer' && <request-prayer-page></request-prayer-page>}
        {this.match.params.page === 'prayer-request-edit' && <prayer-request-edit-page requestId={this.match.params.requestId}></prayer-request-edit-page>}

        {this.match.params.page === 'partner-crm' && <partner-crm></partner-crm>}

        {this.match.params.page === 'translator' && <translator-page></translator-page>}
      </Host>
    );
  }
}
