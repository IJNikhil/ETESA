package com.etesa.app.viewreport;

import com.etesa.app.student.StudentAdapter;

public class ViewReport {
    private String rollNumber;
    private boolean isPresent;

    public ViewReport(String rollNumber, boolean isPresent) {
        this.rollNumber = rollNumber;
        this.isPresent = isPresent;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Override
    public String toString() {
        return "ViewReport{ " +
                "rollNumber='" + rollNumber + '\'' +
                ", isPresent=" + isPresent +
                '}';
    }
}
