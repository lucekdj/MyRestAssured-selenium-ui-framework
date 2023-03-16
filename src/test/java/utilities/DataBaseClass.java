package utilities;

import java.sql.*;

// add dependency to pomXml
//    <dependency>
//            <groupId>mysql</groupId>
//            <artifactId>mysql-connector-java</artifactId>
//            <version>8.0.28</version>
//        </dependency>

public class DataBaseClass {


    static String url = "jdbc:mysql://localhost:3306/bootcamp" ; //address of database pointing on bootcamp from mysql
    static String username = "root";
    static String password = "Sozero343!";

    public static void main(String[] args) throws Exception {
        // registering the driver and getting connection
        Class.forName("com.mysql.cj.jdbc.Driver"); //for register
                Connection con = DriverManager.getConnection(url,username, password );// to get connections driver
        //we need 3 things

            Statement stmt =  con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM bootcamp.employees ");
       // ResultSet rs = stmt.executeQuery("SELECT * FROM bootcamp.employees where name = 'Ali ' ");

            //Moves the cursor forward one row from its current position.
        // A ResultSet cursor is initially positioned before the first row;
        //  the first call to the method next makes the first row the current row;
        // the second call makes the second row the current row, and so on

        //The ResultSet interface provides getter methods
        // (getBoolean, getLong, and so on) for retrieving column values from the current row.
        // Values can be retrieved using either the index number of the column or the name of the column.
        // In general, using the column index will be more efficient. Columns are numbered from 1.

        //rs.next(); //only one name

        //That loop is basically saying, While there is a next value in the column, print it.
//        while (rs.next()) {
//            System.out.print(rs.getString("first_name") + "  ");
//            System.out.println(rs.getString("last_name"));

               System.out.println(rs.getInt("salary"));
//            System.out.println(rs.getString("salary"));
//           // System.out.println(rs.getString(2));
//        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        while(rs.next()){ //outer is for column
            for(int i =1 ; i <= columnsNumber;i++){     //inner is for row
                System.out.print(rs.getString(i)+ "   |   ");
            }
            System.out.println();
        }

    }

}
