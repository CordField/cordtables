import { Component, Host, h, Element, Prop, Event, EventEmitter, Listen, State } from '@stencil/core';
import { TinyUpdateEvent } from './types';

@Component({
  tag: 'cf-tiny',
  styleUrl: 'cf-tiny.css',
  shadow: true,
})
export class CfTiny {
  @Element() el: HTMLElement;

  @Prop() uid: string;
  @Prop({ mutable: true }) initialHTMLContent: string = null;

  @Event() contentUpdate: EventEmitter<TinyUpdateEvent>;
  @State() content: string;
  // we listen to a global event as the event is emitted on the parent element. 
  @Listen('contentSubmitted', {
    target: 'window',
  })
  handleContentSubmitted(e: CustomEvent<string>) {
    console.log('handleContentSubmitted', e.detail, this.uid);
    if (e.detail === this.uid) {
      this.content = '';
      this.el.shadowRoot.getElementById('tiny-ed')!!.shadowRoot.childNodes[1].childNodes[0].childNodes[1].childNodes[0].firstChild[
        'contentDocument'
      ].documentElement.childNodes[1].innerHTML = '';
    }
  }
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
          plugins="image link emoticons image table media autoresize"
          menubar="false"
          autoresize_bottom_margin={0}
          toolbar_mode="floating"
          toolbar="quicklink emoticons image table media | bold italic | undo redo | styleselect | alignleft aligncenter alignright alignjustify | outdent indent"
          quickbars_insert_toolbar="false"
        >
          {this.initialHTMLContent}
        </tinymce-editor>
      </Host>
    );
  }
}
