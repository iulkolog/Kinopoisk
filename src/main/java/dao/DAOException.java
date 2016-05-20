package dao;

/**
 * Created by Iulkolog on 19.05.2016.
 */
public class DAOException extends Exception {
    public DAOException() {
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable t) {
        super(message, t);
    }

    public DAOException(Throwable t) {
        super(t);
    }

}
