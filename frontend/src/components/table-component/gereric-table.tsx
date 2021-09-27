import { Prop, Component, Host, h, Event, EventEmitter } from '@stencil/core';


function objectValues<T extends {}>(obj: T) {
  const test = Object.keys(obj).map((objKey) => obj[objKey as keyof T]);
  console.log(test);
  return Object.keys(obj).map((objKey) => obj[objKey as keyof T]);
}

/*function objectKeys<T extends {}>(obj: T) {
  return Object.keys(obj).map((objKey) => objKey as keyof T);
}*/


@Component({
  tag: 'generic-table',
  styleUrl: 'generic-table.css',
  shadow: true,
})

export class genericTable {
  @Prop() name: String;
  @Prop() columns: Array<any>;
  @Prop() values: Array<any>; 

  @Event({
    eventName: 'rowClicked',
    bubbles: true,
  }) rowClicked : EventEmitter<number>;

  onRowClick(id){
    this.rowClicked.emit(id);
  }
  
  
  render() {
    console.log('Columns: ', this.columns);
    return (
      <Host>
        <slot></slot>
        <h1>{this.name}</h1>
        <div class="main">
          <table>
            <thead>
              <tr>
                {this.columns.map((column)=>
                    <th>{column}</th>
                )}
              </tr>
            </thead>
            <tbody>
              {this.values.map((value) =>
              <tr onClick={() => this.onRowClick(value?.id)}>
                {objectValues(value).map((array)=>
                <td>{array}</td>
                )} 
              </tr>
              )}
            </tbody>
          </table>
        </div>
      </Host>
    );
  }
}
