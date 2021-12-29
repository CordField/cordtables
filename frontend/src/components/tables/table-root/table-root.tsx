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

        <stencil-route url={'/table/sc/funding-accounts'} component="sc-funding-accounts" />
        <stencil-route url={'/table/sc/internship-engagements'} component="sc-internship-engagements" />
        <stencil-route url={'/table/sc/known-languages-by-person'} component="sc-known-languages-by-person" />
        <stencil-route url={'/table/sc/language-engagements'} component="sc-language-engagements" />
        <stencil-route url={'/table/sc/organization-locations'} component="sc-organization-locations" />
        <stencil-route url={'/table/sc/organizations'} component="sc-organizations" />
        <stencil-route url={'/table/sc/partners'} component="sc-partners" />
        <stencil-route url={'/table/sc/partnerships'} component="sc-partnerships" />
        <stencil-route url={'/table/sc/person-unavailabilities'} component="sc-person-unavailabilities" />
        <stencil-route url={'/table/sc/pinned-projects'} component="sc-pinned-projects" />
        <stencil-route url={'/table/sc/posts'} component="sc-posts" />

        <stencil-route url={'/table/sc/product-scripture-references'} component="sc-product-scripture-references" />
        <stencil-route url={'/table/sc/products'} component="sc-products" />
        <stencil-route url={'/table/sc/project-locations'} component="sc-project-locations" />
        <stencil-route url={'/table/sc/projects'} component="sc-projects" />

        <stencil-route url={'/table/common/directories'} component="directories-table" />
        <stencil-route url={'/table/common/files'} component="files-table" />
        <stencil-route url={'/table/common/file-versions'} component="file-versions" />
        <stencil-route url={'/table/sc/field-regions'} component="sc-field-regions-table" />
        <stencil-route url={'/table/sc/field-zones'} component="sc-field-zones" />

        <stencil-route url={'/table/sc/global-partner-assessments'} component="sc-global-partner-assessments" />
        <stencil-route url={'/table/sc/global-partner-engagements'} component="sc-global-partner-engagements" />
        <stencil-route url={'/table/sc/global-partner-engagement-people'} component="sc-global-partner-engagement-people" />
        <stencil-route url={'/table/sc/global-partner-performance'} component="sc-global-partner-performance" />
        <stencil-route url={'/table/common/coalitions'} component="common-coalitions" />
        <stencil-route url={'/table/common/coalition-memberships'} component="common-coalition-memberships" />

        {this.match.params.table === 'up-prayer-requests' && <up-prayer-requests></up-prayer-requests>}
        {this.match.params.table === 'up-prayer-notifications' && <up-prayer-notifications></up-prayer-notifications>}

        {this.match.params.table === 'sc-languages-ex' && <languages-ex></languages-ex>}

        <stencil-route url={'/table/common/organizations'} component="common-organizations" />
        <stencil-route url={'/table/common/tickets'} component="tickets-table" />
        <stencil-route url={'/table/common/ticket-graph'} component="ticket-graph" />
        <stencil-route url={'/table/sc/budget-records'} component="sc-budget-records" />
        <stencil-route url={'/table/common/ticket-assignments'} component="ticket-assignments" />
        <stencil-route url={'/table/common/work-records'} component="work-records" />
        <stencil-route url={'/table/common/work-estimates'} component="work-estimates" />
        <stencil-route url={'/table/common/ticket-feedback'} component="ticket-feedback" />
        <stencil-route url={'/table/common/scripture-reference'} component="scripture-references" />

        <stencil-route url={'/table/sc/locations'} component="sc-locations" />
        <stencil-route url={'/table/common/cell-channels'} component="common-cell-channels" />
        <stencil-route url={'/table/common/discussion-channels'} component="discussion-channels" />
        <stencil-route url={'/table/common/threads'} component="common-threads" />
        <stencil-route url={'/table/common/posts'} component="common-posts" />
        <stencil-route url={'/table/sc/languages'} component="sc-languages" />

        <stencil-route url={'/table/common/locations'} component="common-locations" />
        <stencil-route url={'/table/common/notes'} component="common-notes" />
        <stencil-route url={'/table/common/org-chart-positions'} component="common-org-chart-positions" />
        <stencil-route url={'/table/common/org-chart-position-graph'} component="common-org-chart-position-graph" />
        <stencil-route url={'/table/common/people-graph'} component="common-people-graph" />
        <stencil-route url={'/table/common/people-to-org-relationships'} component="common-people-to-org-relationships" />
        <stencil-route url={'/table/common/stage-graph'} component="common-stage-graph" />
        <stencil-route url={'/table/common/stage-notifications'} component="common-stage-notifications" />
        <stencil-route url={'/table/common/stage-role-column-grants'} component="common-stage-role-column-grants" />
        <stencil-route url={'/table/common/stages'} component="common-stages" />
        <stencil-route url={'/table/common/workflows'} component="common-workflows" />
        <stencil-route url={'/table/sc/ethnologue'} component="sc-ethnologue" />
        <stencil-route url={'/table/sc/global-partner-transitions'} component="sc-global-partner-transitions" />
        <stencil-route url={'/table/sc/people'} component="sc-people" />
        <stencil-route
          url={['/table/common/site-text', '/table/common/site-text-languages', '/table/common/site-text-translations', '/table/common/site-text-strings']}
          component="site-text"
        />

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
