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
        language_id: number;
        sensitivity: string;
        parent: number;
        translator: number;
        location: string;
        title: string;
        content: string;
        reviewed: boolean;
        prayer_type: string;

    };
}
class SubmitPrayerRequestResponse extends GenericResponse {
    prayerRequest: UpPrayerRequest;
}


class UpPrayerRequestListRequest {
    token: string;
}

class UpPrayerRequestListResponse {
    error: ErrorType;
    prayerRequests: UpPrayerRequest[];
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
    @State() prayerRequestsResponse: UpPrayerRequestListResponse;
    @State() peoplesResponse: AdminPeopleListResponse;

    newLanguage_id: number;
    newSensitivity: string;
    newParent: number;
    newTranslator: number;
    newLocation: string;
    newTitle: string;
    newContent: string;
    newReviewed: boolean;
    newPrayer_type: string;

    language_idChange(event){
        this.newLanguage_id = event.target.value;
    }

    sensitivityChange(event){
        this.newSensitivity = event.target.value;
    }

    parentChange(event){
        this.newParent = event.target.value;
    }

    translatorChange(event){
        this.newTranslator = event.target.value;
    }

    locationChange(event){
        this.newLocation = event.target.value;
    }

    titleChange(event){
        this.newTitle = event.target.value;
    }

    contentChange(event){
        this.newContent = event.target.value;
    }

    reviewedChange(event){
        this.newReviewed = event.target.value;
    }

    prayer_typeChange(event){
        this.newPrayer_type = event.target.value;
    }

    submitPrayerRequest = async (event: MouseEvent) => {
        event.preventDefault();
        event.stopPropagation();

        const createResponse = await fetchAs<SubmitPrayerRequestRequest, SubmitPrayerRequestResponse>('prayer-requests/create', {
            token: globals.globalStore.state.token,
            prayerRequest: {
                language_id: this.newLanguage_id,
                sensitivity: this.newSensitivity,
                parent: this.newParent,
                translator: this.newTranslator,
                location: this.newLocation,
                title: this.newTitle,
                content: this.newContent,
                reviewed: this.newReviewed,
                prayer_type: this.newPrayer_type,
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
        this.prayerRequestsResponse = await fetchAs<UpPrayerRequestListRequest, UpPrayerRequestListResponse>('up-prayer-requests/list', {
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
                            <ion-label>Language ID</ion-label>
                            <ion-input name="language_id" onInput={event => this.language_idChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Sensitivity</ion-label>
                            <ion-select okText="Ok" onIonChange={event => this.sensitivityChange(event)} cancelText="Cancel">
                                {/* <ion-select-option value="">Select Sensitivity</ion-select-option> */}
                                <ion-select-option value="Low">Low</ion-select-option>
                                <ion-select-option value="Medium">Medium</ion-select-option>
                                <ion-select-option value="High">High</ion-select-option>
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Parent</ion-label>
                            <ion-select okText="Ok" onIonChange={event => this.parentChange(event)} cancelText="Cancel">
                                {this.prayerRequestsResponse.prayerRequests.map(option => (
                                    <ion-select-option value={option.id}>{option.title}</ion-select-option>
                                ))}
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Translator</ion-label>
                            <ion-input name="translator" onInput={event => this.translatorChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Location</ion-label>
                            <ion-input name="location" onInput={event => this.locationChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Title</ion-label>
                            <ion-input name="title" onInput={event => this.titleChange(event)}></ion-input>
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
                    <ion-col>
                        <ion-item>
                            <ion-label>Reviewed</ion-label>
                            <ion-select okText="Ok" onIonChange={event => this.reviewedChange(event)} cancelText="Cancel">
                                <ion-select-option value="">Null</ion-select-option>
                                <ion-select-option value="true">True</ion-select-option>
                                <ion-select-option value="false">False</ion-select-option>
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Prayer Type</ion-label>
                            <ion-select okText="Ok" onIonChange={event => this.prayer_typeChange(event)} cancelText="Cancel">
                                {/* <ion-select-option value="">Select Prayer Type</ion-select-option> */}
                                <ion-select-option value="Request">Request</ion-select-option>
                                <ion-select-option value="Update">Update</ion-select-option>
                                <ion-select-option value="Celebration">Celebration</ion-select-option>
                            </ion-select>
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
