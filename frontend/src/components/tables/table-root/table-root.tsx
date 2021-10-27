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
        {this.match.params.table === 'admin-users' && <admin-users></admin-users>}
        {this.match.params.table === 'admin-roles' && <roles-table></roles-table>}
        {this.match.params.table === 'admin-role-column-grants' && <role-column-grants></role-column-grants>}
        {this.match.params.table === 'admin-role-membership' && <role-memberships></role-memberships>}
        {this.match.params.table === 'admin-role-table-permissions' && <role-table-permissions></role-table-permissions>}

        {this.match.params.table === 'admin-groups' && <groups-table></groups-table>}
        {this.match.params.table === 'admin-group-membership' && <group-memberships></group-memberships>}
        {this.match.params.table === 'admin-group-row-access' && <groups-row-access></groups-row-access>}

        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}
        {this.match.params.table === 'common-organizations' && <common-organizations></common-organizations>}
        {this.match.params.table === 'common-scripture-references' && <scripture-references></scripture-references>}

        {this.match.params.table === 'sc-languages' && <sc-languages></sc-languages>}
      </Host>
    );
  }
}
