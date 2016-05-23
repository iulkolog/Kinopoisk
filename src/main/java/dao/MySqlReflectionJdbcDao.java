package dao;

import objects.FieldInfo;
import objects.TableInfo;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


public class MySqlReflectionJdbcDao <T>  extends AbstractReflectionJDBCDao < T > {


    public MySqlReflectionJdbcDao(Connection connection) {
        super(connection);
    }

    @Override
    public String getInsertQuery(T object) throws DAOException {

        try{
            String tableName = getTableName(object);

            String tableFields = "";
            String values = "";
            Field[] fields;
            int numberOfAnnotatedFields = 0;

            fields = object.getClass().getFields();

            for(Field f: fields) {
                if (f.isAnnotationPresent(FieldInfo.class)) {
                    if (!f.getAnnotation((FieldInfo.class)).isPrimaryKey()){
                        tableFields = String.format("%s, %s", tableFields, f.getAnnotation((FieldInfo.class)).fieldName());
                        values = String.format("%s, %s", values, "?");
                        numberOfAnnotatedFields++;
                    }
                }
            }
            if (numberOfAnnotatedFields == 0)
                throw new Exception();


            String sql = "INSERT INTO " + tableName +  "(" + tableFields + ") \n" +
                        " VALUES (" + values + ");";
            return sql;


        }catch (Exception e){
            throw new DAOException(e + " Class did'nt annotated with TableInfo or FieldIndo @interface");

        }

    }

    //"UPDATE  SET FIELD1 = ? ... WHERE PK = ?;"
    @Override
    public String getUpdateQuery(T object) throws DAOException {

        try{
            String tableName = getTableName(object);

            String primaryKey = getPrimaryKey(object, "%s %s", " = ?" );

            String tableFields = getFieldNames(object, "%s %s", " = ?", false);

            String sql = "UPDATE " + tableName + " SET " + tableFields + " WHERE " + primaryKey + ";";
            return sql;


        }catch (Exception e){
            throw new DAOException(e + " Class did'nt annotated with TableInfo or FieldIndo @interface");

        }
    }

    @Override
    public String getDeleteQuery(T object) throws DAOException {

        try{
            String tableName = getTableName(object);

            String primaryKey = getPrimaryKey(object,  "%s %s", " = ?" );


            String sql = "DELETE FROM " + tableName + " WHERE " + primaryKey;
            return sql;


        }catch (Exception e){
            throw new DAOException(e + " Class did'nt annotated with TableInfo @interface");

        }

    }

    @Override
    public String getSelectQuery(T object) throws DAOException {

        try{
            String tableName = getTableName(object);

            String primaryKey = getPrimaryKey(object,  "%s %s", " = ?");

            String values = getFieldNames(object, "%s, %s", "", true);

            String sql = "SELECT " + values + " FROM " + tableName + " WHERE " + primaryKey;


            System.out.println(sql);
            return sql;


        }catch (Exception e){
            throw new DAOException(e + " Class did'nt annotated with TableInfo or FieldIndo @interface");

        }
    }

    @Override
    public String getSelectAllQuery(T object) throws DAOException {

        try{
            String tableName = getTableName(object);

            String values = getFieldNames(object, "%s, %s", "", true);

            String sql = "SELECT " + values + " FROM " + tableName;
            return sql;


        }catch (Exception e){
            throw new DAOException(e + " Class did'nt annotated with TableInfo or FieldIndo @interface");

        }
    }

    private String getTableName(T object) throws DAOException{
        try {
            TableInfo tableInfo = object.getClass().getAnnotation(TableInfo.class);
            return tableInfo.tableName();

        }catch (Exception e){
            throw new DAOException(e);
        }
    }

