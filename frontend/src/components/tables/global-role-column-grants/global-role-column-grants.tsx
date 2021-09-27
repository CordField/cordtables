import { Component, Host, h, Listen, State} from '@stencil/core';


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
  @State() isOpen: boolean;

  @Listen('rowClicked')
  handleClick(event){
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
