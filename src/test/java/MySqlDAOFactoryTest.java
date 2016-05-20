/**
 * Created by Iulkolog on 20.05.2016.
 */
import dao.DAOException;
import dao.DAOFactory;
import dao.InterfaceReflectionJdbcDao;
import dao.MySqlDAOFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;


public class MySqlDAOFactoryTest extends ReflectionJdbcDaoTest<Connection> {

    private Connection connection;

    private InterfaceReflectionJdbcDao dao;

    private DAOFactory<Connection> factory = new MySqlDAOFactory<>();
    

    @Override
    public InterfaceReflectionJdbcDao dao() {
        return dao;
    }

    @Override
    public Connection context() {
        return connection;
    }

    public MySqlDAOFactoryTest(Class clazz, Class notPersistedDto) {
        super(clazz, notPersistedDto);
    }
}