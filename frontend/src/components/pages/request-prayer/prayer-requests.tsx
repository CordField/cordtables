import { Component, Host, h, State, Prop} from '@stencil/core';
import { ErrorType, GenericResponse } from '../../../common/types';
import { globals } from '../../../core/global.store';
import { fetchAs } from '../../../common/utility';
import {  injectHistory,RouterHistory } from '@stencil/router';
import { v4 as uuidv4 } from 'uuid';
import '@ionic/core'
import { alertController, toastController } from '@ionic/core';

// class SubmitPrayerRequestRequest {
//     token: string;
//     prayerRequest: {
//         parent: number;
//         subject: string;
//         content: string;
//         notify: [];
//     };
// }
// class SubmitPrayerRequestResponse extends GenericResponse {
//     prayerRequest: CommonPrayerRequest;
// }


class CommonRequest {
    token: string;
}

class NotifyRequest {
    token: string;
    action: string;
    selectedRequest: string;
}

class NotifyRequestResponse extends GenericResponse {
    token: string;
    id: number; 
}

class PrayerRequestsListResponse {
    error: ErrorType;
    prayerRequests: PrayerRequest[];
}

// class AdminPeopleListRequest {
//     token: string;
// }

// class AdminPeopleListResponse {
//     error: ErrorType;
//     peoples: AdminPeople[];
// }

@Component({
  tag: 'page-prayer-requests',
  styleUrl: 'page-prayer-requests.css',
  shadow: false,
})
export class PagePrayerRequests {
    @Prop() history: RouterHistory;
    @State() prayerRequestsResponse: PrayerRequestsListResponse;
    // @State() peoplesResponse: AdminPeopleListResponse;

    selectedPrayerRequests = [];

    async getPrayerRequestsList() {
        console.log( globals.globalStore.state)
        this.prayerRequestsResponse = await fetchAs<CommonRequest, PrayerRequestsListResponse>('prayer-requests/list', {
          token: globals.globalStore.state.token,
        });
    }

    async presentAlert(header, subHead, message) {
        const alert = await alertController.create({
          cssClass: 'my-custom-class',
          header: header,
          subHeader: subHead,
          message: message,
          buttons: ['OK']
        });
        await alert.present();
        const { role } = await alert.onDidDismiss();
    }

    clickedCreateNew = () => {
        this.history.push(`/page/request-prayer`);
    }

    clickedEdit = (event, id) => {
        this.history.replace(`/page/prayer-request-edit/${id}`);
    }

    async presentToast(message) {
        const toast = await toastController.create({
            message: message,
            duration: 2000
        });
        toast.present();
    }
    

    prayerRequestSelect = async (event) => {
        var action = "";
        if(event.target.checked){
            action = 'add'
        }
        else{
            action = 'remove'
        }
        const createResponse = await fetchAs<NotifyRequest, NotifyRequestResponse>('prayer-requests/notify', {
            token: globals.globalStore.state.token,
            action: action,
            selectedRequest: event.target.value,
        });

        if (createResponse.error === ErrorType.NoError) {
            globals.globalStore.state.editMode = false;
            //globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
            //this.history.replace('/page/prayer-requests');
            this.presentToast("Notify updated")
        } else {
            globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
            
        }
    }

    async componentWillLoad() {
        await this.getPrayerRequestsList();
    }

    render() {
        return (
        <Host>
            {/* <slot></slot> */}
            <ion-toolbar>
                <ion-title>Prayer Requests</ion-title>
                <ion-buttons slot="end" >
                    <ion-button fill="outline" onClick={this.clickedCreateNew} color="primary">Create New Prayer Request</ion-button>
                </ion-buttons>
            </ion-toolbar>
            <ion-list>
                {this.prayerRequestsResponse.prayerRequests.map(prayerRequest => (
                    <ion-item>
                        <ion-label>
                            <h2>{ prayerRequest.title }</h2>
                            <h3>{ prayerRequest.content }</h3>
                            <p>Created By: { prayerRequest.requestedBy }</p>
                        </ion-label>
                        <ion-checkbox slot="start" name="pr" checked={prayerRequest.notify?true:false} onIonChange={event => this.prayerRequestSelect(event) } value={ prayerRequest.id.toString() } ></ion-checkbox>
                        <ion-buttons slot="end" >
                            { prayerRequest.myRequest ? <ion-button fill="outline" onClick={event => this.clickedEdit(event, prayerRequest.id)} color="primary">Edit</ion-button> : '' }
                        </ion-buttons>
                    </ion-item>
                ))}
            </ion-list>,
        </Host>
        );
    }
}
injectHistory(PagePrayerRequests);