    private String getPrimaryKey(T object, String format, String additional) throws DAOException{
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            int numberOfPrimaryKeysFields = 0;
            String tableFields = "";

            if (fields.length != 0)
            {
                fields[0].setAccessible(true);
                if (fields[0].isAnnotationPresent(FieldInfo.class) )
                    if (fields[0].getAnnotation(FieldInfo.class).isPrimaryKey()) {
                        tableFields = fields[0].getAnnotation((FieldInfo.class)).fieldName() +
                                additional;
                        numberOfPrimaryKeysFields++;
                    }
            }

            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].isAnnotationPresent(FieldInfo.class) )
                    if (fields[i].getAnnotation(FieldInfo.class).isPrimaryKey()) {
                        tableFields = String.format(format, tableFields, fields[i].getAnnotation((FieldInfo.class)).fieldName() +
                                additional);
                        numberOfPrimaryKeysFields++;
                    }
            }
            if (numberOfPrimaryKeysFields == 0)
                throw new Exception();
            return tableFields;

        }catch (Exception e){
            throw new DAOException(e);
        }
    }


    private String getFieldNames(T object, String format, String additional, boolean withPK) throws DAOException{
        try {
            Field[] fields = getAnnotatedFields(object,withPK);
            String tableFields = "";

            if (fields.length != 0)
            {
                fields[0].setAccessible(true);
                if (fields[0].isAnnotationPresent(FieldInfo.class) )
                    if (fields[0].getAnnotation(FieldInfo.class).isPrimaryKey()) {
                        tableFields = fields[0].getAnnotation((FieldInfo.class)).fieldName() +
                                additional;
                    }
            }

            for (int i = 1; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].isAnnotationPresent(FieldInfo.class) ) {
                    tableFields = String.format(format, tableFields, fields[i].getAnnotation((FieldInfo.class)).fieldName() +
                            additional);
                }

            }
            return tableFields;

        }catch (Exception e){
            throw new DAOException(e);
        }

    }

    private Field[] getAnnotatedFields(T object, boolean withPK) throws DAOException{
        ArrayList<Field> annotatedFields = new ArrayList<>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field f : fields) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(FieldInfo.class) )
                    if ((withPK) || (!f.getAnnotation(FieldInfo.class).isPrimaryKey())) {
                        annotatedFields.add(f);

                    }
            }
            if (annotatedFields.isEmpty())
                throw new Exception("there are no one annotated field in T object");
            return  annotatedFields.toArray(new Field[annotatedFields.size()]);

        }catch (Exception e){
            throw new DAOException(e);
        }

    }


    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, T object) throws DAOException {

        try {
            Field[] fields = object.getClass().getDeclaredFields();

            for(int i = 1; i<= fields.length; i++) {
                fields[i-1].setAccessible(true);
                if(fields[i-1].isAnnotationPresent(FieldInfo.class)&&(!fields[i-1].getAnnotation(FieldInfo.class).isPrimaryKey())) {
                    /*Type type = fields[i - 1].getGenericType();
                    if (type instanceof ParameterizedType) {
                    ParameterizedType ptype = (ParameterizedType) type;
                    ptype.getRawType();
                    ptype.getActualTypeArguments()[0];
                    /** для ссылочных типов данных можно было бы создавать отдельную таблицу на каждый с
                     * ключевым полем таким же как у T object и соответствующими полями для остальных
                     * аргументов. Но так как ссылочные типы внутри могут содержать другие ссылочные типы,
                     * то нужно было бы решать задачу рекурсивно. Возможно лучше было бы тогда данные хранить
                     * в json формате
                     * */
                    //} else {*/

                    statement.setObject(i, fields[i - 1].get(object));
                }
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, T object) throws DAOException {
        try {
            Field[] fields = object.getClass().getDeclaredFields();

            int i = 1;
            for(Field f: fields) {
                f.setAccessible(true);
                if(f.isAnnotationPresent(FieldInfo.class)&&(!f.getAnnotation(FieldInfo.class).isPrimaryKey())) {
                    statement.setObject(i, f.get(object));
                    i++;
                }
            }
            for(Field f: fields) {
                f.setAccessible(true);
                if(f.isAnnotationPresent(FieldInfo.class)&&(f.getAnnotation(FieldInfo.class).isPrimaryKey())) {
                    statement.setObject(i, f.get(object));
                    i++;
                }
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

    @Override
    protected void prepareStatementForSelect(PreparedStatement statement, T object) throws DAOException {
        try {
            Field[] fields = object.getClass().getDeclaredFields();


            System.out.println(fields.length);
            int i = 1;
            for(Field f: fields) {
                f.setAccessible(true);
                System.out.println("i = " + i);
                if(f.isAnnotationPresent(FieldInfo.class)&&(f.getAnnotation(FieldInfo.class).isPrimaryKey())) {
                    statement.setObject(i, f.get(object));
                    i++;
                }
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    @Override
    protected List<T> parseResultSet(ResultSet resultSet) throws DAOException {
        LinkedList<T> result = new LinkedList<>();
        try {

            int i = 1;
            while (resultSet.next()) {
                Object obj = resultSet.getObject(i);
                T item = (T)obj;
                result.add(item);
                i++;
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }
        return result;
    }


}
