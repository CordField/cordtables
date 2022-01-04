import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { MatchResults } from '@stencil/router';


@Component({
    tag: 'filter-menu',
    styleUrl: 'filter-menu.css',
    shadow: true,
})

export class FilterMenu{



    render() {
        return (
            <Host>
                <div>
                    <select name="filter">
                        <option value="">Filter By</option>
                    </select>
                </div>
            </Host>
        )
    }

}