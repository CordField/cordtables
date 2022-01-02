import { Component, Host, h, Prop, State } from '@stencil/core';
import { injectHistory, RouterHistory } from '@stencil/router';
import { ErrorType, GenericResponse } from '../../../common/types';
import { fetchAs } from '../../../common/utility';
import { globals } from '../../../core/global.store';
import { MatchResults } from '@stencil/router';

class ResetPasswordRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

class ResetPasswordResponse extends GenericResponse {
  token: string;
  readableTables: string[];
  isAdmin: string;
  userId: string;
}

class ValidateTokenRequest {
  token: string;
}

class ValidateTokenResponse{
  error: string;
  valid: boolean;
}

@Component({
  tag: 'cf-reset-password',
  styleUrl: 'cf-reset-password.css',
  shadow: true,
})
export class CfResetPassword {
  @Prop() history: RouterHistory;
  @Prop() match: MatchResults;

  @State() email: string;
  @State() isValid: boolean = false;
  @State() resetSuccessMessage: string = "";
  @State() resetErrorMessage: string = "";
  
  password: string;
  confirmPassword: string;

  cnfPasswordChange(event) {
    this.confirmPassword = event.target.value;
  }

  passwordChange(event) {
    this.password = event.target.value;
  }

  clickSubmit = async (event: MouseEvent) => {
    event.preventDefault();
    event.stopPropagation();
    const result = await fetchAs<ResetPasswordRequest, ResetPasswordResponse>('user/reset-password', {
      token: this.match.params.token,
      newPassword: this.password,
      confirmPassword: this.confirmPassword  
    });
    console.log('Result: ', result);
    if (result.error === ErrorType.NoError) {
      this.resetSuccessMessage = "Reset password success, You can login using new password"
      globals.globalStore.state.token = result.token;
      globals.globalStore.state.email = this.email;
      globals.globalStore.state.isLoggedIn = true;
      globals.globalStore.state.readableTables = result.readableTables;
      globals.globalStore.state.isAdmin = result.isAdmin;
      globals.globalStore.state.userId = result.userId;

      this.history.push('/');
    } else {
      this.resetErrorMessage = "Reset password failed"
      console.error('Login failed');
    }
  };

  clickedResetPassword = () => {
    this.history.push(`/reset-password`);
  }

  async validateToken(){
    const result = await fetchAs<ValidateTokenRequest, ValidateTokenResponse>('user/validate-token', { token: this.match.params.token});
    if(result.error == ErrorType.NoError && result.valid){
      this.isValid = true      
    }
  }

  async componentWillLoad() {
    await this.validateToken();
    // await this.getFilesList();
    console.log(this.match.params.token)
  }

  render() {
    return (
      <Host>
        <slot></slot>
        <h1>Reset Password</h1>
        {this.resetErrorMessage !="" && 
          <div class="error-message">{ this.resetErrorMessage }</div>
        }
        {this.resetSuccessMessage != "" &&
          <div class="success-message">{ this.resetSuccessMessage }</div>
        }

        {this.resetSuccessMessage == "" && this.resetErrorMessage == "" && 
          <div> {this.isValid ? 
            <form>
              <div id="email-holder" class="form-input-item">
                <div>
                  <label htmlFor="email">New Password</label>
                </div>
                <input type="password" id="new-password" name="password" onInput={event => this.passwordChange(event)} />
              </div>

              <div id="email-holder" class="form-input-item">
                <div>
                  <label htmlFor="email">Confirm New Password</label>
                </div>
                <input type="password" id="confirm-new-password" name="cnf_password" onInput={event => this.cnfPasswordChange(event)} />
              </div>

              <input id="Login-button" type="submit" value="Reset Password" onClick={this.clickSubmit} />
            </form>
            : <div>Reset Password Link Invalid/Expired </div>
          }</div>
        }
      </Host>
    );
  }
}
injectHistory(CfResetPassword);