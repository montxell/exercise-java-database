package com.codethen.javadbexercise;


import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Responsible of talking with the database to read/write objects */
public abstract class GenericDao <T>{


    private String tableName;  // Queremos la tabla de la base de datos


    public GenericDao(String tableName) {
        this.tableName = tableName;
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
            firstClose(rs, stmt, conn);
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
            firstClose(rs, stmt, conn);
        }
    }



    private void firstClose(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if(rs != null) {
                rs.close();
            }
            if(stmt != null) {
                stmt.close();
            }
            if(conn != null) {
                conn.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void create(T object) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            List<String> columnNames = getColumnNames();

            String columnNamesStr = StringUtils.join(columnNames, ", ");
            String questionMarks = StringUtils.repeat("?", ", ", columnNames.size());

            String sql = "insert into " + tableName + "(" + columnNamesStr + ") values (" + questionMarks + ")";


            stmt = conn.prepareStatement(sql);

            setValues(object, stmt, false);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error creating object", e);
        } finally {
            secondClose(stmt, conn);
        }
    }




    public void update(T object) {

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DatabaseUtil.getConnection();

            // TODO

            stmt = conn.prepareStatement("update " + tableName + " set username = ?, name = ?, email = ? where id = ?");

            setValues(object, stmt, true);

            stmt.executeUpdate();


        } catch(Exception e) {
            throw new RuntimeException("error updating object", e);
        } finally {
            secondClose(stmt, conn);
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
            secondClose(stmt, conn);
        }
    }


    private void secondClose(PreparedStatement stmt, Connection conn) {
        try {
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    /** Retrieve data from the ResultSet (same columns as {@link #getColumnNames()}) and create object */
    protected abstract T getObject(ResultSet rs) throws SQLException;

    // protected solamente lo pueden ver las clases hijas


    /** Returns the column names in the same order you set them in {@link #setValues(Object, PreparedStatement, boolean)} */
    protected abstract List<String> getColumnNames();


    /**
     * Set the object values into the statement, in the same order of {@link #getColumnNames()}.
     *
     * @param needsId if true, set the id into the statement, at the index after the other columns.
     *
     * */

    protected abstract void setValues(T object, PreparedStatement stmt, boolean needsId) throws SQLException;

}
