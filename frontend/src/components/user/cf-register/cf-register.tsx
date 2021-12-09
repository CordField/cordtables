import { Component, Host, h, State, Prop } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';

class RegisterRequest {
  email: string;
  password: string;
}

class RegisterResponse extends GenericResponse {
  token: string;
  readableTables: string[];
  isAdmin: string;
  userId: number;
}

@Component({
  tag: 'cf-register',
  styleUrl: 'cf-register.css',
  shadow: true,
})
export class CfRegister {
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

    const result = await fetchAs<RegisterRequest, RegisterResponse>('user/register', { email: this.email, password: this.password });

    console.log(result);

    if (result.error === ErrorType.NoError) {
      globals.globalStore.state.token = result.token;
      globals.globalStore.state.email = this.email;
      globals.globalStore.state.isLoggedIn = true;
      globals.globalStore.state.readableTables = result.readableTables;
      globals.globalStore.state.isAdmin = result.isAdmin;
      globals.globalStore.state.userId = result.userId;

      this.history.push('/');
    } else {
      console.error('Register failed');
    }
  };

  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Register</h1>

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
          <input id="register-button" type="submit" value="Register" onClick={this.clickSubmit} />
        </form>
      </Host>
    );
  }
}

injectHistory(CfRegister);
