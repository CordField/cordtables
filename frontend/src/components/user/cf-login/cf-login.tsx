import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

class LoginRequest {
  email: string;
  password: string;
}

class LoginResponse extends GenericResponse {
  token: string;
  readableTables: string[];
  isAdmin: string;
  userId: number;
}

@Component({
  tag: 'cf-login',
  styleUrl: 'cf-login.css',
  shadow: true,
})
export class CfLogin {
  @Prop() history: RouterHistory;

  @State() email: string;
  @State() password: string;

  emailChange(event) {
    this.email = event.target.value;
  }

  passwordChange(event) {
    this.password = event.target.value;
  }

  clickSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();

    const result = await fetchAs<LoginRequest, LoginResponse>('user/login', { email: this.email, password: this.password });

    console.log('Result: ', result);

    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.token = result.token;
      globals.globalStore.state.email = this.email;
      globals.globalStore.state.isLoggedIn = true;
      globals.globalStore.state.readableTables = result.readableTables;
      globals.globalStore.state.isAdmin = result.isAdmin;
      globals.globalStore.state.userId = result.userId;

      this.history.push('/');
    } else {
      console.error('Login failed');
    }
  };

  clickedResetPassword = () => {
    this.history.push(`/forgot-password`);
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Login</h1>

        <form>
          <div id="email-holder" class="form-input-item">
            <div>
              <label htmlFor="email">Email Address</label>
            </div>
            <input type="text" id="email" name="email" onInput={event => this.emailChange(event)} />
          </div>

          <div id="password-holder" class="form-input-item">
            <div>
              <label htmlFor="password">Password</label>
            </div>
            <input type="password" id="password" name="password" onInput={event => this.passwordChange(event)} />
          </div>
          <input id="Login-button" type="submit" value="Login" onClick={this.clickSubmit} />
        </form>
        
        <a href="javascript:void(0)" onClick={this.clickedResetPassword}>Reset Password</a>
        
      </Host>
    );
  }
}
injectHistory(CfLogin);