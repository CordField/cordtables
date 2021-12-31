import { Component, Host, h, State, Prop } from '@stencil/core';
import { RouterHistory, injectHistory, MatchResults } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { globals } from '../../../core/global.store';
import { fetchAs } from '../../../common/utility';
import { v4 as uuidv4 } from 'uuid';
import '@ionic/core'

class UpdatePrayerRequestRequest {
    token: string;
    prayerRequest: {
        id: string;
        request_language_id: string;
        target_language_id: string;
        sensitivity: string;
        organization_name: string;
        parent: string;
        translator: string;
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


class GetPrayerRequestRequest {
    token: string;
    id: string;
}

class GetPrayerRequestResponse extends GenericResponse {
    prayerRequest: GetPrayerRequest;
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
  tag: 'prayer-request-edit-page',
  styleUrl: 'prayer-request-edit-page.css',
  shadow: false,
})
export class PrayerRequestEditPage {
    @Prop() requestId: any;
    @Prop() history: RouterHistory;
    @State() prayerRequestsResponse: UpPrayerRequestListResponse;
    @State() peoplesResponse: AdminPeopleListResponse;
    @State() getResponse: any;

    newRequest_language_id: string;
    newTarget_language_id: string;
    newSensitivity: string;
    newOrganization_name: string;
    newParent: string;
    newTranslator: string;
    newLocation: string;
    newTitle: string;
    newContent: string;
    newReviewed: boolean;
    newPrayer_type: string;

    
    
    request_language_idChange(event) {
        this.newRequest_language_id = event.target.value;
    }

    target_language_idChange(event) {
        this.newTarget_language_id = event.target.value;
    }

    sensitivityChange(event){
        this.newSensitivity = event.target.value;
    }

    organization_nameChange(event){
        this.newOrganization_name = event.target.value;
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

    updatePrayerRequest = async (event: MouseEvent) => {
        event.preventDefault();
        event.stopPropagation();

        const createResponse = await fetchAs<UpdatePrayerRequestRequest, SubmitPrayerRequestResponse>('prayer-requests/update', {
            token: globals.globalStore.state.token,
            prayerRequest: {
                id: this.getResponse.id,
                request_language_id: this.newRequest_language_id,
                target_language_id: this.newTarget_language_id,
                sensitivity: this.newSensitivity,
                organization_name: this.newOrganization_name,
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

    async getPrayerRequest() {
        
        const getRequestResponse = await fetchAs<GetPrayerRequestRequest, GetPrayerRequestResponse>('prayer-requests/get', {
            token: globals.globalStore.state.token,
            id: this.requestId
        });
        if(getRequestResponse.error === ErrorType.NoError) {
            this.getResponse = getRequestResponse.prayerRequest
            this.newRequest_language_id = this.getResponse.request_language_id
            this.newTarget_language_id = this.getResponse.request_language_id
            this.newSensitivity = this.getResponse.sensitivity
            this.newOrganization_name = this.getResponse.organization_name
            this.newParent = this.getResponse.parent
            this.newTranslator = this.getResponse.translator
            this.newLocation = this.getResponse.location
            this.newTitle = this.getResponse.title
            this.newContent = this.getResponse.content
            this.newReviewed = this.getResponse.reviewed
            this.newPrayer_type = this.getResponse.prayer_type
        }
        else {
            this.history.replace('/page/not-found')
        }
    }

    async getParentsList() {
        this.prayerRequestsResponse = await fetchAs<UpPrayerRequestListRequest, UpPrayerRequestListResponse>('up-prayer-requests/list', {
          token: globals.globalStore.state.token,
        });
    }

    async componentWillLoad() {
        await this.getPrayerRequest();
        await this.getParentsList();
    }

    clickedBackButton = () => {
        this.history.replace('/page/prayer-requests');
    }

    loadParent(){

    }

    render() {
        console.log(this.requestId)
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
                            <ion-label>Request Language ID</ion-label>
                            <ion-input name="request_language_id" value={this.getResponse.request_language_id} onInput={event => this.request_language_idChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Target Language ID</ion-label>
                            <ion-input name="target_language_id" value={this.getResponse.target_language_id} onInput={event => this.target_language_idChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>

                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Sensitivity</ion-label>
                            <ion-select okText="Ok" value={this.getResponse.sensitivity} onIonChange={event => this.sensitivityChange(event)} cancelText="Cancel">
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
                            <ion-label>Organization Name</ion-label>
                            <ion-input name="organization_name" value={this.getResponse.organization_name} onInput={event => this.organization_nameChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Parent</ion-label>
                            <ion-select okText="Ok" value={this.getResponse.parent} onIonChange={event => this.parentChange(event)} cancelText="Cancel">
                                {this.prayerRequestsResponse.prayerRequests.map(option => (
                                    <ion-select-option onLoad={this.loadParent} value={option.id}>{option.title}</ion-select-option>
                                ))}
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Translator</ion-label>
                            <ion-input name="translator" value={this.getResponse.translator} onInput={event => this.translatorChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Location</ion-label>
                            <ion-input name="location" value={this.getResponse.location} onInput={event => this.locationChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Title</ion-label>
                            <ion-input name="title" value={this.getResponse.title} onInput={event => this.titleChange(event)}></ion-input>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Content</ion-label>
                            <ion-textarea name="content" onInput={event => this.contentChange(event)} value={this.getResponse.content}></ion-textarea>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Reviewed</ion-label>
                            <ion-select okText="Ok" value={this.getResponse.reviewed} onIonChange={event => this.reviewedChange(event)} cancelText="Cancel">
                                <ion-select-option value="">Null</ion-select-option>
                                <ion-select-option value={true}>True</ion-select-option>
                                <ion-select-option value={false}>False</ion-select-option>
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col>
                        <ion-item>
                            <ion-label>Prayer Type</ion-label>
                            <ion-select okText="Ok" value={this.getResponse.prayer_type} onIonChange={event => this.prayer_typeChange(event)} cancelText="Cancel">
                                {/* <ion-select-option value="">Select Prayer Type</ion-select-option> */}
                                <ion-select-option value="Request">Request</ion-select-option>
                                <ion-select-option value="Update">Update</ion-select-option>
                                <ion-select-option value="Celebration">Celebration</ion-select-option>
                            </ion-select>
                        </ion-item>
                    </ion-col>
                </ion-row>
                <ion-row>
                    <ion-col><ion-button  onClick={this.updatePrayerRequest} >Update</ion-button></ion-col>
                </ion-row>
                
            </ion-grid>
        </Host>
        );
    }
}
injectHistory(PrayerRequestEditPage)