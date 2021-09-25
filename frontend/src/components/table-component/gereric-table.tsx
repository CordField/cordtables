import { Prop, Component, Host, h } from '@stencil/core';

@Component({
  tag: 'generic-table',
  styleUrl: 'generic-table.css',
  shadow: true,
})
export class genericTable {
  @Prop() name: String;
  @Prop() columns: Array<any>;
  @Prop() values: Array<any>; 
  
  render() {
    console.log(this.values);
    return (
      <Host>
        <slot></slot>
        <h1>{this.name}</h1>
        <div>
          <table>
            <thead>
              <tr>
                {this.columns.map((columns)=>
                    <th>{columns.name}</th>
                )}
              </tr>
            </thead>
            <tbody>
                {this.values.map((values, index)=>
                
                  <tr key={id}
                      <td>{values.name}</td>
                  )}  
            </tbody>
          </table>
        </div>
      </Host>
    );
  }
}
