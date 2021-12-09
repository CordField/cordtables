import { Component, Host, h } from '@stencil/core';
import { ColumnDescription } from '../../../../common/table-abstractions/types';

@Component({
  tag: 'site-text',
  styleUrl: 'site-text.css',
  shadow: true,
})
export class SiteText {

  handleUpdate = async (): Promise<boolean> => {
    return true;
  }

  handleDelete = async (): Promise<boolean>  => {
    return true;
  }

  columnData: ColumnDescription[] = [
    {
      field: 'english',
      displayName: 'English',
      width: 50,
      editable: false,
      deleteFn: this.handleDelete,
    },
    {
      field: 'comment',
      displayName: 'Comment',
      width: 200,
      editable: true,
      updateFn: this.handleUpdate,
    },
  ];

  render() {
    return (
      <div class="site-text">
        <div class="language-select-wrapper">
          <h4>Select Site Language</h4>
          <language-select />
        </div>
        <div class="translations">

        </div>
      </div>
    );
  }

}
