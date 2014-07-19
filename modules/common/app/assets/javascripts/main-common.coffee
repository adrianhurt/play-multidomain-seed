require.config {
	paths: {
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

require ["common/utils", "common/moreUtils"]