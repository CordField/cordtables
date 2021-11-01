import { Component, Host, h, Element, Prop, Event, EventEmitter } from '@stencil/core';
import { globals } from '../../core/global.store';

@Component({
  tag: 'cf-notif',
  styleUrl: 'cf-notif.css',
  shadow: true,
})
export class Notif {
  @Element() el: HTMLElement;

  componentWillLoad() {
    // setInterval(() => {
    //   this.onChangeHandler();
    // }, 100);
  }

  //   onChangeHandler = () => {
  //     try {
  //       const text =
  //         this.el.shadowRoot.getElementById('tiny-ed')!!.shadowRoot.childNodes[1].childNodes[0].childNodes[1].childNodes[0].firstChild['contentDocument'].documentElement
  //           .childNodes[1].innerHTML;

  //       if (this.content !== text) {
  //         this.content = text;
  //         this.contentUpdate.emit({
  //           id: this.uid,
  //           content: this.content,
  //         });
  //         console.log(this.content);
  //       }
  //     } catch (e) {}
  //   };
  clickHandler = id => {
    globals.globalStore.state.notifications = globals.globalStore.state.notifications.filter(notif => notif.id !== id).reverse();
  };
  render() {
    return (
      <Host>
        {globals.globalStore.state.notifications.reverse().map(notif => {
          return (
            <div key={notif.id} class={notif.type}>
              <span>{notif.text}</span>
              <span
                class="close-button"
                onClick={() => {
                  this.clickHandler(notif.id);
                }}
              >
                &#10006;
              </span>
            </div>
          );
        })}
      </Host>
    );
  }
}
