<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Environment Monitor</title>
<link
	href="${pageContext.request.contextPath}/static/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/static/css/bootstrap-theme.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/jquery-2.2.1.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/js/script.js"></script>
<style>
.modal-body {
	max-height: calc(100vh - 210px);
	overflow-y: auto;
}
</style>
</head>
<body>
	<div id="eservices-container" class="container">
		<nav class="navbar navbar-default navbar-static-top  navbar-inverse">
		<div class="container">
			<div class="navbar-header">
				<h3 class="navbar-text navbar-left">Documentum Services Monitor</h3>
				<button type="button" class="btn btn-default navbar-btn" data-dismiss="modal"
						data-toggle="modal" data-target="#new-eservice-dialog">Add New Service</button>
			</div>
		</div>
		</nav>
		<div id="eservices-panel" class="panel panel-default">

			<table id="eservices-list" class="table table-bordered table-curved">
				<thead>
					<tr>
						<th>ID</th>
						<th>Name</th>
						<th>Version</th>
						<th>Status</th>
						<th>Last Update</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>

		</div>
	</div>

	<div id="view-eservice-dialog" title="View Service" class="modal"
		role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body">
					<table id="eservice-details" class="table table-bordered">
						<tr>
							<td>Service Name:</td>
							<td id="eservice-name"></td>
						</tr>
						<tr>
							<td>Service Docbase:</td>
							<td id="eservice-docbase"></td>
						</tr>
						<tr>
							<td>Service Status:</td>
							<td id="eservice-status"></td>
						</tr>
						<tr>
							<td>Service Version:</td>
							<td id="eservice-version"></td>
						</tr>
						<tr>
							<td>Service Host:</td>
							<td id="eservice-host"></td>
						</tr>
						<tr>
							<td>Service Port:</td>
							<td id="eservice-port"></td>
						</tr>
						<tr>
							<td>Service User:</td>
							<td id="eservice-user"></td>
						</tr>
						<tr>
							<td>Service Email:</td>
							<td id="eservice-email"></td>
						</tr>
						<tr>
							<td>Service Type:</td>
							<td id="eservice-type"></td>
						</tr>
						<tr>
							<td>Service Context:</td>
							<td id="eservice-context"></td>
						</tr>
					</table>
				</div>
				<div class="modal-footer">					
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<div id="new-eservice-dialog" title="New Service" class="modal"
		role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">New Service</h4>
				</div>
				<div class="modal-body">
					<div class="form-group col-sm-10">
						<form class="form-horizontal" name="newEServiceForm"
							id="new-eservice-dialog" method="POST">
							<label for="eServiceType">Type</label> <select
								class="form-control" id="eServiceType" name="serviceType"><option>Test</option></select> <label
								for="eServiceName">Name* (for Content Server name is
								dm_server_config object_name, for xCP application name is
								application name) </label> <input type="text" id="eServiceName"
								class="form-control input-sm" name="name"> <label
								for="eServiceDocbase">Docbase Name</label> <input type="text"
								id="eServiceDocbase" class="form-control input-sm" name="docbase"> <label
								for="eServiceHost">Service Host</label> <input type="text"
								id="eServiceHost" class="form-control input-sm" name="host"> <label
								for="eServicePort">Service Port</label> <input type="number"
								id="eServicePort" class="form-control input-sm" name="port"> <label
								for="eServiceUser">User</label> <input type="text"
								id="eServiceUser" class="form-control input-sm" name="user"> <label
								for="eServicePwd">Password</label> <input type="password"
								id="eServicePwd" class="form-control input-sm" name="password"> <label
								for="eServiceEmail">E-mail (notifications will be sent
								to this address)</label> <input type="email" id="eServiceEmail"
								class="form-control input-sm" name="email">
						</form>
					</div>
				</div>
				<div class="modal-footer">
				<input type="submit" class="btn btn-success" id="add">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>