import { Component, Host, h, State, Prop } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';

class RegisterRequest {
  email: string;
  password: string;
}

class RegisterResponse {
  error: ErrorType;
  token: string;
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
    console.log(this.email, this.password);
    const result = await fetchAs<RegisterRequest, RegisterResponse>('user/register', { email: this.email, password: this.password });

    console.log(result.error);
    console.log(ErrorType.NoError);

    if (result.error === ErrorType.NoError) {
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
