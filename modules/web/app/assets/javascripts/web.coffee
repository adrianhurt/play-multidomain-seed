define ["jquery", "bootstrap", "common/moreUtils"], ($, bootstrap, moreUtils) ->

	############################################################################################################
	## DOCUMENT IS READY - INIT APP
	############################################################################################################
	$ ->
		$('#btn-web-modal').click (e) ->
			moreUtils.showModalAndDismiss $('#myWebModal')