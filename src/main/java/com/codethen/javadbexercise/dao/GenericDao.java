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


    private String tableName;  // Queremos la tabla de la base de datos
    private Class<T> type; // We use this type with reflection to make this GenericDao more automatic. Por ejemplo User user


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



    // protected solamente lo pueden ver las clases hijas

    /** Retrieve data from the ResultSet (same columns as {@link #getColumnNames()}) and create object */
    //protected abstract T getObject(ResultSet rs) throws SQLException;  // abstract cuando no tiene instrucciones {}

    protected T getObject(ResultSet rs) throws SQLException { // T es un type parameter

        // create instance of the model class

        try {

            T object = type.newInstance();


            // sets values from rs to the instance

            // Los sets podemos sacarlos mediante las propiedades o métodos:
            // PROPIEDADES
            Field[] fields = type.getDeclaredFields(); // Alt + enter para sacar Field[] fields.  // getDeclaredFields para que coja las propiedades privadas
            for (Field field : fields) {

                field.setAccessible(true);  // Se tiene que dar acceso a la propiedad privada

                Object value = rs.getObject(field.getName());  // rs.getInt("id") o rs.getString("username")
                setValue(object, field, value);



                // SI NO SE GENERALIZA CON 'Object value' y se pone int o String o double
                /*

                if (field.getType() == int.class) {

                    int value = rs.getInt(field.getName());  // rs.getInt("id")

                    // ahora tengo que ponerlo en la instancia

                    setValue(object, field, value);

                } else if (field.getType() == String.class) {

                    String value = rs.getString(field.getName());

                    setValue(object, field, value);

                } else {

                    throw new RuntimeException("Type not supported: " + field.getType()); // o "Unexpected type"
                }
                */
            }

            // return instance
            return object;


        } catch(Exception e) {
            throw new RuntimeException(e);  // El RuntimeException lo pones cuando sabes que es muy raro que pase y lo puedes arreglar
        }

    }


    // Aquí se quiere conseguir //user.setId(rs.getInt("id")); donde 'value' es rs.getInt("id")

    private void setValue(T object, Field field, Object value) throws Exception {  // En lugar de int value, Object value
        String methodName = "set" + StringUtils.capitalize(field.getName()); //  Esto es setId, setUsername, setName...
        Method setter = type.getMethod(methodName, field.getType());  // Esto representa como si fuera setId(int id). methodName es setId, y field.getType() es int id
                                                                    // En lugar de Integer.class o String.class se coge el tipo general de la propiedad. En getter no se necesita field.getType()
        setter.invoke(object, value); // esto hace como si fuera user.setId(value)/user.setId(rs.getInt("id") o user.setUsername(value)
    }

    // field.getClass() te devuelve Field
    // field.getType() te devuelve Integer o String o Date, lo que tiene en las propiedades




    /** Returns the column names in the same order you set them in {@link #setValues(Object, PreparedStatement, boolean)} */
    //protected abstract List<String> getColumnNames();

    protected List<String> getColumnNames() {

        try {

            List<String> fieldNames = new ArrayList<>();

            Field[] fields = type.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                if (!field.getName().equals("id")) {  // field.getName() es "id" o "username" o "name"

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
                String methodName = "get" + StringUtils.capitalize(columnName);  // getUsername, getName, getEmail
                Method getter = type.getMethod(methodName);

                Object getterValue = getter.invoke(object);  // user.getUsername() o user.getName()....

                stmt.setObject(i + 1, getterValue);  // stmt.setString(1, user.getUsername());

            }


        }  catch(Exception e) {

        throw new RuntimeException(e);

        }

    }

}
