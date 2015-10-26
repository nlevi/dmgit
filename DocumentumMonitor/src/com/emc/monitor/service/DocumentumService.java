package com.emc.monitor.service;

import java.sql.Timestamp;

public class DocumentumService {

	private String address;
	private String docbase;
	private String password;
	private String user;
	private String host;
	private int port;
	private String type;
	private String name;
	private String version;
	private String status;
	private int service_id;
	private Timestamp lastUpdate;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getAddress() {
		return address;
	}

	public String getDocbase() {
		return docbase;
	}

	public String getPassword() {
		return password;
	}

	public String getUser() {
		return user;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getStatus() {
		return status;
	}

	public int getId() {
		return service_id;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setDocbase(String docbase) {
		this.docbase = docbase;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setServiceId(int service_id) {
		this.service_id = service_id;
	}
	
	@Override
    public String toString() {
        return String.format("Service[id=%d,name=%s,host=%s,port=%d,user=%s]\n", 
            service_id, name, host, port, user);
    }

	public void setLastUpdate(Timestamp date) {
		this.lastUpdate = date;
		
	}

	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
}
