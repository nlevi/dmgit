package nl.emonitor.service;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "env_services")

public class EnvironmentService {	
	
	private String address;	
	private String docbase;	
	private String password;	
	private String service_user;	
	private String service_host;	
	private int service_port;	
	private String service_type;	
	private String service_name;	
	private String service_version;	
	private String service_status;	
	private int service_id;	
	private Timestamp last_update;	
	private String service_context;
	
	@Column(name = "service_host")
	public String getHost() {
		return service_host;
	}
	
	@Column(name = "service_port")
	public int getPort() {
		return service_port;
	}
	
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	
	@Column(name = "docbase")
	public String getDocbase() {
		return docbase;
	}
	
	@Column(name = "password")
	public String getPassword() {
		return password;
	}
	
	@Column(name = "service_user")
	public String getUser() {
		return service_user;
	}
	
	@Column(name = "service_type")
	public String getType() {
		return service_type;
	}
	
	@Column(name = "service_name")
	public String getName() {
		return service_name;
	}
	
	@Column(name = "service_version")
	public String getVersion() {
		return service_version;
	}
	
	@Column(name = "service_status")
	public String getStatus() {
		return service_status;
	}
	
	@Id
	@Column(name = "service_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return service_id;
	}	
	
	@Column(name = "last_update")
	public Timestamp getLastUpdate() {
		return last_update;
	}
	
	@Column(name = "service_context")
	public String getServiceContext() {
		return service_context;
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
	
	public void setLastUpdate(Timestamp date) {
		this.last_update = date;
		
	}

	public void setServiceContext(String service_context) {
		this.service_context = service_context;
	}
	
	@Override
    public String toString() {
        return String.format("Service[id=%d,name=%s,host=%s,port=%d,user=%s]\n", 
            service_id, service_name, service_host, service_port, service_user);
    }

}

