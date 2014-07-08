define ["jquery", "bootstrap", "common/utils"], ($, bootstrap, utils) ->

	############################################################################################################
	## DOCUMENT IS READY - INIT APP
	############################################################################################################
	$ ->
		$('#btn-admin-modal').click (e) ->
			utils.showModal $('#myAdminModal')