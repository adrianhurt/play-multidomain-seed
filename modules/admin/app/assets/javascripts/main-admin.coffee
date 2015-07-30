require.config
	paths:
		common: "../lib/common/javascripts"
		jquery: "../lib/jquery/jquery"
		bootstrap: "../lib/bootstrap/js/bootstrap"
	
	shim:
		jquery:
			exports: "$"
		bootstrap:
			deps: ["jquery"]


require ["admin"]