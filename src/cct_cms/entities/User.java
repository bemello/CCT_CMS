/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.entities;

/**
 *
 * @author bemello
 */
public class User {
    
    private int id;
    
    private String username;
        
    private String password;
    
    private String type;
    
    private int lecturerId;

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
    
        public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }
    
    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId( int lecturerId ) {
        this.lecturerId = lecturerId;
    }

    public User() {
    }
    
    public User( int id, String username, String password, String type, int lecturerId ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.lecturerId = lecturerId;
    }

    public boolean isAdmin() {
        return "Admin".equals( this.type );
    }

    public boolean isOffice() {
        return "Office".equals( this.type );
    }

    public boolean isLecturer() {
        return "Lecturer".equals( this.type );
    }
    
    public void setTypeByOption( int option ) {
        if ( option == 1 ) {
            this.type = "Admin";
        } else if ( option == 2 ) {
            this.type = "Office";
        } else {
            this.type = "Lecturer";
        }
    }
}
