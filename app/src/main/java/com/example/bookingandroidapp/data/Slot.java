package com.example.bookingandroidapp.data;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Slot implements Serializable {
    public String TeacherName;
    public String TeacherSurname;
    public String SubjectName;
    public Time StartTime;
    public Time EndTime;
    public String WeekDate;
    public int TeacherId;
    public int SlotId;
    public Integer BookingId;
    public String UserId;

    public Slot(String teacherName, String teacherSurname, String subjectName, Time startTime, Time endTime, String weekDate, int teacherId, int slotId, Integer bookingId, String userId) {
        TeacherName = teacherName;
        TeacherSurname = teacherSurname;
        SubjectName = subjectName;
        StartTime = startTime;
        EndTime = endTime;
        WeekDate = weekDate;
        TeacherId = teacherId;
        SlotId = slotId;
        BookingId = bookingId;
        UserId = userId;
    }

    public String toString(){
        return "Prof = " + TeacherName + " " + TeacherSurname + ", Materia = " + SubjectName;
    }
}

