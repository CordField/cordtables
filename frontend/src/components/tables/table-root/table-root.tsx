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
        {console.log('params', this.match)}
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

        <stencil-route url={'/table/sc/funding-accounts'} exact={true} component="sc-funding-accounts" />
        <stencil-route url={'/table/sc/internship-engagements'} exact={true} component="sc-internship-engagements" />
        <stencil-route url={'/table/sc/known-languages-by-person'} exact={true} component="sc-known-languages-by-person" />
        <stencil-route url={'/table/sc/language-engagements'} exact={true} component="sc-language-engagements" />
        <stencil-route url={'/table/sc/organization-locations'} exact={true} component="sc-organization-locations" />
        <stencil-route url={'/table/sc/organizations'} exact={true} component="sc-organizations" />
        <stencil-route url={'/table/sc/partners'} exact={true} component="sc-partners" />
        <stencil-route url={'/table/sc/partnerships'} exact={true} component="sc-partnerships" />
        <stencil-route url={'/table/sc/person-unavailabilities'} exact={true} component="sc-person-unavailabilities" />
        <stencil-route url={'/table/sc/pinned-projects'} exact={true} component="sc-pinned-projects" />
        <stencil-route url={'/table/sc/posts'} exact={true} component="sc-posts" />

        <stencil-route url={'/table/sc/product-scripture-references'} exact={true} component="sc-product-scripture-references" />
        <stencil-route url={'/table/sc/products'} exact={true} component="sc-products" />
        <stencil-route url={'/table/sc/project-locations'} exact={true} component="sc-project-locations" />
        <stencil-route url={'/table/sc/projects'} exact={true} component="sc-projects" />

        {this.match.params.table === 'common-directories' && <directories-table></directories-table>}
        {this.match.params.table === 'common-files' && <files-table></files-table>}
        {this.match.params.table === 'common-file-versions' && <file-versions></file-versions>}
        <stencil-route url={'/table/sc/field-regions'} exact={true} component="sc-field-regions-table" />
        <stencil-route url={'/table/sc/field-zones'} exact={true} component="sc-field-zones" />

        <stencil-route url={'/table/sc/global-partner-assessments'} exact={true} component="sc-global-partner-assessments" />
        <stencil-route url={'/table/sc/global-partner-engagements'} exact={true} component="sc-global-partner-engagements" />
        <stencil-route url={'/table/sc/global-partner-engagement-people'} exact={true} component="sc-global-partner-engagement-people" />
        <stencil-route url={'/table/sc/global-partner-performance'} exact={true} component="sc-global-partner-performance" />
        {this.match.params.table === 'common-coalitions' && <common-coalitions></common-coalitions>}
        {this.match.params.table === 'common-coalition-memberships' && <common-coalition-memberships></common-coalition-memberships>}

        {this.match.params.table === 'up-prayer-requests' && <up-prayer-requests></up-prayer-requests>}
        {this.match.params.table === 'up-prayer-notifications' && <up-prayer-notifications></up-prayer-notifications>}

        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}
        {this.match.params.table === 'common-organizations' && <common-organizations></common-organizations>}
        {this.match.params.table === 'common-tickets' && <tickets-table></tickets-table>}
        {this.match.params.table === 'common-ticket-graph' && <ticket-graph></ticket-graph>}
        <stencil-route url={'/table/sc/budget-records'} exact={true} component="sc-budget-records" />
        {this.match.params.table === 'common-ticket-assignments' && <ticket-assignments></ticket-assignments>}
        {this.match.params.table === 'common-work-records' && <work-records></work-records>}
        {this.match.params.table === 'common-work-estimates' && <work-estimates></work-estimates>}
        {this.match.params.table === 'common-ticket-feedback' && <ticket-feedback></ticket-feedback>}
        {this.match.params.table === 'common-scripture-references' && <scripture-references></scripture-references>}
        <stencil-route url={'/table/sc/locations'} exact={true} component="sc-locations" />
        {this.match.params.table === 'common-cell-channels' && <common-cell-channels></common-cell-channels>}
        {this.match.params.table === 'common-threads' && <common-threads></common-threads>}
        {this.match.params.table === 'common-posts' && <common-posts></common-posts>}
        <stencil-route url={'/table/sc/languages'} exact={true} component="sc-languages" />

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
        <stencil-route url={'/table/sc/ethnologue'} exact={true} component="sc-ethnologue" />
        <stencil-route url={'/table/sc/global-partner-transitions'} exact={true} component="sc-global-partner-transitions" />
        <stencil-route url={'/table/sc/people'} exact={true} component="sc-people" />

        {['common-site-text', 'common-site-text-languages', 'common-site-text-strings', 'common-site-text-translations'].includes(this.match.params.table) && (
          <site-text></site-text>
        )}

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
