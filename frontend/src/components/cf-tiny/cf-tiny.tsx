import { Component, Host, h, Element, Prop, Event, EventEmitter } from '@stencil/core';
import { TinyUpdateEvent } from './types';

@Component({
  tag: 'cf-tiny',
  styleUrl: 'cf-tiny.css',
  shadow: true,
})
export class CfTiny {
  @Element() el: HTMLElement;

  @Prop() uid: number;

  @Event() contentUpdate: EventEmitter<TinyUpdateEvent>;
  content: string;

  componentWillLoad() {
    setInterval(() => {
      this.onChangeHandler();
    }, 100);
  }

  onChangeHandler = () => {
    try {
      const text =
        this.el.shadowRoot.getElementById('tiny-ed')!!.shadowRoot.childNodes[1].childNodes[0].childNodes[1].childNodes[0].firstChild['contentDocument'].documentElement
          .childNodes[1].innerHTML;

      if (this.content !== text) {
        this.content = text;
        this.contentUpdate.emit({
          id: this.uid,
          content: this.content,
        });
        console.log(this.content);
      }
    } catch (e) {}
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <tinymce-editor
          key={0}
          id="tiny-ed"
          api-key={process.env.TINY_KEY}
          plugins="image link emoticons image table media"
          menubar="false"
          toolbar_mode="floating"
          toolbar="quicklink emoticons image table media | bold italic | undo redo | styleselect | alignleft aligncenter alignright alignjustify | outdent indent"
          quickbars_insert_toolbar="false"
        ></tinymce-editor>
      </Host>
    );
  }
}
