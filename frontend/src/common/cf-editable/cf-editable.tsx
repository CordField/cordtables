import { Component, Host, h, Prop, State, Element,  Event, EventEmitter } from '@stencil/core';


@Component({
    tag: 'cf-editable',
    styleUrl: 'cf-editable.css',
    shadow: true,
})

export class CfEditable {
    @Element() el: HTMLElement;
    @Prop() rowData: any;
    @Prop() field: string;
    @Prop() rowId: string;
    @Prop() endPoint: string;
    @Prop() value: any = null;
    @Prop() enableEdit: boolean = false;
    @Prop() selectOptions?: Array<{ display: string; value: any }> | null = null;
    @Prop() multiSelect?: boolean = false;
    @Prop() updateFn?: (id: string, columnName: any, value: any, endpoint: string) => Promise<boolean>;

    @Event({ bubbles: true, composed: true }) reloadPartners: EventEmitter<number>;

    @State() showEdit: boolean = false;
    @State() newValue: any;

    showEditClicked = () => {
      this.showEdit = true
    }

    cancelEditClicked = () => {
      this.showEdit = false
    }

    updateValue = async event => {
      this.newValue = event.target.value;
    };

    handleSelect(event, isArray) {
      if (isArray) {
        var options = event.target.options;
        var value = [];
        for (var i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            value.push(options[i].value);
            //console.log(options[i].value);
          }
        }
        console.log(value);
        this.newValue = '{' + value.toString() + '}';
        console.log(this.newValue);
      } else {
        this.newValue = event.target.value;
      }
    }

    submitEdit = async () => {
      const result = await this.updateFn(this.rowId, this.field, this.newValue, this.endPoint);  
      if (result) {
        this.reloadPartners.emit(1);
        if (typeof this.value === 'boolean') {
          this.value = this.newValue === 'true';
        } else {
          this.value = this.newValue;
        }
        this.showEdit = false;
      } else {
        // todo
      }
    };

    componentWillLoad() {
      this.newValue = this.value
    }
  
    render() {
      console.log(this.value)
      return (
        <Host>
          <slot></slot>
          <div class="editable-box">
            {this.showEdit ? (
              <span>
                {this.selectOptions != null ? (
                  <select name="s" onInput={event => this.handleSelect(event, this.multiSelect)} multiple={this.multiSelect}>
                    {this.selectOptions && this.selectOptions.length > 0 && this.selectOptions.map(option => (
                      <option value={option.value} selected={this.value != null ?
                        (typeof this.value=="string"?(this.value==option.value):(this.value.filter(obj => obj.value===option.value).length > 0)) : (false)
                      }>{option.display}</option>
                    ))}
                  </select>
                ):(
                  <input type="text" value={this.value}  onInput={event => this.updateValue(event)} />
                )}
                <span>
                  <span class="save-icon edit-buttons" onClick={this.submitEdit}>
                    <ion-icon name="checkmark-outline"></ion-icon>
                  </span>
                  <span id="cancel-icon" class="cancel-icon edit-buttons" onClick={this.cancelEditClicked}>
                    <ion-icon name="close-outline"></ion-icon>
                  </span>
                </span>
              </span>
            ):(
              <span>
                {this.value != null ? (
                  <span>
                    {typeof this.value === 'boolean' && <span>{this.value.toString()}</span>}
                    {typeof this.value === 'number' && <span>{this.value.toString()}</span>}
                    {typeof this.value === 'object' && this.value.constructor.name == "Array" && <span>
                      {this.value.map(obj => 
                        <span> {obj.value} </span>
                      )}  
                    </span>}
                    {typeof this.value === 'string' && <span title={this.value}>{this.value}</span>}
                  </span>
                ):(
                  <span></span>
                )}
                
              </span>
            )}
            {this.enableEdit && !this.showEdit && (
              <span class="edit-buttons" onClick={this.showEditClicked}>
                <ion-icon name="create-outline"></ion-icon>
              </span>
            )}
          </div>
        </Host>
      );
    }
  }