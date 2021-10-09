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
        {this.match.params.table === 'admin-global-roles' && <global-roles></global-roles>}
        {this.match.params.table === 'admin-global-role-column-grants' && <global-role-column-grants></global-role-column-grants>}
        {this.match.params.table === 'admin-global-role-table-permissions' && <global-role-table-permissions></global-role-table-permissions>}
        {this.match.params.table === 'admin-global-role-memberships' && <global-role-memberships></global-role-memberships>}
        {this.match.params.table === 'admin-groups' && (
          <div>
            <groups-table></groups-table>
            <groups-row-access></groups-row-access>
            <group-memberships></group-memberships>
          </div>
        )}
        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}
      </Host>
    );
  }
}
