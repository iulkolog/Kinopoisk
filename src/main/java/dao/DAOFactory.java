package dao;

import java.sql.Connection;

/**
 * Created by Iulkolog on 19.05.2016.
 */
public interface DAOFactory <Context>{

    /** Возвращает подключение к базе данных */
    public Context getContext() throws DAOException;

   // public InterfaceReflectionJdbcDao getDao(Context context, Class dtoClass) throws DAOException;

    //public InterfaceReflectionJdbcDao getDao(Connection connection, Class dtoClass) throws DAOException;

    InterfaceReflectionJdbcDao getDao(Connection connection) throws DAOException;


    public interface DaoCreator<Context> {
        public InterfaceReflectionJdbcDao create(Context context);
    }
}
