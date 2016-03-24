$(function() {
	var eservices = [];

	$.getJSON('/EnvironmentMonitor/service/', function(data) {
		eservices = data;
		generateEServicesTableHTML(eservices);
		// $(window).trigger('hashchange');

	});

	$(window).on('hashchange', function() {
		render(decodeURI(window.location.hash));
	});

	function generateEServicesTableHTML(data) {
		var listServicesTable = $('#eservices-list > tbody');
		var lDate = new Date();
		data
				.forEach(function(item) {
					lDate.setTime(item.lastUpdate);
					listServicesTable
							.append("<tr data-index="
									+ item.id
									+ " class="
									+ isRunning(item.status)
									+ " data-toggle='modal' data-target='#view-eservice-dialog'>"
									+ "<td>" + item.id + "</td>" + "<td>"
									+ item.name + "</td>" + "<td>"
									+ item.version + "</td>" + "<td>"
									+ item.status + "</td>" + "<td>"
									+ lDate.toLocaleString() + "</td></tr>");

				});
		listServicesTable.find('tr').on('click', function(e) {
			e.preventDefault();
			var eServiceId = $(this).data('index');
			// window.location.hash = 'service/' + eServiceId;
			console.log("Open view dialog" + eServiceId);
			// viewEService(eServiceId);
		})
	}
	;

	function renderSingleServicePage(index, data) {
		var detailsContainer = $('#eservice-details');
		if (data.length) {
			data
					.forEach(function(item) {
						var lDate = new Date(item.lastUpdate)
						if (item.id == index) {
							detailsContainer.find('#eservice-id').text(item.id);
							detailsContainer.find('#eservice-name').text(
									item.name);
							detailsContainer.find('#eservice-version').text(
									item.version);
							detailsContainer.find('#eservice-status').text(
									item.status);
							detailsContainer.find('#eservice-lastupdate').text(
									lDate.toLocaleString());
							detailsContainer.find('#eservice-docbase').text(
									item.docbase);
							detailsContainer.find('#eservice-version').text(
									item.version);
							detailsContainer.find('#eservice-host').text(
									item.host);
							detailsContainer.find('#eservice-port').text(
									item.port);
							detailsContainer.find('#eservice-user').text(
									item.user);
							detailsContainer.find('#eservice-email').text(
									item.address);
							detailsContainer.find('#eservice-type').text(
									item.serviceType);
							detailsContainer.find('#eservice-context').text(
									item.context);
						}
					});
		}
		;

		// page.addClass('visible');
	}
	;

	function showError() {
		var page = $('.error');
		page.addClass('visible');
	}
	;

	function refreshHash() {
		window.location.hash = '#';
	}
	;

	// Bootstrap modals

	$('#view-eservice-dialog').modal({
		keyboard : false,
		backdrop : "static",
		show : false,
	}).on('show.bs.modal', function(e) {
		var rowIndex = $(e.relatedTarget).data('index');
		$(this).find('.modal-title').text('Service ID #' + rowIndex);
		renderSingleServicePage(rowIndex, eservices);
	});
	
	$('form#add').click(function(e) {
		e.preventDefault();
		var frm = $('form#new-eservice-dialog');
		var data = formToJSON(frm);
		var j = frm.serializeFormJSON();
		var s = frm.serialize();
		console.log(data);
		console.log(j);
		console.log(JSON.stringify(j));
		$.post('/EnvironmentMonitor/service/', s, function(response) {
			console.log(response);			
		}, 'json');
		
//		$.ajax({
//		    url: '/EnvironmentMonitor/service/',
//		    type: 'POST',
//		    data: JSON.stringify(j),
//		    contentType: 'application/json; charset=utf-8',
//		    dataType: 'json',
//		    async: false,
//		    success: function(msg) {
//		        alert(msg);
//		    }
//		});
	});
	
	$('.modal-body').css({
		'max-height': '100%',
		'overflow-y': 'auto'
	});

	// Jquery-UI modals
	/*
	 * function viewEService(index) { var viewEServiceDialog; console.log("This
	 * is dialog"); renderSingleServicePage(index, eservices);
	 * viewEServiceDialog = $('#view-eservice-dialog').dialog({ autoOpen :
	 * false, height : 520, width : 600, modal : true, buttons : [ { text :
	 * "Edit", click : function() { console.log("Edit button clicked.");
	 * addEService(); viewEServiceDialog.dialog("close"); } }, { text : "Close",
	 * click : function() { viewEServiceDialog.dialog("close"); //refreshHash(); } } ]
	 * });
	 * 
	 * viewEServiceDialog.dialog("open"); };
	 * 
	 * function addEService() { var addEServiceDialog; console.log("This is add
	 * dialog"); //renderAddEServicePage(index, eservices); addEServiceDialog =
	 * $('#add-eservice-dialog').dialog({ autoOpen : false, height : 520, width :
	 * 600, modal : true, buttons : [ { text : "Add", click : function() {
	 * console.log("Add button clicked."); } }, { text : "Cancel", click :
	 * function() { addEServiceDialog.dialog("close"); refreshHash(); } } ] });
	 * 
	 * addEServiceDialog.dialog("open"); };
	 */
	function formToJSON(form) {
		var array = form.serializeArray();
		var json = {};
		$.each(array, function () {
            if (json[this.name]) {
                if (!json[this.name].push) {
                    json[this.name] = [json[this.name]];
                }
                json[this.name].push(this.value || '');
            } else {
                json[this.name] = this.value || '';
            }
        });
        return json;
	};
		
	function isRunning(status) {
		return status == 'Running' ? 'success' : 'danger';
	};
});

(function($) {
	$.fn.serializeFormJSON = function() {

	   var o = {};
	   var a = this.serializeArray();
	   $.each(a, function() {
	       if (o[this.name]) {
	           if (!o[this.name].push) {
	               o[this.name] = [o[this.name]];
	           }
	           o[this.name].push(this.value || '');
	       } else {
	           o[this.name] = this.value || '';
	       }
	   });
	   return o;
	};
	})(jQuery);