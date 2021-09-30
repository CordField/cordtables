import { Prop } from '@ionic/core/dist/types/stencil-public-runtime';
import { Component, Host, h } from '@stencil/core';
import { MatchResults } from '@stencil/router';

@Component({
  tag: 'table-root',
  styleUrl: 'table-root.css',
  shadow: true,
})
export class TableRoot {
  @Prop() match: MatchResults;

  render() {
    return (
      <Host>
        <slot></slot>
        {this.match.params.table === 'global-roles' && <global-roles></global-roles>}
        {this.match.params.table === 'global-role-column-grants' && <global-role-column-grants></global-role-column-grants>}
        {this.match.params.table === 'global-role-table-permissions' && <global-role-table-permissions></global-role-table-permissions>}
        {this.match.params.table === 'global-role-memberships' && <global-role-memberships></global-role-memberships>}
        {this.match.params.table === 'groups' && <cf-groups></cf-groups>}
      </Host>
    );
  }
}
