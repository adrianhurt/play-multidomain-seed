define ["jquery", "bootstrap"], ($, bootstrap) ->

	showModal: ($modal) -> $modal.modal 'show'
	hideModal: ($modal) -> $modal.modal 'hide'