import { Component, Host, h, State, Listen, Prop, Event, EventEmitter } from '@stencil/core';
import { v4 } from 'uuid';
import { ErrorType } from '../../common/types';
import { fetchAs } from '../../common/utility';
import { globals } from '../../core/global.store';
import { CommonThread } from '../tables/common/threads/types';

class SearchRequest {
  searchKeyword: string;
  searchColumnName: string;
  token: string;
}

class SearchResponse<T> {
  error: ErrorType;
  data?: T[];
}

@Component({
  tag: 'search-form',
  styleUrl: 'search.css',
  shadow: true,
})
export class Search {
  @Prop() columnNames: string[] = [];
  searchColumnName: string = this.columnNames[0];
  searchKeyword: string = '';
  @Event({ eventName: 'searchResults' }) searchResults: EventEmitter<any>;
  async handleSubmit(e) {
    e.preventDefault();
    const searchUrl = window.location.href.split('/').slice(-2).join('/').concat('/search');
    console.log(this.searchKeyword);
    const result = await fetchAs<SearchRequest, SearchResponse<any>>(searchUrl, {
      //   tableName: searchUrl.split('/').join('.'),
      searchColumnName: this.searchColumnName,
      searchKeyword: this.searchKeyword,
      token: globals.globalStore.state.token,
    });
    if (result.error === ErrorType.NoError) {
      this.searchResults.emit(result.data);
    } else {
      globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: result.error, id: v4(), type: 'error' });
    }
  }
  render() {
    return (
      <Host>
        <form onSubmit={e => this.handleSubmit(e)}>
          {
            //1.dropdown for the column names
            //2.text field for the search term
          }
          <select
            onInput={e => {
              this.searchColumnName = (e.target as HTMLSelectElement).value;
            }}
          >
            <option value="">Select Column to Search On</option>
            {this.columnNames.map(columnName => (
              <option value={columnName}>{columnName}</option>
            ))}
          </select>
          <label htmlFor="searchKeyword">
            <input type="text" name="searchKeyword" value={this.searchKeyword} onChange={e => (this.searchKeyword = (e.target as HTMLInputElement).value)} />
          </label>
          <button>Search!</button>
        </form>
      </Host>
    );
  }
}
