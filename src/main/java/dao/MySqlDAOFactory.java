package dao;

import objects.User;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by Iulkolog on 19.05.2016.
 */
public class MySqlDAOFactory<T> implements DAOFactory{
    private String user = "root";
    private String password = "";
    private String url = "jdbc:mysql://localhost:3306/mydatabase";
    private String driver = "com.mysql.jdbc.Driver";


    @Override
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
            Class.forName(driver);//Регистрируем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
