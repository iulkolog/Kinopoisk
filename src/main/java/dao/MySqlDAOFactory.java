package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Iulkolog on 19.05.2016.
 */
public class MySqlDAOFactory<T> implements DAOFactory<Connection>{
    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/mydatabase";
    private String driver = "com.mysql.jdbc.Driver";



    public Connection getContext() throws DAOException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return  connection;
    }

    @Override
    public InterfaceReflectionJdbcDao getDao(Connection connection) throws DAOException {

        MySqlReflectionJdbcDao<T> i = new MySqlReflectionJdbcDao<>(connection);
        return i;
    }

    public MySqlDAOFactory() {
        try {
            Class.forName(driver).newInstance();
            //DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
