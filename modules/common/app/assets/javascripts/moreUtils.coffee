define ["common/utils"], (utils) ->

	showModalAndDismiss: ($modal) ->
		$modal.find('.modal-body > p').text(utils.helloWorld())
		utils.showModal $modal
		setTimeout(
			( -> utils.hideModal $modal),
			3000
		)