import { Component, Host, h, State, Prop} from '@stencil/core';
import {  injectHistory,RouterHistory } from '@stencil/router';
import '@ionic/core'

@Component({
  tag: 'page-not-found',
  styleUrl: 'page-not-found.css',
  shadow: false,
})
export class PagePrayerRequests {
    @Prop() history: RouterHistory;

    goBack = () => {
        this.history.goBack();
    }
    render() {
        return (
        <Host>
            {/* <slot></slot> */}
            <ion-button fill="outline" onClick={this.goBack}>Go Back</ion-button>
            <h1>Page Not Found</h1>
        </Host>
        );
    }
}
injectHistory(PagePrayerRequests);