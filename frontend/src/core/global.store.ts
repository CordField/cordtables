import { createStore } from '@stencil/store';

export class Globals {
  public globalStore = createStore({
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
    token: localStorage.getItem('token'),
    email: localStorage.getItem('email'),
    readableTables: JSON.parse(localStorage.getItem('readableTables')),
    isAdmin: localStorage.getItem('isAdmin'),
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
        localStorage.removeItem('isAdmin');
        localStorage.removeItem('readableTables');
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

    this.globalStore.onChange('readableTables', newValue => {
      if (newValue != null) {
        localStorage.setItem('readableTables', JSON.stringify(newValue));
      } else {
        localStorage.removeItem('readableTables');
      }
    });

    this.globalStore.onChange('isAdmin', newValue => {
      if (newValue) {
        localStorage.setItem('isAdmin', 'true');
      } else {
        localStorage.setItem('isAdmin', 'false');
      }
    });
  }
}

export const globals = new Globals();
