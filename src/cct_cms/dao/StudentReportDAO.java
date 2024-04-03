/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cct_cms.dao;

import java.util.Date;

/**
 *
 * @author bemello
 */
public class StudentReportDAO {

    private String studentName;

    private String studentNumber;

    private String programme;

    private String module;

    private Date startDate;

    private Date endDate;

    private double firstGrade;

    private double secondGrade;

    private double thirdGrade;

    public StudentReportDAO( String studentName, String studentNumber, 
            String programme, String module, Date startDate, Date endDate, 
            double firstGrade, double secondGrade, double thirdGrade ) {
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.programme = programme;
        this.module = module;
        this.startDate = startDate;
        this.endDate = endDate;
        this.firstGrade = firstGrade;
        this.secondGrade = secondGrade;
        this.thirdGrade = thirdGrade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName( String studentName ) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber( String studentNumber ) {
        this.studentNumber = studentNumber;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme( String programme ) {
        this.programme = programme;
    }

    public String getModule() {
        return module;
    }

    public void setModule( String module ) {
        this.module = module;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }

    public double getFirstGrade() {
        return firstGrade;
    }

    public void setFirstGrade( double firstGrade ) {
        this.firstGrade = firstGrade;
    }

    public double getSecondGrade() {
        return secondGrade;
    }

    public void setSecondGrade( double secondGrade ) {
        this.secondGrade = secondGrade;
    }

    public double getThirdGrade() {
        return thirdGrade;
    }

    public void setThirdGrade( double thirdGrade ) {
        this.thirdGrade = thirdGrade;
    }
    
    public String hasPassed() {
        String result = "No";
        if ( ( getFirstGrade() + getSecondGrade() + getThirdGrade() ) / 3 >= 40 ) {
            result = "Yes";
        }
        return result;
    }

}
