import { createStore } from '@stencil/store';
interface Notification {
  id: string;
  text: string;
  type: 'error' | 'success' | 'info';
}

export class Globals {
  storeObject = {
    editMode: false,
    editModeWidth: 0,
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
    token: localStorage.getItem('token'),
    email: localStorage.getItem('email'),
    readableTables: (JSON.parse(localStorage.getItem('readableTables')) ?? []) as string[],
    isAdmin: localStorage.getItem('isAdmin'),
    notifications: (JSON.parse(localStorage.getItem('notifications')) ?? []) as Notification[],
    userId: JSON.parse(localStorage.getItem('userId')) as number | undefined,
  };
  public globalStore = createStore(this.storeObject);

  constructor() {
    this.globalStore.onChange('editMode', newValue => {
      if (newValue === true) {
        this.globalStore.state.editModeWidth = 50;
      } else {
        this.globalStore.state.editModeWidth = 0;
      }
    });
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
    this.globalStore.onChange('userId', newValue => {
      if (newValue != null) {
        localStorage.setItem('userId', JSON.stringify(newValue));
      } else {
        localStorage.removeItem('userId');
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

    this.globalStore.onChange('notifications', newValue => {
      if (newValue != null) {
        localStorage.setItem('notifications', JSON.stringify(newValue));
      } else {
        localStorage.removeItem('notifications');
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
