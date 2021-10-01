import { createStore } from '@stencil/store';

export class Globals {
  public globalStore = createStore({
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
    token: localStorage.getItem('token'),
    email: localStorage.getItem('email'),
    // globalRolesData: localStorage.getItem('globalRolesData'),
  });

  constructor() {
    this.globalStore.onChange('isLoggedIn', newValue => {
      if (newValue) {
        localStorage.setItem('isLoggedIn', 'true');
      } else {
        console.log('logging out');
        this.globalStore.state.email = null;
        localStorage.setItem('isLoggedIn', 'false');
        localStorage.removeItem('email');
      }
    });

    this.globalStore.onChange('email', newValue => {
      if (newValue != null) {
        localStorage.setItem('email', newValue);
      } else {
        localStorage.removeItem('email');
      }
    });

    this.globalStore.onChange('token', newValue => {
      if (newValue != null) {
        localStorage.setItem('token', newValue);
      } else {
        localStorage.removeItem('token');
      }
    });
    // this.globalStore.onChange('globalRolesData', newValue => {
    //   if (newValue != null) {
    //     localStorage.setItem('globalRolesData', newValue);
    //   } else {
    //     localStorage.removeItem('globalRolesData');
    //   }
    // });
  }
}

export const globals = new Globals();
