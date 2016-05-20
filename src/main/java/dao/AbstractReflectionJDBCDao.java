package dao;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *  Абстрактный класс предоставляющий базовую реализацию CRUD операций с использованием JDBC.
 *
 */
public abstract class AbstractReflectionJDBCDao<T> implements InterfaceReflectionJdbcDao<T> {

    private Connection connection;

    public AbstractReflectionJDBCDao(Connection connection) { this.connection = connection; }

    /**
     * Возвращает запрос для вставки новой записи в базу данных.
     * */
    public abstract String getInsertQuery(T object) throws DAOException;

    /**
     * Возвращает запрос для обновления записи.
     * */
    public abstract String getUpdateQuery(T object);

    /**
     * Возвращает запрос для удаления записи из базы данных.
     */
    public abstract String getDeleteQuery(T object);

    /**
     * Возвращает запрос для выборки записей.
     */
    public abstract String getSelectQuery(T object);

    /**
     * Возвращает запрос для выборки всех записей.
     */
    public abstract String getSelectAllQuery(T object);

    /**
     * Устанавливает аргументы insert запроса в соответствии со значением полей объекта object.
     */

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws DAOException;

    /**
     * Устанавливает аргументы update запроса в соответствии со значением полей объекта object.
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws DAOException;

    /**
     * Устанавливает аргументы delete запроса в соответствии со значением PK полей объекта object.
     */
    protected abstract void prepareStatementForSelect(PreparedStatement statement, T object) throws DAOException;

    /**
     * Разбирает ResultSet и возвращает список объектов соответствующих содержимому ResultSet.
     */
    protected abstract List<T> parseResultSet(ResultSet rs) throws DAOException;

    @Override
    public void insert(T object) throws DAOException {

        String sql = getInsertQuery(object);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void update(T object) throws DAOException {

        String sql = getUpdateQuery(object);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DAOException("On update modify more then 1 record: " + count);
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteByKey(T key) throws DAOException {
        String sql = getDeleteQuery(key);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForSelect(statement, key);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new DAOException("On delete modify more then 1 record: " + count);
            }
            statement.close();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    public T selectByKey(T key) throws DAOException {
        List<T> list;
        String sql = getSelectQuery(key);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForSelect(statement, key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new DAOException(e);
        }
        if (list == null || list.size() == 0) {
            throw new DAOException("Record with such PK(s) not found.");
        }
        if (list.size() > 1) {
            throw new DAOException("More than one record was selected.");
        }
        return list.iterator().next();
    }

   /* @Override
    public List<T> selectAll() throws DAOException {
        List<T> list;
        String sql = getSelectAllQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs,);
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return list;
    }*/

    public static Class<?> getClass(Type type)
    {
        if (type instanceof Class) {
            return (Class) type;
        } else {
            return null;
        }
    }

}
