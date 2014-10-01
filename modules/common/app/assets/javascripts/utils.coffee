define ["jquery", "bootstrap"], ($, bootstrap) ->

	# inner vars and functions
	_world = "World"
	_helloTo = (name) -> "Hello #{name}!"
	_helloWorld = -> _helloTo _world

	# every here will be "packed" into the returned object with these attributes visible from outside.
	helloTo: _helloTo
	helloWorld: _helloWorld
	showModal: ($modal) -> $modal.modal 'show'
	hideModal: ($modal) -> $modal.modal 'hide'