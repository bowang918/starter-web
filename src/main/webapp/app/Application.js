Ext.define('App.Application', {
	extend : 'Ext.app.Application',
	name : 'App',
	requires : [ 'App.ux.form.trigger.Clear', 'App.StringUtils','App.Util','App.ux.form.field.TreeComboBox' ],
	stores: ['Roles'],
	constructor : function() {
		var me = this;
		
		Ext.setGlyphFontFamily('custom');
		Ext.tip.QuickTipManager.init();
		
		Ext.direct.Manager.addProvider(Ext.app.REMOTING_API);

		me.setupGlobalErrorHandler();

		Ext.direct.Manager.on('event', function(e) {
			if (e.code && e.code === 'parse') {
				window.location.reload();
			}
		});

		Ext.direct.Manager.on('exception', function(e) {
			if (e.message === 'accessdenied') {
				App.Util.errorToast(i18n.accessdenied);
			} else {
				App.Util.errorToast(e.message);
			}
		});
		me.callParent(arguments);
	},
	/**
	 * A template method that is called when your application boots. It is
	 * called before the Application's launch function is executed so gives a
	 * hook point to run any code before your Viewport is created.
	 */
	// init : function(application) {
	//
	// },
	/**
	 * Called automatically when the page has completely loaded. This is an
	 * empty function that should be overridden by each application that needs
	 * to take action on page load.
	 */
	launch : function() {
		Ext.fly('circle').destroy();
	},
	/**
	 * A template method like init, but called after the viewport is created.
	 * This is called after the launch method of Application is executed.
	 */
	// onLaunch : function(application) {
	//
	// },
	setupGlobalErrorHandler : function() {
		var me = this, existingFn = window.onerror;
		if (typeof existingFn === 'function') {
			window.onerror = Ext.Function.createSequence(existingFn,
					me.globalErrorHandler);
		} else {
			window.onerror = me.globalErrorHandler;
		}
	},

	globalErrorHandler : function(message, file, line, col, error) {
		var trace = printStackTrace({
			e : error
		});
		var tracestring = '';

		if (trace) {
			tracestring = '\n' + trace.join('\n');
		}
		// TODO log the client's js error on backend
		// logService.error(message + '-->' + file + '::' + line + tracestring);
	}
});
