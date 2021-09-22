import { Config } from '@stencil/core';

// https://stenciljs.com/docs/config

export const config: Config = {
  devServer: {
    reloadStrategy: 'hmr',
    port: 3333,
  },
  globalStyle: 'src/global/app.css',
  globalScript: 'src/global/app.ts',
  taskQueue: 'async',
  outputTargets: [
    {
      type: 'www',
      // comment the following line to disable service workers in production
      serviceWorker: null,
      baseUrl: 'http://localhost:3333/',
    },
  ],
};
