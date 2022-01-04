import { Component, Host, h, Prop, State } from '@stencil/core';
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



    render() {
        return (
            <Host>
                <div class="search-input">
                    <input type="text" name="search" placeholder="Search" />
                </div>
            </Host>
        )
    }

}