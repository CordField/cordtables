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

        {/* {this.match.params.table === 'admin-roles' && <roles-table></roles-table>}
        {this.match.params.table === 'admin-role-column-grants' && <role-column-grants></role-column-grants>}
        {this.match.params.table === 'admin-role-membership' && <role-memberships></role-memberships>}
        {this.match.params.table === 'admin-role-table-permissions' && <role-table-permissions></role-table-permissions>}
        {this.match.params.table === 'admin-groups' && <groups-table></groups-table>}
        {this.match.params.table === 'admin-group-membership' && <group-memberships></group-memberships>}
        {this.match.params.table === 'admin-group-row-access' && <groups-row-access></groups-row-access>} */}

        {this.match.params.table === 'admin-group-memberships' && <admin-group-memberships></admin-group-memberships>}
        {this.match.params.table === 'admin-group-row-access' && <admin-group-row-access></admin-group-row-access>}
        {this.match.params.table === 'admin-groups' && <admin-groups></admin-groups>}
        {this.match.params.table === 'admin-role-column-grants' && <admin-role-column-grants></admin-role-column-grants>}

        {this.match.params.table === 'admin-role-memberships' && <admin-role-memberships></admin-role-memberships>}
        {this.match.params.table === 'admin-role-table-permissions' && <admin-role-table-permissions></admin-role-table-permissions>}
        {this.match.params.table === 'admin-roles' && <admin-roles></admin-roles>}
        {this.match.params.table === 'admin-people' && <admin-people></admin-people>}

        {this.match.params.table === 'sc-funding-accounts' && <sc-funding-accounts></sc-funding-accounts>}
        {this.match.params.table === 'sc-internship-engagements' && <sc-internship-engagements></sc-internship-engagements>}
        {this.match.params.table === 'sc-known-languages-by-person' && <sc-known-languages-by-person></sc-known-languages-by-person>}
        {this.match.params.table === 'sc-language-engagements' && <sc-language-engagements></sc-language-engagements>}
        {this.match.params.table === 'sc-organization-locations' && <sc-organization-locations></sc-organization-locations>}
        {this.match.params.table === 'sc-organizations' && <sc-organizations></sc-organizations>}
        {this.match.params.table === 'sc-partners' && <sc-partners></sc-partners>}
        {this.match.params.table === 'sc-partnerships' && <sc-partnerships></sc-partnerships>}
        {this.match.params.table === 'sc-person-unavailabilities' && <sc-person-unavailabilities></sc-person-unavailabilities>}
        {this.match.params.table === 'sc-pinned-projects' && <sc-pinned-projects></sc-pinned-projects>}
        {this.match.params.table === 'sc-posts' && <sc-posts></sc-posts>}

        {this.match.params.table === 'sc-product-scripture-references' && <sc-product-scripture-references></sc-product-scripture-references>}
        {this.match.params.table === 'sc-products' && <sc-products></sc-products>}
        {this.match.params.table === 'sc-project-locations' && <sc-project-locations></sc-project-locations>}
        {this.match.params.table === 'sc-projects' && <sc-projects></sc-projects>}

        {this.match.params.table === 'common-directories' && <directories-table></directories-table>}
        {this.match.params.table === 'common-files' && <files-table></files-table>}
        {this.match.params.table === 'common-file-versions' && <file-versions></file-versions>}
        {this.match.params.table === 'sc-field-regions' && <sc-field-regions-table></sc-field-regions-table>}
        {this.match.params.table === 'sc-field-zones' && <sc-field-zones></sc-field-zones>}

        {this.match.params.table === 'sc-global-partner-assessments' && <sc-global-partner-assessments></sc-global-partner-assessments>}
        {this.match.params.table === 'sc-global-partner-engagements' && <sc-global-partner-engagements></sc-global-partner-engagements>}
        {this.match.params.table === 'sc-global-partner-engagement-people' && <sc-global-partner-engagement-people></sc-global-partner-engagement-people>}
        {this.match.params.table === 'sc-global-partner-performance' && <sc-global-partner-performance></sc-global-partner-performance>}
        {this.match.params.table === 'common-coalitions' && <common-coalitions></common-coalitions>}
        {this.match.params.table === 'common-coalition-memberships' && <common-coalition-memberships></common-coalition-memberships>}

        {this.match.params.table === 'up-prayer-requests' && <up-prayer-requests></up-prayer-requests>}
        {this.match.params.table === 'up-prayer-notifications' && <up-prayer-notifications></up-prayer-notifications>}

        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}
        {this.match.params.table === 'common-organizations' && <common-organizations></common-organizations>}
        {this.match.params.table === 'common-tickets' && <tickets-table></tickets-table>}
        {this.match.params.table === 'common-ticket-graph' && <ticket-graph></ticket-graph>}
        {this.match.params.table === 'sc-budget-records' && <sc-budget-records></sc-budget-records>}
        {this.match.params.table === 'common-ticket-assignments' && <ticket-assignments></ticket-assignments>}
        {this.match.params.table === 'common-work-records' && <work-records></work-records>}
        {this.match.params.table === 'common-work-estimates' && <work-estimates></work-estimates>}
        {this.match.params.table === 'common-ticket-feedback' && <ticket-feedback></ticket-feedback>}
        {this.match.params.table === 'common-scripture-references' && <scripture-references></scripture-references>}
        {this.match.params.table === 'sc-locations' && <sc-locations></sc-locations>}
        {this.match.params.table === 'common-cell-channels' && <common-cell-channels></common-cell-channels>}
        {this.match.params.table === 'common-threads' && <common-threads></common-threads>}
        {this.match.params.table === 'common-posts' && <common-posts></common-posts>}
        {this.match.params.table === 'sc-languages' && <sc-languages></sc-languages>}
        


        {/* {this.match.params.table === 'admin-users' && <admin-users></admin-users>} */}
        {this.match.params.table === 'common-locations' && <common-locations></common-locations>}
        {this.match.params.table === 'common-notes' && <common-notes></common-notes>}
        {this.match.params.table === 'common-org-chart-positions' && <common-org-chart-positions></common-org-chart-positions>}
        {this.match.params.table === 'common-org-chart-position-graph' && <common-org-chart-position-graph></common-org-chart-position-graph>}
        {this.match.params.table === 'common-people-graph' && <common-people-graph></common-people-graph>}
        {this.match.params.table === 'common-people-to-org-relationships' && <common-people-to-org-relationships></common-people-to-org-relationships>}
        {this.match.params.table === 'common-stage-graph' && <common-stage-graph></common-stage-graph>}
        {this.match.params.table === 'common-stage-notifications' && <common-stage-notifications></common-stage-notifications>}
        {this.match.params.table === 'common-stage-role-column-grants' && <common-stage-role-column-grants></common-stage-role-column-grants>}
        {this.match.params.table === 'common-stages' && <common-stages></common-stages>}
        {this.match.params.table === 'common-workflows' && <common-workflows></common-workflows>}
        {this.match.params.table === 'sc-ethnologue' && <sc-ethnologue></sc-ethnologue>} 
        {this.match.params.table === 'sc-global-partner-transitions' && <sc-global-partner-transitions></sc-global-partner-transitions>}
        {this.match.params.table === 'sc-people' && <sc-people></sc-people>}

        {['common-site-text', 'common-site-text-languages', 'common-site-text-strings', 'common-site-text-translations'].includes(this.match.params.table) && <site-text></site-text>}

        {this.match.params.table === 'sil-country-codes' && <sil-country-codes></sil-country-codes>}
        {this.match.params.table === 'sil-language-codes' && <sil-language-codes></sil-language-codes>}
        {this.match.params.table === 'sil-language-index' && <sil-language-index></sil-language-index>}
        {this.match.params.table === 'sil-table-of-languages' && <sil-table-of-languages></sil-table-of-languages>}
        {this.match.params.table === 'sil-iso-639-3' && <sil-iso-639-3></sil-iso-639-3>}
        {this.match.params.table === 'sil-iso-639-3-names' && <sil-iso-639-3-names></sil-iso-639-3-names>}
        {this.match.params.table === 'sil-iso-639-3-macrolanguages' && <sil-iso-639-3-macrolanguages></sil-iso-639-3-macrolanguages>}
        {this.match.params.table === 'sil-iso-639-3-retirements' && <sil-iso-639-3-retirements></sil-iso-639-3-retirements>}
        {this.match.params.table === 'sil-table-of-countries' && <sil-table-of-countries></sil-table-of-countries>}
        {this.match.params.table === 'sil-table-of-languages-in-country' && <sil-table-of-languages-in-country></sil-table-of-languages-in-country>}



      </Host>
    );
  }
}
