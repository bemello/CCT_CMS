/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.dao;

/**
 *
 * @author bemello
 */
public class CourseReportDAO {
    
     private String module;
    
    private String programme;
    
    private int numberStudents;
    
    private String lecturerName;
    
    private String room;

    public CourseReportDAO( 
            String module, 
            String programme, 
            int numberStudents, 
            String lecturerName, 
            String room ) {
        this.module = module;
        this.programme = programme;
        this.numberStudents = numberStudents;
        this.lecturerName = lecturerName;
        this.room = room;
    }

    public String getModule() {
        return module;
    }

    public void setModule( String module ) {
        this.module = module;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme( String programme ) {
        this.programme = programme;
    }

    public int getNumberStudents() {
        return numberStudents;
    }

    public void setNumberStudents( int numberStudents ) {
        this.numberStudents = numberStudents;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName( String lecturerName ) {
        this.lecturerName = lecturerName;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom( String room ) {
        this.room = room;
    }
    
}
