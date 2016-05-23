
import dao.*;
import objects.*;

import java.sql.Connection;

public class MyMain {
    public static void main(String[] args) throws DAOException {
        DAOFactory factory = new MySqlDAOFactory<User>();
        Connection connection = (Connection) factory.getContext();
        InterfaceReflectionJdbcDao dao = factory.getDao(connection);
        User u = new User();
        u.setUserId(3);
        dao.selectByKey(u);
    }
}
