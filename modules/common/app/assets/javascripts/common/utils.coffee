define ["jquery", "bootstrap"], ($, bootstrap) ->

	showModal: ($modal) -> $modal.modal 'show'
	hideElement: ($elem) -> $elem.hide()