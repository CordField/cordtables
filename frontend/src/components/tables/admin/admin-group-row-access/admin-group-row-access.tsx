import { Component, Host, h, State } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { v4 as uuidv4 } from 'uuid';

class CreateGroupRowAccessExRequest {
  token: string;
  groupRowAccess: {
    group_id: string;
    table_name: string;
    row: number;
  };
}
class CreateGroupRowAccessExResponse extends GenericResponse {
  groupRowAccess: AdminGroupRowAccess;
}

class AdminGroupRowAccessListRequest {
  token: string;
}

class AdminGroupRowAccessListResponse {
  error: ErrorType;
  groupRowAccesses: AdminGroupRowAccess[];
}

class AdminGroupRowAccessUpdateRequest {
  token: string;
  column: string;
  value: any;
  id: string;
}

class AdminGroupRowAccessUpdateResponse {
  error: ErrorType;
  groupRowAccess: AdminGroupRowAccess | null = null;
}

class DeleteGroupRowAccessExRequest {
  id: string;
  token: string;
}

class DeleteGroupRowAccessExResponse extends GenericResponse {
  id: string;
}

@Component({
  tag: 'admin-group-row-access',
  styleUrl: 'admin-group-row-access.css',
  shadow: true,
})
export class AdminGroupRowAccesss {
  @State() groupRowAccessesResponse: AdminGroupRowAccessListResponse;

  newGroup_id: string;
  newTable_name: string;
  newRow: number;

  handleUpdate = async (id: string, columnName: string, value: string): Promise<boolean> => {
    const updateResponse = await fetchAs<AdminGroupRowAccessUpdateRequest, AdminGroupRowAccessUpdateResponse>('admin/group-row-access/update-read', {
      token: globals.globalStore.state.token,
      column: columnName,
      id: id,
      value: value !== '' ? value : null,
    });

    console.log(updateResponse);

    if (updateResponse.error == ErrorType.NoError) {
      this.groupRowAccessesResponse = {
        error: ErrorType.NoError,
        groupRowAccesses: this.groupRowAccessesResponse.groupRowAccesses.map(groupRowAccess => (groupRowAccess.id === id ? updateResponse.groupRowAccess : groupRowAccess)),
      };
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item updated successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: updateResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  handleDelete = async id => {
    const deleteResponse = await fetchAs<DeleteGroupRowAccessExRequest, DeleteGroupRowAccessExResponse>('admin/group-row-access/delete', {
      id,
      token: globals.globalStore.state.token,
    });
    if (deleteResponse.error === ErrorType.NoError) {
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item deleted successfully', id: uuidv4(), type: 'success' });
      return true;
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: deleteResponse.error, id: uuidv4(), type: 'error' });
      return false;
    }
  };

  async getList() {
    this.groupRowAccessesResponse = await fetchAs<AdminGroupRowAccessListRequest, AdminGroupRowAccessListResponse>('admin/group-row-access/list', {
      token: globals.globalStore.state.token,
    });
  }

  group_idChange(event) {
    this.newGroup_id = event.target.value;
  }

  table_nameChange(event) {
    this.newTable_name = event.target.value;
  }

  rowChange(event) {
    this.newRow = event.target.value;
  }

  handleInsert = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const createResponse = await fetchAs<CreateGroupRowAccessExRequest, CreateGroupRowAccessExResponse>('admin/group-row-access/create-read', {
      token: globals.globalStore.state.token,
      groupRowAccess: {
        group_id: this.newGroup_id,
        table_name: this.newTable_name,
        row: this.newRow,
      },
    });

