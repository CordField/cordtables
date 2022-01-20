import { Component, Host, h, Prop, State, Event, EventEmitter } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { MatchResults } from '@stencil/router';


@Component({
    tag: 'search-input',
    styleUrl: 'search-input.css',
    shadow: true,
})

export class SearchInput{
    @State() value: string;
    @Event({ bubbles: true, composed: true }) doSearch: EventEmitter<string>;

    handleKeyup(p){
        this.doSearch.emit(p.target.value)
    }

    render() {
        return (
            <Host>
                <div class="search-input">
                    <input type="text" value={this.value} name="search" onKeyUp={(p) => this.handleKeyup(p)} placeholder="Search" />
                </div>
            </Host>
        )
    }

}