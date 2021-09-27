import { Component, Host, h, Listen, State, Prop} from '@stencil/core';
import { RouterHistory } from '@stencil/router';
import { GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';

class readAllResponse extends GenericResponse {
  id: number;
  email: string;
}

const tableColumns = [
    "Id",
    "Address",
    "Name",
    "Country",
];

const tableValues = [
  {
    id: 1,
    address: "firstValue",
    name: "",
    country: "USA"
  },
  {
    id: 2,
    address: "secondValue",
    name: "test",
    country: "Brazil"
  }
];

let rowId = 0;


@Component({
  tag: 'global-role-column-grants',
  styleUrl: 'global-role-column-grants.css',
  shadow: true,
})

export class GlobalRoleColumnGrants {
  @Prop() history: RouterHistory;

  @State() email: string;
  @State() password: string;
  @State() isOpen: boolean;

  @Listen('rowClicked')
  async handleClick(event){
    const result = await fetchAs<{}, readAllResponse>('table/global-role-column-grants', { });
    console.log("Result: ", result);
    if(event && event.detail){
      rowId = event.detail;
      this.isOpen = !this.isOpen;
    }
  }

  @Listen('modalClosed')
  handleModalClose(event){
    if(event && event.detail){
      this.isOpen = !this.isOpen;
      console.log('boolean: ', event.detail)
    }
  }

  render() {
    return (
      <Host>
         <create-update-modal
         isOpen={this.isOpen}
         >
          
          </create-update-modal>
        <slot></slot>
        <generic-table 
          name="Test" 
          columns={tableColumns} 
          values={tableValues}>
        
        </generic-table>
       
      </Host>
      
    );
  }
}
