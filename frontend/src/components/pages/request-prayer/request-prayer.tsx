import { Component, Host, h, State, Prop } from '@stencil/core';
import { RouterHistory, injectHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { globals } from '../../../core/global.store';
import { fetchAs } from '../../../common/utility';
import { v4 as uuidv4 } from 'uuid';
import '@ionic/core'

class SubmitPrayerRequestRequest {
    token: string;
    prayerRequest: {
        parent: number;
        subject: string;
        content: string;
    };
}
class SubmitPrayerRequestResponse extends GenericResponse {
    prayerRequest: CommonPrayerRequest;
}


class CommonPrayerRequestListRequest {
    token: string;
}

class CommonPrayerRequestListResponse {
    error: ErrorType;
    prayerRequests: CommonPrayerRequest[];
}

class AdminPeopleListRequest {
    token: string;
}

class AdminPeopleListResponse {
    error: ErrorType;
    peoples: AdminPeople[];
}

@Component({
  tag: 'request-prayer-page',
  styleUrl: 'request-prayer.css',
  shadow: false,
})
export class RequestPrayerPage {
    @Prop() history: RouterHistory;
    @State() prayerRequestsResponse: CommonPrayerRequestListResponse;
    @State() peoplesResponse: AdminPeopleListResponse;

    newParent: number;
    newContent: string;
    newSubject: string;

    parentChange(event){
        this.newParent = event.target.value;
    }

    subjectChange(event){
        this.newSubject = event.target.value;
    }

    contentChange(event){
        this.newContent = event.target.value;
    }

    submitPrayerRequest = async (event: MouseEvent) => {
        event.preventDefault();
        event.stopPropagation();

        const createResponse = await fetchAs<SubmitPrayerRequestRequest, SubmitPrayerRequestResponse>('prayer-requests/create', {
            token: globals.globalStore.state.token,
            prayerRequest: {
                parent: this.newParent,
                subject: this.newSubject,
                content: this.newContent,
            },
        });
      
        if (createResponse.error === ErrorType.NoError) {
            globals.globalStore.state.editMode = false;
            //this.getList();
            globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: 'item inserted successfully', id: uuidv4(), type: 'success' });
            this.history.replace('/page/prayer-requests');
        } else {
            globals.globalStore.state.notifications = globals.globalStore.state.notifications.concat({ text: createResponse.error, id: uuidv4(), type: 'error' });
            
        }
    }

    async getParentsList() {
        this.prayerRequestsResponse = await fetchAs<CommonPrayerRequestListRequest, CommonPrayerRequestListResponse>('common-prayer-requests/list', {
          token: globals.globalStore.state.token,
        });
    }

    async componentWillLoad() {
        await this.getParentsList();
    }

    clickedBackButton = () => {
        this.history.replace('/page/prayer-requests');
    }

    render() {
        return (
        <Host>
            <slot></slot>
            <ion-toolbar>
                <ion-title>Request Prayer</ion-title>
                <ion-buttons slot="end" >
                    <ion-button fill="outline" onClick={this.clickedBackButton} color="primary">Go Back</ion-button>
                </ion-buttons>
            </ion-toolbar>
            <ion-grid>
            <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Parent</ion-label>
                            <ion-select okText="Ok" onIonChange={event => this.parentChange(event)} cancelText="Cancel">
                                {this.prayerRequestsResponse.prayerRequests.map(option => (
                                    <ion-select-option value={option.id}>{option.subject}</ion-select-option>
                                ))}
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Subject</ion-label>
                            <ion-input name="subject" onInput={event => this.subjectChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Content</ion-label>
                            <ion-textarea name="content" onInput={event => this.contentChange(event)}></ion-textarea>
                        </ion-item>
                    </ion-col>
                </ion-row>

                
                <ion-row>
                    <ion-col><ion-button  onClick={this.submitPrayerRequest} >Submit</ion-button></ion-col>
                </ion-row>
                
            </ion-grid>
        </Host>
        );
    }
}
injectHistory(RequestPrayerPage)