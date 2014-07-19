define ["common/utils"], (utils) ->

	showModalAndDismiss: ($modal) ->
		utils.showModal $modal
		setTimeout(
			( -> utils.hideModal $modal),
			3000
		)