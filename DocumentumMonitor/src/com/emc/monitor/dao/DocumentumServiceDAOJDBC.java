package com.emc.monitor.dao;



import static com.emc.monitor.dao.DAOUtils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.emc.monitor.service.DocumentumService;

public class DocumentumServiceDAOJDBC implements DocumentumServiceDAO {

    private static final String GET_BY_ID_STMT =
        "SELECT service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details WHERE service_id = ?";
    private static final String GET_BY_TYPE_STMT =
        "SELECT service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details WHERE service_type = ?";
    private static final String GET_ALL_STMT =
        "SELECT service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name, service_status, service_version, last_update FROM mntr_env_details ORDER BY service_id";
    private static final String CREATE_STMT =
        "INSERT INTO mntr_env_details (service_id, admin_address, docbase, user_passwd, service_user, service_port, service_host, service_type, service_name) VALUES (NEXT VALUE FOR service_id, ?, ?, ?, ?, ?, ?, ? ,?)";
    private static final String UPDATE_STMT =
        "UPDATE mntr_env_details SET admin_address = ?, docbase = ?, user_passwd = ?, service_user = ?, service_port = ?, service_host = ?, service_type = ?, service_name = ?, service_status = ?, service_version = ?, last_update = ? WHERE service_id = ?";

    private DAOFactory daoFactory;

    DocumentumServiceDAOJDBC(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public DocumentumService getServiceById(int id) throws DAOException {
      DocumentumService dctmService = new DocumentumService();

        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, GET_BY_ID_STMT, false, id);
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

        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, GET_BY_TYPE_STMT, false, type);
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
        
        try (
        	Connection con = daoFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_ALL_STMT);
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
//            toSqlDate(dctmService.getDate())
        };

        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, CREATE_STMT, true, values);
        ) {
            int createdRows = stmt.executeUpdate();
            if (createdRows == 0) {
                throw new DAOException("Creating service failed.");
            }
            
        } catch (SQLException e) {
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
        		convertToSqlTimestamp(new Date()),
        		dctmService.getId()
        };

        try (
            Connection con = daoFactory.getConnection();
            PreparedStatement stmt = preparedStmt(con, UPDATE_STMT, false, values);
        ) {
        	System.out.println(stmt.toString());
            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new DAOException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
