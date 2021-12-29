import { Component, State, EventEmitter, Event, Prop, h } from '@stencil/core';

@Component({
  tag: 'custom-accordion',
  styleUrl: 'custom-accordion.scss',
  shadow: true
})

export class CustomAccordion {

  @State() toggle: boolean = false;

  @Event() onToggle: EventEmitter;

  @Prop() label: string;

  @Prop() marginLeft: string;

  @Prop() width: string;

  @Prop() color: string;

  toggleComponent() {
    this.toggle = !this.toggle;
    this.onToggle.emit({ visible: this.toggle });
  }

  render() {
    return (
      <div>
      <button class="accordion"
      style={{
        width: this.width,
        backgroundColor: this.color,
      }}
      onClick={() => this.toggleComponent()}>
      <span style={{color:"white", marginLeft: this.marginLeft}}>{this.label}</span>
      {this.toggle ? <span style={{color:"white"}}>&#9650;</span> : <span style={{color:"white"}}>&#9660;</span>}
      </button>
      <div class={`content-box ${this.toggle ? 'open' : 'close'}`}
      style={{width: this.width}}>
        <div>
          <slot />
        </div>
      </div>
      </div>
    )
  }
}