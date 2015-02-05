Ext.define('App.view.main.MainController', {
	extend : 'Ext.app.ViewController',
	routes : {
		':navigationId' : {
			action : 'onNavigationRoute',
			conditions : {
	            ':navigationId' : '([0-9]+)'
	        }
		},
	},
	
    onNavigationRoute : function(navigationId) {
    	if(!this.ignoreHashChange){
	    	var navigationTree = this.lookupReference('appNavigationTree');
	    	var node = navigationTree.getStore().getNodeById( navigationId );
			if (node && node.data && node.data.view) {
				var view = node.data.view;
				var tabPanel = this.lookupReference('appMainTabpanel'), treePath = node.getPath(), card = tabPanel.child('panel[treePath=' + treePath + ']');
				if (!card) {
					card = Ext.create(view, {
	    				navigationId : node.data.id,
						treePath : treePath,
						title : node.data.text,
						closable : true,
						icon : node.data.icon
					});
					tabPanel.add(card);
				}
				tabPanel.setActiveTab(card);
			}
    	}
    	this.ignoreHashChange = false;
    },
	onNavigationTreeSelectionchange : function(tree, selected, eOpts) {
		if(selected && selected.length === 1){
			this.redirectTo(selected[0].data.id);
		}
	},
	
	onMainTabPanelTabChange : function( tabPanel, newCard, oldCard, eOpts ){
		if(!oldCard||newCard.treePath!=oldCard.treePath){
			var navigationTree = this.lookupReference('appNavigationTree');
			navigationTree.suspendEvents();
			navigationTree.selectPath(newCard.treePath);
			navigationTree.resumeEvents();
			
			this.ignoreHashChange = true;
			this.redirectTo(newCard.navigationId);
		}
	},
	onMainTabPanelRemove : function( tabPanel, component, eOpts ){
		var navigationTree = this.lookupReference('appNavigationTree');
		if (tabPanel.items.length === 0) {
			navigationTree.getSelectionModel().deselectAll();
		}
	},
	
	init: function() {
		securityController.getLoggedOnUser(this.afterLoggedOnUserReceived, this);
	},
	
	afterLoggedOnUserReceived: function(user) {
		this.getViewModel().set('loggedOnUser',user);
	},
	onNavigationStoreLoad: function(store, records, successful, operation, node, eOpts ){
		if (this.autoOpenView) {
			var node = store.findNode('view', this.autoOpenView);
			if (node) {
				var navigationTree = this.lookupReference('appNavigationTree');
				Ext.defer(function() {
					navigationTree.selectPath(node.getPath());
				}, 1);
			}
		}
	},
});
