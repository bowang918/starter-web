/* <debug> */
Ext.Loader.setConfig({
      enabled: true,
      paths: {
          'App': 'app',
          'App.ux': 'app/ux',
          'Ext.ux' :'resources/ext/5.1.0/ext-ux'
      }
    });
/* </debug> */
Ext.application({
	name : 'App',
	extend : 'App.Application',
	autoCreateViewport : 'App.view.main.Main'
});
