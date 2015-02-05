Ext.define('App.view.main.Main', {
	extend : 'Ext.container.Container',
	requires : [ 'Ext.ux.TabReorderer', 'Ext.ux.TabCloseMenu','App.view.main.Header', 'App.view.main.SideBar','App.view.main.MainController', 'App.view.main.MainModel' ],
	
	controller : {
		xclass : 'App.view.main.MainController'
	},
	
	viewModel : {
		xclass : 'App.view.main.MainModel'
	},
	
	layout: {
		type: 'border',
		padding: 3
	},
	
	items : [ {
		xtype : 'appHeader',
		region : 'north',
		split : false
	}, {
		region : 'west',
		xtype : 'appSideBar',
		reference : 'appNavigationTree',
		split : true
	}, {
		region : 'center',
		reference : 'appMainTabpanel',
		xtype : 'tabpanel',
		items : [],
		split : true,
		plugins : [ 'tabreorderer', {
			ptype : 'tabclosemenu',
			closeTabText : i18n.tabclosemenu_close,
			closeOthersTabsText : i18n.tabclosemenu_closeother,
			closeAllTabsText : i18n.tabclosemenu_closeall
		} ],
		plain : true,
		listeners : {
			tabchange : 'onMainTabPanelTabChange',
			remove : 'onMainTabPanelRemove'
		}
	} ]
});
