<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<div class="panel-heading">
	<span class="lead">Services</span>
</div>
<div class="tablecontainer">
	<table class="table table-hover table-bordered table-curved col-md-9">
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
			<tr ng-repeat="es in eservices" ng-class="isRunning(es)">
				<td>{{ es.id }}</td>
				<td>{{ es.name }}</td>
				<td>{{ es.version }}</td>
				<td>{{ es.status }}</td>
				<td>{{ es.lastUpdate | date: 'short' }}</td>
				<td>
					<button type="button" ng-click="remove(es.id)"
						class="btn btn-danger btn-sm">Remove</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>
