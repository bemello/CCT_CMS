/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cct_cms;

import cct_cms.dao.CourseReportDAO;
import cct_cms.dao.LecturerReportDAO;
import cct_cms.dao.StudentReportDAO;
import cct_cms.database.DBConnector;
import cct_cms.entities.User;
import cct_cms.util.FileUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 *
 * @author bemello
 */
public class Application {

    private final static DBConnector DB_CONNECTOR = new DBConnector();
    
    private static FileUtils fileUtils = new FileUtils();

    private static User loggedUser;
    
    private static Scanner scanner = new Scanner( System.in );

    private final static String[] COURSE_REPORT_HEADER =
        { "Course", "Module", "Number of Students", "Lecturer", "Room"};
    
    private final static String COURSE_REPORT_HEADER_FORMAT = 
            "%-70s %-50s %-20s %-30s %-10s";

    private final static String[] STUDENT_REPORT_HEADER =
        { "Student", "Student Number", "Course", "Module", "Starting Date",
        "End Date", "First Grade", "Second Grade", "Third Grade", "Final Result" };
    
        private final static String STUDENT_REPORT_HEADER_FORMAT = 
            "%-20s %-20s %-70s %-50s %-20s %-20s %-20s %-20s %-20s %-20s";

    private final static String[] LECTURER_REPORT_HEADER =
        { "Lecturer", "Role", "Module", "Number of Students" };
    
    private final static String LECTURER_REPORT_HEADER_FORMAT =
            "%-20s %-20s %-50s %-20s";
    
    private final static String COURSE_REPORT_FILENAME = 
            "course_report";
    
    private final static String STUDENT_REPORT_FILENAME = 
            "student_report";
    
