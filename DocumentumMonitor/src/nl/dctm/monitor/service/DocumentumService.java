package nl.dctm.monitor.service;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mntr_env_details")

public class DocumentumService {
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "docbase")
	private String docbase;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "service_user")
	private String service_user;
	
	@Column(name = "service_host")
	private String service_host;
	
	@Column(name = "service_port")
	private int service_port;
	
	@Column(name = "service_type")
	private String service_type;
	
	@Column(name = "service_name")
	private String service_name;
	
	@Column(name = "service_version")
	private String service_version;
	
	@Column(name = "service_status")
	private String service_status;
	
	@Id
	@Column(name = "service_id")
	private int service_id;
	
	@Column(name = "last_update")
	private Timestamp last_update;
	
	@Column(name = "service_context")
	private String service_context;

	public String getHost() {
		return service_host;
	}

	public int getPort() {
		return service_port;
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
		return service_user;
	}

	public String getType() {
		return service_type;
	}

	public String getName() {
		return service_name;
	}

	public String getVersion() {
		return service_version;
	}

	public String getStatus() {
		return service_status;
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
		this.service_user = user;
	}

	public void setHost(String host) {
		this.service_host = host;
	}

	public void setPort(int port) {
		this.service_port = port;
	}

	public void setType(String type) {
		this.service_type = type;
	}

	public void setName(String name) {
		this.service_name = name;
	}

	public void setVersion(String version) {
		this.service_version = version;
	}

	public void setStatus(String status) {
		this.service_status = status;
	}

	public void setServiceId(int service_id) {
		this.service_id = service_id;
	}
	
	@Override
    public String toString() {
        return String.format("Service[id=%d,name=%s,host=%s,port=%d,user=%s]\n", 
            service_id, service_name, service_host, service_port, service_user);
    }

	public void setLastUpdate(Timestamp date) {
		this.last_update = date;
		
	}

	public Timestamp getLastUpdate() {
		return last_update;
	}

	public String getServiceContext() {
		return service_context;
	}

	public void setServiceContext(String service_context) {
		this.service_context = service_context;
	}

}
