package dao;

import java.sql.Connection;

/**
 * Created by Iulkolog on 19.05.2016.
 */
public interface DAOFactory <Context>{

    public Context getContext() throws DAOException;

    public InterfaceReflectionJdbcDao getDao(Context context) throws DAOException;


    public interface DaoCreator<Context> {
        public InterfaceReflectionJdbcDao create(Context context);
    }
}