    private final static String LECTURER_REPORT_FILENAME = 
            "lecturer_report";

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) {
        
        displayInitialMenu();
        
        System.out.println( loggedUser.getType() );
        
        if ( loggedUser.isAdmin() ) {
            displayAdminMenu();
        } else if ( loggedUser.isOffice() ) {
            displayOfficeMenu();
        } else {
            displayLecturerMenu();
        }
        //generateCourseReportCSV();
        //generateCourseReportTXT();
        //generateCourseReportTXT( true );
    }
        
    private static void displayInitialMenu() {
        while ( loggedUser == null ) {            
            System.out.println( "College CMS - Login" );
            System.out.println( "-------------------------" );
            System.out.println( "Please, enter your username: " );
            String username = scanner.nextLine();
            System.out.println( "Please, enter your password: " );
            String password = scanner.nextLine();
            doLogin( username, password );            
        }
    }

    private static void displayAdminMenu() {
        System.out.print( "Welcome, " );
        System.out.println( loggedUser.getUsername() );
        System.out.println( "-------------------------" );
        System.out.println( "Select an option: " );
        System.out.println("1 - Manage Users");
        System.out.println("2 - Change username");
        System.out.println("3 - Change password");
        
        switch( scanner.nextInt() ) {
            case 1: 
                scanner.nextLine();
                displayUserManagementMenu();
                break;
            case 2: 
                scanner.nextLine();
                updateUsernameMenu();
                displayAdminMenu();
                break;
            case 3:
                scanner.nextLine();
                updatePasswordMenu();
                displayAdminMenu();
                break;
            default:
                System.err.println( "Invalid option!" );
                displayAdminMenu();
        }
    }

    private static void updatePasswordMenu() {
        System.out.println( "Please, type the new password: " );
        String newPassword = scanner.nextLine();
        if ( isPasswordValid( newPassword ) ) {
            updatePassword( newPassword );
        }
    }

    private static boolean isPasswordValid( String newPassword ) {
        boolean result = false;
        if ( newPassword.length() < 4 ) {
            System.err.println( "Password must have at least 4 characters" );
        } else {
            System.out.println( "Please, re-type the new password: " );
            String newPasswordConfirm = scanner.nextLine();
            if ( !newPassword.equals( newPasswordConfirm) ) {
                System.err.println( "Password mismatch. Please, try again!" );
            } else {
                result = true;
            }
        }
        return result;
    }

    private static void updateUsernameMenu() {
        System.out.println( "Please, type the new username: " );
        String newUsername = scanner.nextLine();
        if ( isUsernameValid( newUsername ) ){
            updateUsername( newUsername );
        }
    }

    private static boolean isUsernameValid( String newUsername ) {
        boolean result = true;
        if ( newUsername.length() < 5 ) {
            result = false; 
            System.err.println( "Username must have at least 5 characters" );
        } 
        return result;
    }
    
    private static void displayUserManagementMenu() {
        System.out.println( "Select an option: " );
        System.out.println( "1 - Add new user" );
        System.out.println( "2 - Update user details" );
        System.out.println( "3 - Delete user" );
        System.out.println( "4 - Go back" );
        
        switch( scanner.nextInt() ) {
            case 1: 
                scanner.nextLine();
                System.out.println( "Please, type the username: " );
                String username = scanner.nextLine();
                if ( isUsernameValid( username ) ) {
                    System.out.println( "Please, type the password: " );
                    String password = scanner.nextLine();
                    if ( isPasswordValid( password ) ) {
                        User newUser = new User();
                        newUser.setUsername( username );
                        newUser.setPassword( password );
                        
                        System.out.println( "Select the type of user: " );
                        System.out.println("1 - Admin");
                        System.out.println("2 - Office");
                        System.out.println("3 - Lecturer");
                        int chosen = scanner.nextInt();
                        if ( chosen > 0 && chosen <= 3 ) {
                            scanner.nextLine();
                            newUser.setTypeByOption( chosen );
                            if ( chosen == 3 ) {
                                System.out.println( "Type the lecturer unique id: " );
                                int id = scanner.nextInt();
                                String lecturerName = DB_CONNECTOR.findLecturerById( id );
                                if ( lecturerName != null ) {
                                    System.out.println( "Confirm to link this account to lecturer " + lecturerName + " ?" );
                                    System.out.println( "1 - Yes" );
                                    System.out.println( "2 - No" );
                                    chosen = scanner.nextInt();
                                    if ( chosen == 1 ) {
                                        newUser.setLecturerId( id );
                                    } else {
                                        System.err.println( "User not created!" );
                                        displayUserManagementMenu();
                                    }
                                } else {
                                    displayUserManagementMenu();
                                }
                            }
                            saveUser( newUser );
                        }
                    }
                }
                displayUserManagementMenu();
                break;
                
            case 2: 
                scanner.nextLine();
                System.out.println( "Please, type the username of the user you want to update: " );
                username = scanner.nextLine();
                User user = getUserByUsername( username );
                if ( user == null ) {
                    System.err.println( "User not found!" );
                } else {
                    System.out.println( "Type a new username (or the current one if you want to keep it): " );
                    username = scanner.nextLine();
                    if ( isUsernameValid( username ) ) {
                        System.out.println( "Please, type a password: " );
                        String password = scanner.nextLine();
                        if ( isPasswordValid( password ) ) {
                            user.setUsername( username );
                            user.setPassword( password );

                            System.out.println( "Select the type of user: " );
                            System.out.println("1 - Admin");
                            System.out.println("2 - Office");
                            System.out.println("3 - Lecturer");
                            int chosen = scanner.nextInt();
                            if ( chosen > 0 && chosen <= 3 ) {
                                scanner.nextLine();
                                user.setTypeByOption( chosen );
                                if ( chosen == 3 ) {
                                    System.out.println( "Type the lecturer unique id: " );
                                    int id = scanner.nextInt();
                                    String lecturerName = DB_CONNECTOR.findLecturerById( id );
                                    if ( lecturerName != null ) {
                                        System.out.println( "Confirm to link this account to lecturer " + lecturerName + " ?" );
                                        System.out.println( "1 - Yes" );
                                        System.out.println( "2 - No" );
                                        chosen = scanner.nextInt();
                                        if ( chosen == 1 ) {
                                            user.setLecturerId( id );
                                        } else {
                                            System.err.println( "User not updated!" );
                                            displayUserManagementMenu();
                                        }
                                    } else {
                                        displayUserManagementMenu(); 
                                    }
                                }
                                saveUser( user );
                            }
                        }
                    }
                }
                displayUserManagementMenu();
                break;
            case 3:
                scanner.nextLine();
                System.out.println( "Please, type the username of the user you want to delete: " );
                username = scanner.nextLine();
                user = getUserByUsername( username );
                if ( user == null ) {
                    System.err.println( "User not found!" );
                } else {
                    deleteUser( user );
                }
                displayUserManagementMenu();
                break;
            case 4:
                scanner.nextLine();
                displayAdminMenu();
                break;
            default:
                System.err.println( "Invalid option!" );
                displayUserManagementMenu();
        }
    }

    private static void displayOfficeMenu() {
        System.out.print( "Welcome, " );
        System.out.println( loggedUser.getUsername() );
        System.out.println( "-------------------------" );
        System.out.println( "Select an option: " );
        System.out.println("1 - Generate Course Report");
        System.out.println("2 - Generate Student Report");
        System.out.println("3 - Generate Lecturer Report");
        System.out.println("4 - Change username");
        System.out.println("5 - Change password");
        
        switch( scanner.nextInt() ) {
            case 1: 
                scanner.nextLine();
                System.out.println( "Select an output method: " );
                System.out.println( "1 - CSV file" );
                System.out.println( "2 - TXT file" );
                System.out.println( "3 - Console" );
                
                switch ( scanner.nextInt() ) {
                    case 1 :
                        generateCourseReportCSV();
                        displayOfficeMenu();
                        break;
                    case 2 : 
                        generateCourseReportTXT();
                        displayOfficeMenu();
                        break;
                    case 3 : 
                        generateCourseReportTXT( true );
                        displayOfficeMenu();
                        break;
                    default : 
                        System.err.println( "Invalid option!" );
                        displayOfficeMenu();
                        break;
                }
                break;
            case 2: 
                scanner.nextLine();
                System.out.println( "Select an output method: " );
                System.out.println( "1 - CSV file" );
                System.out.println( "2 - TXT file" );
                System.out.println( "3 - Console" );
                
                switch ( scanner.nextInt() ) {
                    case 1 :
                        generateStudentReportCSV();
                        displayOfficeMenu();
                        break;
                    case 2 : 
                        generateStudentReportTXT();
                        displayOfficeMenu();
                        break;
                    case 3 : 
                        generateStudentReportTXT( true );
                        displayOfficeMenu();
                        break;
                    default : 
                        System.err.println( "Invalid option!" );
                        displayOfficeMenu();
                        break;
                }
                break;
            case 3: 
                scanner.nextLine();
                System.out.println( "Select an output method: " );
                System.out.println( "1 - CSV file" );
                System.out.println( "2 - TXT file" );
                System.out.println( "3 - Console" );
                
                switch ( scanner.nextInt() ) {
                    case 1 :
                        generateLecturerReportCSV();
                        displayOfficeMenu();
                        break;
                    case 2 : 
                        generateLecturerReportTXT();
                        displayOfficeMenu();
                        break;
                    case 3 : 
                        generateLecturerReportTXT( true );
                        displayOfficeMenu();
                        break;
                    default : 
                        System.err.println( "Invalid option!" );
                        displayOfficeMenu();
                        break;
                }
                break;
            case 4: 
                scanner.nextLine();
                updateUsernameMenu();
                displayOfficeMenu();
                break;
            case 5:
                scanner.nextLine();
                updatePasswordMenu();
                displayOfficeMenu();
                break;
            default:
                System.err.println( "Invalid option!" );
                displayOfficeMenu();
        }
    }

    private static void displayLecturerMenu() {
        System.out.print( "Welcome, " );
        System.out.println( loggedUser.getUsername() );
        System.out.println( "-------------------------" );
        System.out.println( "Select an option: " );
        System.out.println("1 - Generate report");
        System.out.println("2 - Change username");
        System.out.println("3 - Change password");
        
        switch( scanner.nextInt() ) {
            case 1: 
                scanner.nextLine();
                System.out.println( "Select an output method: " );
                System.out.println( "1 - CSV file" );
                System.out.println( "2 - TXT file" );
                System.out.println( "3 - Console" );
                
                switch ( scanner.nextInt() ) {
                    case 1 :
                        generateLecturerReportCSV( loggedUser.getLecturerId() );
                        displayLecturerMenu();
                        break;
                    case 2 : 
                        generateLecturerReportTXT( loggedUser.getLecturerId() );
                        displayLecturerMenu();
                        break;
                    case 3 : 
                        generateLecturerReportTXT( loggedUser.getLecturerId(), true );
                        displayLecturerMenu();
                        break;
                    default : 
                        System.err.println( "Invalid option!" );
                        displayLecturerMenu();
                        break;
                }
                break;
            case 2: 
                scanner.nextLine();
                updateUsernameMenu();
                displayLecturerMenu();
                break;
            case 3:
                scanner.nextLine();
                updatePasswordMenu();
                displayLecturerMenu();
                break;
            default:
                System.err.println( "Invalid option!" );
                displayLecturerMenu();
        }
    }
    
    private static void generateCourseReportCSV() {
        ArrayList<String> reportData = new ArrayList<>();
        String line = "";
        
        for ( CourseReportDAO dao : DB_CONNECTOR.generateCourseReport() ) {
            line += dao.getProgramme() + ",";
            line += dao.getModule() + ",";
            line += dao.getNumberStudents() + ",";
            line += dao.getLecturerName() + ",";
            line += dao.getRoom();
            reportData.add( line );
            line = "";
        }
        
        fileUtils.generateCSV( 
                COURSE_REPORT_HEADER, 
                reportData, 
                COURSE_REPORT_FILENAME );
    }
    
    private static void generateStudentReportCSV() {
        ArrayList<String> reportData = new ArrayList<>();
        String line = "";
        
        for ( StudentReportDAO dao : DB_CONNECTOR.generateStudentReport() ) {
            line += dao.getStudentName() + ",";
            line += dao.getStudentNumber()+ ",";
            line += dao.getProgramme() + ",";
            line += dao.getModule() + ",";
            line += dao.getStartDate() + ",";
            line += dao.getEndDate() + ",";
            line += dao.getFirstGrade() + ",";
            line += dao.getSecondGrade() + ",";
            line += dao.getThirdGrade() + ",";
            line += dao.hasPassed();
            reportData.add( line );
            line = "";
        }
        
        fileUtils.generateCSV( 
                STUDENT_REPORT_HEADER, 
                reportData, 
                STUDENT_REPORT_FILENAME );
    }
    
    private static void generateLecturerReportCSV() {
        generateLecturerReportCSV( 0 );
    }
    
    private static void generateLecturerReportCSV( int lecturerId ) {
        ArrayList<String> reportData = new ArrayList<>();
        String line = "";
        
        for ( LecturerReportDAO dao : DB_CONNECTOR.generateLecturerReport( lecturerId ) ) {
            line += dao.getLecturerName() + ",";
            line += dao.getRole() + ",";
            line += dao.getModule() + ",";
            line += dao.getNumberStudents();
            reportData.add( line );
            line = "";
        }
        
        fileUtils.generateCSV( 
                LECTURER_REPORT_HEADER, 
                reportData, 
                LECTURER_REPORT_FILENAME );
    }
    
    private static void generateCourseReportTXT() {
        generateCourseReportTXT( false );
    }
    
    private static void generateCourseReportTXT( boolean consoleOnly ) {
        ArrayList<String[]> arrayValues = new ArrayList<>();
                
        for ( CourseReportDAO dao : DB_CONNECTOR.generateCourseReport() ) {
            String[] tempArray = { 
                dao.getProgramme(), 
                dao.getModule(),
                Integer.toString( dao.getNumberStudents() ),
                dao.getLecturerName(),
                dao.getRoom()
            };
            arrayValues.add( tempArray );
        }
        
        fileUtils.generateTXT( 
                COURSE_REPORT_HEADER, 
                arrayValues, 
                COURSE_REPORT_HEADER_FORMAT,
                COURSE_REPORT_FILENAME,
                consoleOnly );
    }
    
    private static void generateStudentReportTXT() {
        generateStudentReportTXT( false );
    }
    
    private static void generateStudentReportTXT( boolean consoleOnly ) {
        ArrayList<String[]> arrayValues = new ArrayList<>();
        
        String pattern = "dd/MM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        
        for ( StudentReportDAO dao : DB_CONNECTOR.generateStudentReport() ) {
            String[] tempArray = { 
                dao.getStudentName(),
                dao.getStudentNumber(),
                dao.getProgramme(),
                dao.getModule(),
                df.format( dao.getStartDate() ),
                df.format( dao.getEndDate() ),
                Double.toString( dao.getFirstGrade() ),
                Double.toString( dao.getSecondGrade() ),
                Double.toString( dao.getThirdGrade() ),
                dao.hasPassed()
            };
            arrayValues.add( tempArray );
        }
        
        fileUtils.generateTXT( 
                STUDENT_REPORT_HEADER, 
                arrayValues, 
                STUDENT_REPORT_HEADER_FORMAT,
                STUDENT_REPORT_FILENAME,
                consoleOnly );
    }
    
    private static void generateLecturerReportTXT() {
        generateLecturerReportTXT( 0, false );
    }

    private static void generateLecturerReportTXT( int lecturerId ) {
        generateLecturerReportTXT( lecturerId, false );
    }
        
    private static void generateLecturerReportTXT( boolean consoleOnly ) {
        generateLecturerReportTXT( 0, consoleOnly );
    }
    
    private static void generateLecturerReportTXT( int lecturerId, boolean consoleOnly ) {
        ArrayList<String[]> arrayValues = new ArrayList<>();
                
        for ( LecturerReportDAO dao : DB_CONNECTOR.generateLecturerReport( lecturerId ) ) {
            String[] tempArray = { 
                dao.getLecturerName(),
                dao.getRole(),
                dao.getModule(),
                Integer.toString( dao.getNumberStudents() )
            };
            arrayValues.add( tempArray );
        }
        
        fileUtils.generateTXT( 
                LECTURER_REPORT_HEADER, 
                arrayValues, 
                LECTURER_REPORT_HEADER_FORMAT,
                LECTURER_REPORT_FILENAME,
                consoleOnly );
    }

    private static void doLogin( String username, String password ) {
        loggedUser = DB_CONNECTOR.login( username, password );
    }
    
    private static User getUserByUsername( String username ) {
        return DB_CONNECTOR.getUserByUsername( username );
    }

    private static void updateUsername( String newUsername ) {
        loggedUser.setUsername( newUsername );
        DB_CONNECTOR.updateUser( loggedUser );
    }

    private static void updatePassword( String newPassword ) {
        loggedUser.setPassword( newPassword );
        DB_CONNECTOR.updateUser( loggedUser );        
    }
    
    private static void saveUser( User user ) {
        if ( user.getId() == 0 ) {
            DB_CONNECTOR.addUser( user );
            System.out.println( "User created!" );
        } else {
            DB_CONNECTOR.updateUser( user );
            System.out.println( "User updated!" );
        }
    }
    
    private static void deleteUser( User user ) {
        DB_CONNECTOR.deleteUser( user );
        System.out.println( "User deleted!" );
    }
}
