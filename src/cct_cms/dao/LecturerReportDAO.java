/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.dao;

/**
 *
 * @author bemello
 */
public class LecturerReportDAO {
    
    private String lecturerName;
    
    private String role;
    
    private String module;
    
    private int numberStudents;

    public LecturerReportDAO( String lecturerName, String role, String module, int numberStudents ) {
        this.lecturerName = lecturerName;
        this.role = role;
        this.module = module;
        this.numberStudents = numberStudents;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName( String lecturerName ) {
        this.lecturerName = lecturerName;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public String getModule() {
        return module;
    }

    public void setModule( String module ) {
        this.module = module;
    }

    public int getNumberStudents() {
        return numberStudents;
    }

    public void setNumberStudents( int numberStudents ) {
        this.numberStudents = numberStudents;
    }
    
}
