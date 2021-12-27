import { createStore } from '@stencil/store';
import { AppState } from '../common/types';
interface Notification {
  id: string;
  text: string;
  type: 'error' | 'success' | 'info';
}

const language = localStorage.getItem('language');

export class Globals {
  storeObject = {
    appState: AppState.Init,
    language: (language === null || language === undefined)?'default': language,
    editMode: false,
    editModeWidth: 0,
    isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
    token: localStorage.getItem('token'),
    email: localStorage.getItem('email'),
    readableTables: (JSON.parse(localStorage.getItem('readableTables')) ?? []) as string[],
    isAdmin: localStorage.getItem('isAdmin'),
    notifications: (JSON.parse(localStorage.getItem('notifications')) ?? []) as Notification[],
    userId: JSON.parse(localStorage.getItem('userId')) as number | undefined,
    siteTextLanguages: [],
    siteTextStrings: [],
    siteTextTranslations: {}
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
      if (newValue !== '' && newValue !== undefined && newValue !== null) {
        localStorage.setItem('isAdmin', 'true');
      } else {
        localStorage.setItem('isAdmin', 'false');
      }
    });
    this.globalStore.onChange('language', newValue => {
      if(newValue !== null) localStorage.setItem('language', newValue);
    });
  }
}

export const globals = new Globals();
