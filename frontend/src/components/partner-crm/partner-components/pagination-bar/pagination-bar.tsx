import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../../common/types';
import { fetchAs } from '../../../../common/utility';
import { globals } from '../../../../core/global.store';
import { MatchResults } from '@stencil/router';


@Component({
    tag: 'pagination-bar',
    styleUrl: 'pagination-bar.css',
    shadow: true,
})

export class PaginationBar{



    render() {
        return (
            <Host>
                
            </Host>
        )
    }

}