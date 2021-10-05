import { Component, h, Prop, Event, EventEmitter } from '@stencil/core';


@Component({
  tag: 'create-update-modal',
  styleUrl: 'create-update-modal.css',
  shadow: true,
})




export class GlobalRoleColumnGrants {
@Prop() modalTitle: String;
@Prop() isOpen: boolean = false;

@Event({
    eventName: 'modalClosed',
    bubbles: true,
  }) modalClosed : EventEmitter<boolean>;

@Event({
  eventName: 'modalOkay',
  bubbles: true,
}) modalOkay : EventEmitter<boolean>;

@Event({
  eventName: 'modalDelete',
  bubbles: true,
}) modalDelete : EventEmitter<boolean>;


private handleClose = () => {
    this.isOpen = !this.isOpen;
    this.modalClosed.emit(!this.isOpen);
}

private handleOkay = () => {
  this.isOpen = !this.isOpen;
  this.modalOkay.emit(!this.isOpen);
}

private handleDelete = () => {
  this.isOpen = !this.isOpen;
  this.modalDelete.emit(!this.isOpen);
}

  render() {
    return (
        <div class={this.isOpen ? 'modal-wrapper': 'modal-wrapper-none'}>
            <div class="modal-overlay" />
            <div class="modal">
                <div class="header">
                    <h6>Modal Header</h6>
                    <div class="close">
                        <span class="closeButton" onClick={this.handleClose}> X </span>
                    </div>
                </div>
                <div class="content">
                    <slot />
                </div>
                <div class="footer">
                  <button class="delete" onClick={this.handleDelete}>Delete Record</button>
                  <button class="ok" onClick={this.handleOkay}>Save Record</button>
                </div>
            </div>
        </div>
    );
  }
}
