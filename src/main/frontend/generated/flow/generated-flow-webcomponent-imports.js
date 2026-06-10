import { injectGlobalWebcomponentCss } from 'Frontend/generated/jar-resources/theme-util.js';

import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/scroller/src/vaadin-scroller.js';
import '@vaadin/side-nav/src/vaadin-side-nav.js';
import '@vaadin/side-nav/src/vaadin-side-nav-item.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '52f63feaf4ff3b5ba2d4197d4b32e54dea44460df95ec80e1cf33418f5028d7d') {
    pending.push(import('./chunks/chunk-f9479477c046402a11d46a73c3cb9c628df6f7dec18cf83012b833b08fcdcf0e.js'));
  }
  if (key === 'cfae4d109953bc8f0c80ddc3f5e1a56f02dc873d97822c4ab954618fd6192f9d') {
    pending.push(import('./chunks/chunk-2ab1b3952b55dd5a3459fb72e13d216a9b38c24c0609d2e5035ee575e4f53cef.js'));
  }
  return Promise.all(pending);
}
window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}