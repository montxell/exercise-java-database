package com.codethen.javadbexercise.dao;

import com.codethen.javadbexercise.util.DatabaseUtil;
import com.codethen.javadbexercise.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAOs (Data Access Objects) are responsible of talking with the database to read/write objects
 */
public abstract class GenericDao <T>{


    private String tableName;
    private Class<T> type; // We use this type with reflection to make this GenericDao more automatic. For example: User user


    public GenericDao(String tableName, Class<T> type ) {
        this.tableName = tableName;
        this.type = type;
    }



    public List<T> findAll() {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            // get connection
            conn = DatabaseUtil.getConnection();

            // prepare and execute query
            stmt = conn.prepareStatement("select * from " + tableName);
            rs = stmt.executeQuery();

            List<T> objects = new ArrayList<>();

            while (rs.next()) {

                T object = getObject(rs);
                objects.add(object);

            }

            return objects;

        } catch(Exception e) {
            throw new RuntimeException("error finding objects", e);
        } finally {
            DatabaseUtil.close(rs, stmt, conn);
        }
    }



    public T findById(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;


        try {

            conn = DatabaseUtil.getConnection();

            stmt = conn.prepareStatement("select * from " + tableName + " where id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();


            if (rs.next()) {

                T object = getObject(rs);

                return object;

            } else {

                return null;
            }


        // boilerplate (exception handling and closing resources)
        } catch(Exception e) {
            throw new RuntimeException("error finding object", e);
        } finally {
            DatabaseUtil.close(rs, stmt, conn);
        }
    }



    public void create(T object) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            List<String> columnNames = getColumnNames();

            String columnNamesStr = StringUtils.join(columnNames, "", ", ");
            String questionMarks = StringUtils.repeat("?", ", ", columnNames.size());

            String sqlStatement = "insert into " + tableName + "(" + columnNamesStr + ") values (" + questionMarks + ")";


            stmt = conn.prepareStatement(sqlStatement);

            setValues(object, stmt, false);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error creating object", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
        }
    }




    public void update(T object) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            List<String> columnNames = getColumnNames();

            String equalQuestion = " = ?";
            String columnNameEqualQuestion = StringUtils.join(columnNames, equalQuestion, ", ");

            stmt = conn.prepareStatement("update " + tableName + " set " + columnNameEqualQuestion + " where id" + equalQuestion);

            setValues(object, stmt, true);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error updating object", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
        }
    }



    public void delete(int id) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            stmt = conn.prepareStatement("delete from " + tableName + " where id = ?");
            stmt.setInt(1, id);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error deleting object", e);
        } finally {
            DatabaseUtil.close(null, stmt, conn);
        }
    }




    /** Retrieve data from the ResultSet (same columns as {@link #getColumnNames()}) and create object */
    //protected abstract T getObject(ResultSet rs) throws SQLException;  // abstract cuando no tiene instrucciones {}

    protected T getObject(ResultSet rs) throws SQLException {

        // create instance of the model class

        try {

            T object = type.newInstance();


            // sets values from rs to the instance

            Field[] fields = type.getDeclaredFields();  // getDeclaredFields to obtain private properties
            for (Field field : fields) {

                field.setAccessible(true);  // Get access to private properties

                Object value = rs.getObject(field.getName());   // Objective: rs.getInt("id") o rs.getString("username")

                setValue(object, field, value);



                /*  SI NO SE GENERALIZA CON 'Object value' y se pone int o String


                if (field.getType() == int.class) {

                    int value = rs.getInt(field.getName());

                    setValue(object, field, value);

                } else if (field.getType() == String.class) {

                    String value = rs.getString(field.getName());

                    setValue(object, field, value);

                } else {

                    throw new RuntimeException("Type not supported: " + field.getType());   // or ("Unexpected type")
                }

                */
            }

            // return instance
            return object;


        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }



    private void setValue(T object, Field field, Object value) throws Exception {  // Instead of: int value --> Object value
        String methodName = "set" + StringUtils.capitalize(field.getName());       // Objective: setId, setUsername, setName...
        Method setter = type.getMethod(methodName, field.getType());           // Represents the idea of: setId(int id), setUsername(String username)...
                                                                               // methodName --> setId, setUsername... // field.getType() --> int, String
        setter.invoke(object, value);    // Objective: user.setId(value) o user.setUsername(value)...
                                         // --> user.setId(rs.getInt("id")) o user.setUsername(rs.getString("username"))...
    }



    /** Returns the column names in the same order you set them in {@link #setValues(Object, PreparedStatement, boolean)} */
    //protected abstract List<String> getColumnNames();

    protected List<String> getColumnNames() {

        try {

            List<String> fieldNames = new ArrayList<>();

            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                if (!field.getName().equals("id")) {    // field.getName() --> "id" o "username" o "name"

                    fieldNames.add(field.getName());

                }

            }

            return fieldNames;

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Set the object values into the statement, in the same order of {@link #getColumnNames()}.
     *
     * @param needsId if true, set the id into the statement, at the index after the other columns.
     *
     * */

    // protected abstract void setValues(T object, PreparedStatement stmt, boolean needsId) throws SQLException;

    protected void setValues(T object, PreparedStatement stmt, boolean needsId) throws SQLException {

        try {

            List<String> columnNames = getColumnNames();


            if (needsId) {

                columnNames.add("id");
            }


            for (int i = 0; i < columnNames.size(); i++) {

                String columnName = columnNames.get(i);
                String methodName = "get" + StringUtils.capitalize(columnName);   // Objective: getUsername, getName, getEmail
                Method getter = type.getMethod(methodName);

                Object getterValue = getter.invoke(object);    // Objective: user.getUsername() o user.getName()....

                stmt.setObject(i + 1, getterValue);  // Objective: stmt.setString(1, user.getUsername());....

            }


        }  catch(Exception e) {

        throw new RuntimeException(e);

        }

    }

}
