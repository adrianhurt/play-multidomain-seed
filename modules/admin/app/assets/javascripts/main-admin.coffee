require.config {
	paths: {
		common: "../lib/common/javascripts/common"
		jquery: "../lib/jquery/jquery"
		bootstrap: "../lib/bootstrap/js/bootstrap"
	}
	shim: {
		bootstrap: {
			deps: ["jquery"]
		}
		jquery: {
			exports: "$"
		}
	}
}

require ["admin"]