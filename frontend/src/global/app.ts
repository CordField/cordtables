import { siteTextService } from "../core/site-text.service";
export default async () => {
  /**
   * The code to be executed should be placed within a default function that is
   * exported by the global script. Ensure all of the code in the global script
   * is wrapped in the function() that is exported.
   */

  initApp();
};

const initApp = () => {
  console.info("initializing app");

  window.addEventListener('appload', (event: any) => {
    console.debug("Adding appload event", event.detail.namespace);
    if(event.detail.namespace === 'app') {
      siteTextService.load();
    }
  });
}