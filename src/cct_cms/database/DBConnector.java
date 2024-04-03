/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.database;

import cct_cms.dao.CourseReportDAO;
import cct_cms.dao.LecturerReportDAO;
import cct_cms.dao.StudentReportDAO;
import cct_cms.entities.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * @author bemello
 */
public class DBConnector {
    
    private final String DB_URL = "jdbc:mysql://localhost/cms_db";
    private final String USER = "cms-application";
    private final String PASSWORD = "cms-application2024CCT";
    
    private Connection connection;
    private User loggedUser;
    
    private void connect() throws SQLException {
        this.connection =  DriverManager.getConnection(
                DB_URL, USER, PASSWORD );
    }
    
    private void closeConnection() throws SQLException {
        this.connection.close();
    }
    
    private PreparedStatement getPreparedStatement( String parametrizedQuery )
            throws SQLException {
        return connection.prepareStatement( parametrizedQuery );
    }
    
    public User login( String username, String password ) {
        try {
            connect();
            
            PreparedStatement stmt = getPreparedStatement( 
                    "select * from CMS_User where username = ? and password = ?" );
            stmt.setString( 1, username);
            stmt.setString( 2, password);
            ResultSet rs = stmt.executeQuery();            
            
            if ( !rs.isBeforeFirst() ) {
                System.err.println( "Incorrect Username/Password!" );
            }
            else {
                rs.next();
                loggedUser = new User();
                loggedUser.setId( rs.getInt( "id" ) );
                loggedUser.setUsername( rs.getString( "username" ) );
                loggedUser.setPassword(rs.getString( "password" ) );
                loggedUser.setType(rs.getString( "type" ) );
                loggedUser.setLecturerId( rs.getInt( "lecturer_id" ) );
            }
            
            closeConnection();
        }
        catch( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        
        return this.loggedUser;
    }
    
    public User getUserByUsername( String username ) {
        User user = null;
        try {
            connect();
            
            PreparedStatement stmt = getPreparedStatement( 
                    "select * from CMS_User where username = ?");
            stmt.setString( 1, username);
            ResultSet rs = stmt.executeQuery();            
            
            if ( !rs.isBeforeFirst() ) {
                System.err.println( "No user found!" );
            }
            else {
                rs.next();
                user = new User(
                        rs.getInt( "id" ), 
                        rs.getString( "username" ),
                        rs.getString( "password" ),
                        rs.getString( "type" ),
                        rs.getInt( "lecturer_id" ) );
            }
            
            closeConnection();
        }
        catch( SQLException e ) {
            System.out.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return user;
    }

    public User getUserById( int id ) {
        User user = null;
        try {
            connect();
            
            PreparedStatement stmt = getPreparedStatement( 
                    "select * from CMS_User where id = ?");
            stmt.setInt( 1, id );
            ResultSet rs = stmt.executeQuery();            
            
            if ( !rs.isBeforeFirst() ) {
                System.err.println( "No user found!" );
            }
            else {
                rs.next();
                user = new User(
                        rs.getInt( "id" ), 
                        rs.getString( "username" ),
                        rs.getString( "password" ),
                        rs.getString( "type" ),
                        rs.getInt( "lecturer_id" ) );
            }
            
            closeConnection();
        }
        catch( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return user;
    }
    
    public String findLecturerById( int id ) {
        String result = null;
        try {
            connect();
            
            PreparedStatement stmt = getPreparedStatement( 
                    "select CONCAT(name, ' ', surname) as lecturer_name from CMS_Lecturer where id = ?" );
            stmt.setInt( 1, id );
            ResultSet rs = stmt.executeQuery();            
            
            if ( !rs.isBeforeFirst() ) {
                System.err.println( "No lecturer found for the unique id " + id );
            }
            else {
                rs.next();
                result = rs.getString( "lecturer_name" );
            }
            
            closeConnection();
        }
        catch( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return result;
    }
    
    public void addUser( User user ) {
        try {
            connect();
            PreparedStatement stmt = 
                    getPreparedStatement( 
                            "INSERT INTO CMS_User(CMS_User.username, CMS_User.password, CMS_User.type, CMS_User.lecturer_id ) VALUES ( ?, ?, ?, ? )" );
            stmt.setString( 1, user.getUsername() );
            stmt.setString( 2, user.getPassword() );
            stmt.setString( 3, user.getType() );
            if ( user.getLecturerId() != 0 ) {
                stmt.setInt( 4, user.getLecturerId() );
            } else {
                stmt.setObject( 4, null );
            }
            stmt.executeUpdate();
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
    }
    
    public void deleteUser( User user ) {
        try {
            connect();
            PreparedStatement stmt = 
                    getPreparedStatement( 
                            "DELETE FROM CMS_User WHERE id = ?" );
            stmt.setInt( 1, user.getId() );
            stmt.executeUpdate();
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
    }
    
    public void updateUser( User user ) {
        try {
            connect();
            PreparedStatement stmt = 
                    getPreparedStatement( 
                            "UPDATE CMS_User SET username = ?, password = ?, type = ?, lecturer_id = ? WHERE id = ?" );
            stmt.setString( 1, user.getUsername() );
            stmt.setString( 2, user.getPassword() );
            stmt.setString( 3, user.getType() );
            if ( user.getLecturerId() != 0 ) {
                stmt.setInt( 4, user.getLecturerId() );
            } else {
                stmt.setObject( 4, null );
            }
            stmt.setInt(5, user.getId() );
            stmt.executeUpdate();
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
    }
    
    public ArrayList<CourseReportDAO> generateCourseReport() {
        String query = "SELECT module.name as 'module', course.name as 'programme',";
        query += "COUNT(student.id) as 'numberStudents',";
        query += "CONCAT(lecturer.name,' ',lecturer.surname) as 'lecturerName',";
        query += "module.room as 'room' ";
        query += "FROM CMS_Module module ";
        query += "JOIN CMS_Course course ON course.id = module.course_id ";
        query += "JOIN CMS_Lecturer lecturer ON lecturer.id = module.lecturer_id ";
        query += "JOIN CMS_Student student ON student.course_id = course.id ";
        query += "GROUP BY module.id ";
        query += "ORDER BY course.name, module.name;";
        
        ArrayList<CourseReportDAO> results = new ArrayList<>();
        
        try {
            connect();
            PreparedStatement stmt = getPreparedStatement( query );
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                CourseReportDAO courseReport = 
                        new CourseReportDAO(
                                rs.getString( "module" ),
                                rs.getString( "programme" ),
                                rs.getInt( "numberStudents" ),
                                rs.getString( "lecturerName" ),
                                rs.getString( "room" )
                        );
                results.add( courseReport );
            }
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return results;
    }
    
    public ArrayList<StudentReportDAO> generateStudentReport() {
        String query = "select CONCAT(student.name,' ',student.surname) as 'studentName',";
        query += "student.student_number as 'studentNumber',";
        query += "course.name as 'programme',";
        query += "module.name as 'module',";
        query += "enrollment.start_date as 'startDate',";
        query += "enrollment.end_date as 'endDate',";
        query += "grade1.grade_value as 'firstGrade',";
        query += "grade2.grade_value as 'secondGrade',";
        query += "grade3.grade_value as 'thirdGrade' ";
        query += "FROM CMS_Student student ";
        query += "JOIN CMS_Enrollment enrollment ON enrollment.student_id = student.id ";
        query += "JOIN CMS_Module module ON enrollment.module_id = module.id ";
        query += "JOIN CMS_Grade grade1 ON grade1.enrollment_id = enrollment.id AND grade1.description = 'CA 1' ";
        query += "LEFT JOIN CMS_Grade grade2 ON grade2.enrollment_id = enrollment.id AND grade2.description = 'CA 2' ";
        query += "LEFT JOIN CMS_Grade grade3 ON grade3.enrollment_id = enrollment.id AND grade3.description = 'CA 3' ";
        query += "JOIN CMS_Course course ON course.id = student.course_id ";
        query += "ORDER BY student.name;";
        
        ArrayList<StudentReportDAO> results = new ArrayList<>();
        
        try {
            connect();
            PreparedStatement stmt = getPreparedStatement( query );
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                StudentReportDAO studentReport = 
                        new StudentReportDAO(
                                rs.getString( "studentName" ),
                                rs.getString( "studentNumber" ),
                                rs.getString( "programme" ),
                                rs.getString( "module" ),
                                rs.getDate( "startDate" ),
                                rs.getDate( "endDate" ),
                                rs.getDouble( "firstGrade" ),
                                rs.getDouble( "secondGrade" ),
                                rs.getDouble( "thirdGrade" )
                        );
                results.add( studentReport );
            }
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return results;
    }
    
    public ArrayList<LecturerReportDAO> generateLecturerReport( int id ) {
        String query = "select CONCAT(lecturer.name,' ',lecturer.surname) as 'lecturerName',";
        query += "lecturer.role as 'role',";
        query += "module.name as 'module',";
        query += "COUNT(student.id) as 'numberStudents'";
        query += "FROM CMS_Lecturer lecturer ";
        query += "JOIN CMS_Module module ON module.lecturer_id = lecturer.id ";
        query += "JOIN CMS_Enrollment enrollment ON enrollment.module_id = module.id ";
        query += "JOIN CMS_Student student ON student.id = enrollment.student_id ";
        if ( id > 0 ) {
            query += "WHERE lecturer.id = ?";
        }
        query += "GROUP BY module.id ";
        query += "ORDER BY lecturer.name;";
        
        ArrayList<LecturerReportDAO> results = new ArrayList<>();
        
        try {
            connect();
            PreparedStatement stmt = getPreparedStatement( query );
            if ( id > 0 ) {
                stmt.setInt( 1, id );
            }
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                LecturerReportDAO lecturerReport = 
                        new LecturerReportDAO( 
                                rs.getString( "lecturerName" ),
                                rs.getString( "role" ),
                                rs.getString( "module" ),
                                rs.getInt( "numberStudents" )
                        );
                results.add( lecturerReport );
            }
            closeConnection();
        } catch ( SQLException e ) {
            System.err.println( "Error trying to connect to database: " + e.getMessage() );
        }
        return results;
    }
    
}
