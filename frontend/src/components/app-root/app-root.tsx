import { Component, h } from '@stencil/core';

@Component({
  tag: 'app-root',
  styleUrl: 'app-root.css',
  shadow: true,
})
export class AppRoot {
  render() {
    return (
      <div id="root-wrap-outer">
        <cf-header></cf-header>
        <div id="root-wrap-inner">
          <main>
            <stencil-router>
              <stencil-route-switch scrollTopOffset={0}>
                <stencil-route url="/" component="app-home" exact={true} />
                <stencil-route url="/profile" component="app-profile" />
                <stencil-route url="/register" component="cf-register" />
                <stencil-route url="/login" component="cf-login" />
              </stencil-route-switch>
            </stencil-router>
          </main>
        </div>
      </div>
    );
  }
}
