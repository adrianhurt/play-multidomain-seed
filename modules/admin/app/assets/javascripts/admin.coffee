define ["jquery", "bootstrap", "admin/otherLib"], ($, bootstrap, otherLib) ->

	############################################################################################################
	## DOCUMENT IS READY - INIT APP
	############################################################################################################
	$ ->
		$('#btn-admin-modal').click (e) ->
			otherLib.adminShowModalAndDismiss $('#myAdminModal')