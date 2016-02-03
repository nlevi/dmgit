package nl.dctm.monitor.dao;
import static nl.dctm.monitor.dao.DAOUtils.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.documentum.fc.common.DfException;

import nl.dctm.monitor.service.DocumentumService;
import nl.dctm.monitor.utils.UpdateDFCProperties;

public class DocumentumServiceDAOJDBC implements DocumentumServiceDAO {
	final static Logger logger = Logger.getLogger(DocumentumServiceDAOJDBC.class);
    private DAOFactory daoFactory;

    DocumentumServiceDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public DocumentumService getServiceById(int id) throws DAOException {
      DocumentumService dctmService = new DocumentumService();
      String by_id_stmt = "SELECT service_id, address, docbase, password, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details WHERE service_id = ?";
        try (
            Connection con = daoFactory.getConnection();        	
            PreparedStatement stmt = preparedStmt(con, by_id_stmt, false, id);
            ResultSet resultSet = stmt.executeQuery();
        ) {
            if (resultSet.next()) {
            	dctmService = mapResults(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return dctmService;
    }
    
    @Override
    public List<DocumentumService> getServicesByType(String type) throws DAOException {
      List<DocumentumService> dctmServices = new ArrayList<>();
      String by_type_stmt = "SELECT service_id, address, docbase, password, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details WHERE service_type = ?";
        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, by_type_stmt, false, type);
            ResultSet resultSet = stmt.executeQuery();
        ) {
            while (resultSet.next()) {
            	dctmServices.add(mapResults(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return dctmServices;
    }

    @Override
    public List<DocumentumService> getAllServices() throws DAOException {
        List<DocumentumService> dctmServices = new ArrayList<>();
        String all_stmt =
                "SELECT service_id, address, docbase, password, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details ORDER BY service_id";
        try (
        	Connection con = daoFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(all_stmt);
            ResultSet resultSet = stmt.executeQuery();
        ) {
            while (resultSet.next()) {
            	dctmServices.add(mapResults(resultSet));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return dctmServices;
    }

    @Override
    public void create(DocumentumService dctmService) throws IllegalArgumentException, DAOException {        

        Object[] values = {
        		dctmService.getAddress(),
        		dctmService.getDocbase(),
        		dctmService.getPassword(),
        		dctmService.getUser(),
        		dctmService.getPort(),
        		dctmService.getHost(),
        		dctmService.getType(),
        		dctmService.getName()
        };
        
        String create_stmt = "INSERT INTO mntr_env_details (address, docbase, password, service_user, service_port, service_host, service_type, service_name) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";
        
        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, create_stmt, true, values);
        ) {
            int createdRows = stmt.executeUpdate();
            if (createdRows == 0) {
                throw new DAOException("Creating service failed.");
            } else {            	
            	if(dctmService.getType().equals("cs")) {        			
        			UpdateDFCProperties.update(dctmService.getHost(), dctmService.getPort());        			
        		}
            }
            
        } catch (SQLException | IOException | DfException | URISyntaxException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(DocumentumService dctmService) throws DAOException {
        if (dctmService.getId() == 0) {
            throw new IllegalArgumentException("Serivce is not created yet, the service ID is 0.");
        }       
        
        Object[] values = {
        		dctmService.getAddress(),
        		dctmService.getDocbase(),
        		dctmService.getPassword(),
        		dctmService.getUser(),
        		dctmService.getPort(),
        		dctmService.getHost(),
        		dctmService.getType(),
        		dctmService.getName(),        	
        		dctmService.getStatus(),
        		dctmService.getVersion(),        		
        		convertToTimestamp(new Date()),
        		dctmService.getServiceContext(),
        		dctmService.getId()
        };
        
        String update_stmt = "UPDATE mntr_env_details SET address = ?, docbase = ?, password = ?, service_user = ?, service_port = ?, service_host = ?, service_type = ?, service_name = ?, service_status = ?, service_version = ?, last_update = ?, service_context = ? WHERE service_id = ?";
        
        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, update_stmt, false, values);
        ) {        	
            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
    
    @Override
    public void delete(int id) throws DAOException {
    	
    	String delete_stmt = "DELETE FROM mntr_env_details WHERE service_id = ?";
    	
        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, delete_stmt, false, id);
        ) {
            int deletedRow = stmt.executeUpdate();
            if (deletedRow == 0) {
                throw new DAOException("Deleting service failed, no rows affected.");
            } 
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
    
    @Override
    public void execQuery(String query) {
    	try (
                Connection con = daoFactory.getConnection();
                PreparedStatement stmt = preparedStmt(con, query, false, null);
            ) {
                stmt.executeUpdate();
                
            } catch (SQLException e) {
                if(tableExists(e)) {
                	logger.info("Tablealready exists.");
                }
            }
    }
}