    if (createResponse.error === ErrorType.NoError) {
      globals.globalStore.state.editMode = false;
      this.getList();
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
    }
  };

  columnData: ColumnDescription[] = [
    {
      field: 'id',
      displayName: 'ID',
      width: 250,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'group_id',
      displayName: 'Group ID',
      width: 250,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'table_name',
      displayName: 'Table Name',
      width: 200,
      editable: true,
      selectOptions: [
        { display: 'admin.database_version_control', value: 'admin.database_version_control' },
        { display: 'admin.email_tokens', value: 'admin.email_tokens' },
        { display: 'admin.group_memberships', value: 'admin.group_memberships' },
        { display: 'admin.group_row_access', value: 'admin.group_row_access' },
        { display: 'admin.groups', value: 'admin.groups' },
        { display: 'admin.peers', value: 'admin.peers' },
        { display: 'admin.people', value: 'admin.people' },
        { display: 'admin.role_column_grants', value: 'admin.role_column_grants' },
        { display: 'admin.role_memberships', value: 'admin.role_memberships' },
        { display: 'admin.role_table_permissions', value: 'admin.role_table_permissions' },
        { display: 'admin.roles', value: 'admin.roles' },
        { display: 'admin.tokens', value: 'admin.tokens' },
        { display: 'admin.users', value: 'admin.users' },
        { display: 'common.blogs', value: 'common.blogs' },
        { display: 'common.blog_posts', value: 'common.blog_posts' },
        { display: 'common.cell_channels', value: 'common.cell_channels' },
        { display: 'common.coalition_memberships', value: 'common.coalition_memberships' },
        { display: 'common.coalitions', value: 'common.coalitions' },
        { display: 'common.directories', value: 'common.directories' },
        { display: 'common.discussion_channels', value: 'common.discussion_channels' },
        { display: 'common.education_by_person', value: 'common.education_by_person' },
        { display: 'common.education_entries', value: 'common.education_entries' },
        { display: 'common.file_versions', value: 'common.file_versions' },
        { display: 'common.files', value: 'common.files' },
        { display: 'common.locations', value: 'common.locations' },
        { display: 'common.notes', value: 'common.notes' },
        { display: 'common.organizations', value: 'common.organizations' },
        { display: 'common.people_graph', value: 'common.people_graph' },
        { display: 'common.people_to_org_relationships', value: 'common.people_to_org_relationships' },
        { display: 'common.posts', value: 'common.posts' },
        { display: 'common.scripture_references', value: 'common.scripture_references' },
        { display: 'common.site_text', value: 'common.site_text' },
        { display: 'common.stage_graph', value: 'common.stage_graph' },
        { display: 'common.stage_notifications', value: 'common.stage_notifications' },
        { display: 'common.stage_role_column_grants', value: 'common.stage_role_column_grants' },
        { display: 'common.stages', value: 'common.stages' },
        { display: 'common.threads', value: 'common.threads' },
        { display: 'common.ticket_assignments', value: 'common.ticket_assignments' },
        { display: 'common.ticket_feedback', value: 'common.ticket_feedback' },
        { display: 'common.ticket_graph', value: 'common.ticket_graph' },
        { display: 'common.tickets', value: 'common.tickets' },
        { display: 'common.work_estimates', value: 'common.work_estimates' },
        { display: 'common.work_records', value: 'common.work_records' },
        { display: 'common.workflows', value: 'common.workflows' },
        { display: 'sil.country_codes', value: 'sil.country_codes' },
        { display: 'sil.language_codes', value: 'sil.language_codes' },
        { display: 'sil.language_index', value: 'sil.language_index' },
        { display: 'sc.ethnologue', value: 'sc.ethnologue' },
        { display: 'sc.budget_records', value: 'sc.budget_records' },
        { display: 'sc.budgets', value: 'sc.budgets' },
        { display: 'sc.ceremonies', value: 'sc.ceremonies' },
        { display: 'sc.change_to_plans', value: 'sc.change_to_plans' },
        { display: 'sc.field_regions', value: 'sc.field_regions' },
        { display: 'sc.field_zones', value: 'sc.field_zones' },
        { display: 'sc.funding_accounts', value: 'sc.funding_accounts' },
        { display: 'sc.global_partner_assessments', value: 'sc.global_partner_assessments' },
        { display: 'sc.global_partner_engagements', value: 'sc.global_partner_engagements' },
        { display: 'sc.global_partner_engagement_people', value: 'sc.global_partner_engagement_people' },
        { display: 'sc.global_partner_performance', value: 'sc.global_partner_performance' },
        { display: 'sc.internship_engagements', value: 'sc.internship_engagements' },
        { display: 'sc.known_languages_by_person', value: 'sc.known_languages_by_person' },
        { display: 'sc.language_engagements', value: 'sc.language_engagements' },
        { display: 'sc.languages', value: 'sc.languages' },
        { display: 'sc.locations', value: 'sc.locations' },
        { display: 'sc.organization_locations', value: 'sc.organization_locations' },
        { display: 'sc.organizations', value: 'sc.organizations' },
        { display: 'sc.partners', value: 'sc.partners' },
        { display: 'sc.partnerships', value: 'sc.partnerships' },
        { display: 'sc.people', value: 'sc.people' },
        { display: 'sc.periodic_reports', value: 'sc.periodic_reports' },
        { display: 'sc.person_unavailabilities', value: 'sc.person_unavailabilities' },
        { display: 'sc.pinned_projects', value: 'sc.pinned_projects' },
        { display: 'sc.posts', value: 'sc.posts' },
        { display: 'sc.product_scripture_references', value: 'sc.product_scripture_references' },
        { display: 'sc.products', value: 'sc.products' },
        { display: 'sc.project_locations', value: 'sc.project_locations' },
        { display: 'sc.project_members', value: 'sc.project_members' },
        { display: 'sc.projects', value: 'sc.projects' },
        { display: 'common.prayer_requests', value: 'common.prayer_requests' },
        { display: 'common.prayer_notifications', value: 'common.prayer_notifications' },
      ],
      updateFn: this.handleUpdate,
    },
    {
      field: 'row',
      displayName: 'Row',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'created_at',
      displayName: 'Created At',
      width: 250,
      editable: false,
    },
    {
      field: 'created_by',
      displayName: 'Created By',
      width: 100,
      editable: false,
    },
    {
      field: 'modified_at',
      displayName: 'Last Modified',
      width: 250,
      editable: false,
    },
    {
      field: 'modified_by',
      displayName: 'Last Modified By',
      width: 100,
      editable: false,
    },
    {
      field: 'owning_person',
      displayName: 'Owning Person ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
    {
      field: 'owning_group',
      displayName: 'Owning Group ID',
      width: 100,
      editable: true,
      updateFn: this.handleUpdate,
    },
  ];

  async componentWillLoad() {
    await this.getList();
    // await this.getFilesList();
  }

  render() {
    return (
      <Host>
        <slot></slot>
        {/* table abstraction */}
        {this.groupRowAccessesResponse && <cf-table rowData={this.groupRowAccessesResponse.groupRowAccesses} columnData={this.columnData}></cf-table>}

        {/* create form - we'll only do creates using the minimum amount of fields
         and then expect the user to use the update functionality to do the rest*/}

        {globals.globalStore.state.editMode === true && (
          <form class="form-thing">
            <div id="group_id-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="group_id">group_id</label>
              </span>
              <span class="form-thing">
                <input type="text" id="group_id" name="group_id" onInput={event => this.group_idChange(event)} />
              </span>
            </div>

            <div id="table_name-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="table_name">Table Name</label>
              </span>
              <span class="form-thing">
                <select id="table_name" name="table_name" onInput={event => this.table_nameChange(event)}>
                  <option value="">Select Table</option>
                  <option value="admin.database_version_control" selected={this.newTable_name === 'admin.database_version_control'}>
                    admin.database_version_control
                  </option>
                  <option value="admin.email_tokens" selected={this.newTable_name === 'admin.email_tokens'}>
                    admin.email_tokens
                  </option>
                  <option value="admin.group_memberships" selected={this.newTable_name === 'admin.group_memberships'}>
                    admin.group_memberships
                  </option>
                  <option value="admin.group_row_access" selected={this.newTable_name === 'admin.group_row_access'}>
                    admin.group_row_access
                  </option>
                  <option value="admin.groups" selected={this.newTable_name === 'admin.groups'}>
                    admin.groups
                  </option>
                  <option value="admin.peers" selected={this.newTable_name === 'admin.peers'}>
                    admin.peers
                  </option>
                  <option value="admin.people" selected={this.newTable_name === 'admin.people'}>
                    admin.people
                  </option>
                  <option value="admin.role_column_grants" selected={this.newTable_name === 'admin.role_column_grants'}>
                    admin.role_column_grants
                  </option>
                  <option value="admin.role_memberships" selected={this.newTable_name === 'admin.role_memberships'}>
                    admin.role_memberships
                  </option>
                  <option value="admin.role_table_permissions" selected={this.newTable_name === 'admin.role_table_permissions'}>
                    admin.role_table_permissions
                  </option>
                  <option value="admin.roles" selected={this.newTable_name === 'admin.roles'}>
                    admin.roles
                  </option>
                  <option value="admin.tokens" selected={this.newTable_name === 'admin.tokens'}>
                    admin.tokens
                  </option>
                  <option value="admin.users" selected={this.newTable_name === 'admin.users'}>
                    admin.users
                  </option>

                  <option value="common.blogs" selected={this.newTable_name === 'common.blogs'}>
                    common.blogs
                  </option>
                  <option value="common.blog_posts" selected={this.newTable_name === 'common.blog_posts'}>
                    common.blog_posts
                  </option>
                  <option value="common.cell_channels" selected={this.newTable_name === 'common.cell_channels'}>
                    common.cell_channels
                  </option>
                  <option value="common.coalition_memberships" selected={this.newTable_name === 'common.coalition_memberships'}>
                    common.coalition_memberships
                  </option>
                  <option value="common.coalitions" selected={this.newTable_name === 'common.coalitions'}>
                    common.coalitions
                  </option>
                  <option value="common.directories" selected={this.newTable_name === 'common.directories'}>
                    common.directories
                  </option>
                  <option value="common.discussion_channels" selected={this.newTable_name === 'common.discussion_channels'}>
                    common.discussion_channels
                  </option>
                  <option value="common.education_by_person" selected={this.newTable_name === 'common.education_by_person'}>
                    common.education_by_person
                  </option>
                  <option value="common.education_entries" selected={this.newTable_name === 'common.education_entries'}>
                    common.education_entries
                  </option>
                  <option value="common.file_versions" selected={this.newTable_name === 'common.file_versions'}>
                    common.file_versions
                  </option>
                  <option value="common.files" selected={this.newTable_name === 'common.files'}>
                    common.files
                  </option>
                  <option value="common.locations" selected={this.newTable_name === 'common.locations'}>
                    common.locations
                  </option>
                  <option value="common.notes" selected={this.newTable_name === 'common.notes'}>
                    common.notes
                  </option>
                  <option value="common.organizations" selected={this.newTable_name === 'common.organizations'}>
                    common.organizations
                  </option>
                  <option value="common.people_graph" selected={this.newTable_name === 'common.people_graph'}>
                    common.people_graph
                  </option>
                  <option value="common.people_to_org_relationships" selected={this.newTable_name === 'common.people_to_org_relationships'}>
                    common.people_to_org_relationships
                  </option>
                  <option value="common.posts" selected={this.newTable_name === 'common.posts'}>
                    common.posts
                  </option>
                  <option value="common.scripture_references" selected={this.newTable_name === 'common.scripture_references'}>
                    common.scripture_references
                  </option>
                  <option value="common.site_text" selected={this.newTable_name === 'common.site_text'}>
                    common.site_text
                  </option>
                  <option value="common.stage_graph" selected={this.newTable_name === 'common.stage_graph'}>
                    common.stage_graph
                  </option>
                  <option value="common.stage_notifications" selected={this.newTable_name === 'common.stage_notifications'}>
                    common.stage_notifications
                  </option>
                  <option value="common.stage_role_column_grants" selected={this.newTable_name === 'common.stage_role_column_grants'}>
                    common.stage_role_column_grants
                  </option>
                  <option value="common.stages" selected={this.newTable_name === 'common.stages'}>
                    common.stages
                  </option>
                  <option value="common.threads" selected={this.newTable_name === 'common.threads'}>
                    common.threads
                  </option>
                  <option value="common.ticket_assignments" selected={this.newTable_name === 'common.ticket_assignments'}>
                    common.ticket_assignments
                  </option>
                  <option value="common.ticket_feedback" selected={this.newTable_name === 'common.ticket_feedback'}>
                    common.ticket_feedback
                  </option>
                  <option value="common.ticket_graph" selected={this.newTable_name === 'common.ticket_graph'}>
                    common.ticket_graph
                  </option>
                  <option value="common.tickets" selected={this.newTable_name === 'common.tickets'}>
                    common.tickets
                  </option>
                  <option value="common.work_estimates" selected={this.newTable_name === 'common.work_estimates'}>
                    common.work_estimates
                  </option>
                  <option value="common.work_records" selected={this.newTable_name === 'common.work_records'}>
                    common.work_records
                  </option>
                  <option value="common.workflows" selected={this.newTable_name === 'common.workflows'}>
                    common.workflows
                  </option>

                  <option value="sil.country_codes" selected={this.newTable_name === 'sil.country_codes'}>
                    sil.country_codes
                  </option>
                  <option value="sil.language_codes" selected={this.newTable_name === 'sil.language_codes'}>
                    sil.language_codes
                  </option>
                  <option value="sil.language_index" selected={this.newTable_name === 'sil.language_index'}>
                    sil.language_index
                  </option>
                  <option value="sc.ethnologue" selected={this.newTable_name === 'sc.ethnologue'}>
                    sc.ethnologue
                  </option>

                  <option value="sc.budget_records" selected={this.newTable_name === 'sc.budget_records'}>
                    sc.budget_records
                  </option>
                  <option value="sc.budgets" selected={this.newTable_name === 'sc.budgets'}>
                    sc.budgets
                  </option>
                  <option value="sc.ceremonies" selected={this.newTable_name === 'sc.ceremonies'}>
                    sc.ceremonies
                  </option>
                  <option value="sc.change_to_plans" selected={this.newTable_name === 'sc.change_to_plans'}>
                    sc.change_to_plans
                  </option>
                  <option value="sc.field_regions" selected={this.newTable_name === 'sc.field_regions'}>
                    sc.field_regions
                  </option>
                  <option value="sc.field_zones" selected={this.newTable_name === 'sc.field_zones'}>
                    sc.field_zones
                  </option>
                  <option value="sc.funding_accounts" selected={this.newTable_name === 'sc.funding_accounts'}>
                    sc.funding_accounts
                  </option>
                  <option value="sc.global_partner_assessments" selected={this.newTable_name === 'sc.global_partner_assessments'}>
                    sc.global_partner_assessments
                  </option>
                  <option value="sc.global_partner_engagements" selected={this.newTable_name === 'sc.global_partner_engagements'}>
                    sc.global_partner_engagements
                  </option>
                  <option value="sc.global_partner_engagement_people" selected={this.newTable_name === 'sc.global_partner_engagement_people'}>
                    sc.global_partner_engagement_people
                  </option>
                  <option value="sc.global_partner_performance" selected={this.newTable_name === 'sc.global_partner_performance'}>
                    sc.global_partner_performance
                  </option>
                  <option value="sc.internship_engagements" selected={this.newTable_name === 'sc.internship_engagements'}>
                    sc.internship_engagements
                  </option>
                  <option value="sc.known_languages_by_person" selected={this.newTable_name === 'sc.known_languages_by_person'}>
                    sc.known_languages_by_person
                  </option>
                  <option value="sc.language_engagements" selected={this.newTable_name === 'sc.language_engagements'}>
                    sc.language_engagements
                  </option>
                  <option value="sc.languages" selected={this.newTable_name === 'sc.languages'}>
                    sc.languages
                  </option>
                  <option value="sc.locations" selected={this.newTable_name === 'sc.locations'}>
                    sc.locations
                  </option>
                  <option value="sc.organization_locations" selected={this.newTable_name === 'sc.organization_locations'}>
                    sc.organization_locations
                  </option>
                  <option value="sc.organizations" selected={this.newTable_name === 'sc.organizations'}>
                    sc.organizations
                  </option>
                  <option value="sc.partners" selected={this.newTable_name === 'sc.partners'}>
                    sc.partners
                  </option>
                  <option value="sc.partnerships" selected={this.newTable_name === 'sc.partnerships'}>
                    sc.partnerships
                  </option>
                  <option value="sc.people" selected={this.newTable_name === 'sc.people'}>
                    sc.people
                  </option>
                  <option value="sc.periodic_reports" selected={this.newTable_name === 'sc.periodic_reports'}>
                    sc.periodic_reports
                  </option>
                  <option value="sc.person_unavailabilities" selected={this.newTable_name === 'sc.person_unavailabilities'}>
                    sc.person_unavailabilities
                  </option>
                  <option value="sc.pinned_projects" selected={this.newTable_name === 'sc.pinned_projects'}>
                    sc.pinned_projects
                  </option>
                  <option value="sc.posts" selected={this.newTable_name === 'sc.posts'}>
                    sc.posts
                  </option>
                  <option value="sc.product_scripture_references" selected={this.newTable_name === 'sc.product_scripture_references'}>
                    sc.product_scripture_references
                  </option>
                  <option value="sc.products" selected={this.newTable_name === 'sc.products'}>
                    sc.products
                  </option>
                  <option value="sc.project_locations" selected={this.newTable_name === 'sc.project_locations'}>
                    sc.project_locations
                  </option>
                  <option value="sc.project_members" selected={this.newTable_name === 'sc.project_members'}>
                    sc.project_members
                  </option>
                  <option value="sc.projects" selected={this.newTable_name === 'sc.projects'}>
                    sc.projects
                  </option>
                </select>
              </span>
            </div>

            <div id="row-holder" class="form-input-item form-thing">
              <span class="form-thing">
                <label htmlFor="row">Row</label>
              </span>
              <span class="form-thing">
                <input type="text" id="row-name" name="row" onInput={event => this.rowChange(event)} />
              </span>
            </div>

            <span class="form-thing">
              <input id="create-button" type="submit" value="Create" onClick={this.handleInsert} />
            </span>
          </form>
        )}
      </Host>
    );
  }
}
