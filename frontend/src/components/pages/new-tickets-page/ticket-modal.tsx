import { Component, h, Prop, Event, EventEmitter } from '@stencil/core';


@Component({
  tag: 'ticket-modal',
  styleUrl: 'ticket-modal.css',
  shadow: true,
})

export class TicketModal{
@Prop() modalTitle: String;
@Prop() isOpen: boolean = false;
@Prop() type: String = 'Create';

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
      console.log('isOpen: ', this.isOpen);
    return (
        <div class={this.isOpen ? 'modal-wrapper': 'modal-wrapper-none'}>
            <div class="modal-overlay" />
            <div class="modal">
                <div class="header">
                    <h4>{this.modalTitle}</h4>
                    <div class="close">
                        <span class="closeButton" onClick={this.handleClose}> X </span>
                    </div>
                </div>
                <div class="content">
                    <slot />
                </div>
                <div class="footer">
                  <button class="ok" onClick={this.handleOkay}>{(this.type === 'Create')? 'Create' : 'Update'}</button>
                </div>
            </div>
        </div>
    );
  }
}
