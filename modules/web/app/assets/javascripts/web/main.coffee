require.config {
	paths: {
		common: "../common"
		jquery: "../../lib/jquery/jquery"
		bootstrap: "../../lib/bootstrap/js/bootstrap"
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

require ["app"]