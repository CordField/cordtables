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
        {(this.match.params.table === 'admin-roles' ||
          this.match.params.table === 'admin-role-column-grants' ||
          this.match.params.table === 'admin-role-memberships' ||
          this.match.params.table === 'admin-role-table-permissions') && (
          <div>
            <roles-table></roles-table>
            <role-memberships></role-memberships>
            <role-table-permissions></role-table-permissions>
            <role-column-grants></role-column-grants>
          </div>
        )}

        {(this.match.params.table === 'admin-groups' || this.match.params.table === 'admin-group-memberships' || this.match.params.table === 'admin-group-row-access') && (
          <div>
            <groups-table></groups-table>
            <group-memberships></group-memberships>
            <groups-row-access></groups-row-access>
          </div>
        )}
        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}
        {this.match.params.table === 'common-organizations' && <common-organizations></common-organizations>}
        {this.match.params.table === 'common-scripture-references' && <scripture-references></scripture-references>}
      </Host>
    );
  }
}
