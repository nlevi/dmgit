<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<div class="panel panel-default">
	<div class="panel-heading">
		<span class="lead">New Service</span>
	</div>
	<div class="formcontainer">
		<form ng-submit="ctrl.submit()" name="newServiceForm"
			class="form-horizontal">
			<input type="hidden" ng-model="ctrl.eservice.id" />
			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceName">Service
						Name</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.name" id="serviceName"
							class="form-control input-sm" placeholder="Enter service name"
							required />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.serviceName.$error.required">Please
								enter Service name</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceContext">Service
						Context</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.context"
							id="serviceContext" class="form-control input-sm"
							placeholder="Enter service context" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceType">Service
						Type</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.type" id="serviceType"
							class="form-control input-sm" placeholder="Enter service type"
							required />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.serviceType.$error.required">Please
								enter Service type</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceDocbase">Service
						Docbase Name</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.docbase"
							id="serviceDocbae" class="form-control input-sm"
							placeholder="Enter service docbase name" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceHost">Service
						Host</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.host" id="serviceHost"
							class="form-control input-sm"
							placeholder="Enter service host name or IP address" required />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.serviceHost.$error.required">Please
								enter Service host name or IP address</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="servicePort">Service
						Port</label>
					<div class="col-md-7">
						<input type="number" ng-model="ctrl.eservice.port"
							id="serviceContext" class="form-control input-sm"
							placeholder="Enter service context" required />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.servicePort.$error.required">Please
								enter Service port</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceUser">Service
						Username</label>
					<div class="col-md-7">
						<input type="text" ng-model="ctrl.eservice.user" id="serviceUser"
							class="form-control input-sm"
							placeholder="Enter service administrator account username" />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.serviceUser.$error.required">Please
								enter username</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="servicePassword">Admin
						User Password</label>
					<div class="col-md-7">
						<input type="password" ng-model="ctrl.eservice.password"
							id="servicePassword" class="form-control input-sm"
							placeholder="Enter service administrator user password" />
						<div class="has-error" ng-show="newServiceForm.$dirty">
							<span ng_show="newServiceForm.servicePassword.$error.required">Please
								enter password</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-12">
					<label class="col-md-2 control-label" for="serviceAddress">Admin
						USer E-mail</label>
					<div class="col-md-7">
						<input type="email" ng-model="ctrl.eservice.address"
							id="serviceAddress" class="form-control input-sm"
							placeholder="Enter service context" />
					</div>
				</div>
			</div>

			<div class="row">
				<div class="form-actions col-sm-10">
					<input type="submit" value="Add" class="btn btn-primary btn-sm"
						ng-disabled="newServiceForm.$invalid">
				</div>
			</div>

		</form>
	</div>
</div>
