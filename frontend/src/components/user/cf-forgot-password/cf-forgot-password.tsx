import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

class ForgotPasswordRequest {
  email: string;
}

class ForgotPasswordResponse extends GenericResponse {
  token: string;
  readableTables: string[];
  isAdmin: string;
  userId: number;
}

@Component({
  tag: 'cf-forgot-password',
  styleUrl: 'cf-forgot-password.css',
  shadow: true,
})
export class CfForgotPassword {
  @Prop() history: RouterHistory;

  @State() email: string;
  @State() password: string;

  @State() message: string;

  emailChange(event) {
    this.email = event.target.value;
  }

  clickSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<ForgotPasswordRequest, ForgotPasswordResponse>('user/forgot-password', { email: this.email });

    console.log('Result: ', result);

    if (result.error === ErrorType.NoError) {
        this.message = "Reset Password link sent to your email"
    } else {
        this.message = "Email does not exists"
        console.error('Login failed');
    }
  };

  testButtonClick = () => {
      this.message = "New test after button click"
      console.log(this.message)
  }


  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Reset Password</h1>
        <div class={this.message!=""?'show':'hide'}>{ this.message }</div>
        <form>
          <div id="email-holder" class="form-input-item">
            <div>
              <label htmlFor="email">Email Address</label>
            </div>
            <input type="text" id="email" name="email" onInput={event => this.emailChange(event)} />
          </div>

          <button id="Login-button" type="button" onClick={this.clickSubmit} >Reset Password</button>
          {/* <button type="button" onClick={this.testButtonClick}>Test Button</button> */}
        </form>
        

        
      </Host>
    );
  }
}
injectHistory(CfForgotPassword);