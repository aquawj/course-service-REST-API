package com.cloudcomputing.fall2018.courseservice.service;

import com.cloudcomputing.fall2018.courseservice.datamodel.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseService {
    static HashMap<String, Course> course_Map = InMemoryDatabase.getCourseDB();

    //get all courses
    public List<Course> getAllCourses(){
        List<Course> list = new ArrayList<>();
        list.addAll(course_Map.values());
        return list;
    }

    //get all courses of a program
    public List<Course> getCoursesByProgram(Program program){
        return program.getCourses();
    }

    // add a course
    public void addCourse(String name, String id, String board, String roster, Student ta, Professor professor) {
        Course course = new Course(name, id, board, roster, ta, professor);
        course_Map.put(id, course);
    }

    public void addCourse(Course course) {
        course_Map.put(course.getId(), course);
    }

    // add a lecture to course
    public void addLectureToCourse(String courseId, Lecture lecture){
        Course course = course_Map.get(courseId);
        course_Map.get(courseId).getLectures().add(lecture);
        course_Map.put(courseId, course);
    }

    // add a student to course
    public void addStudentToCourse(String courseId, Student student){
        Course course = course_Map.get(courseId);
        course_Map.get(courseId).getStudents().add(student);
        course_Map.put(courseId, course);
    }

    //get a course
    public Course getCourse(String courseId){
        return course_Map.get(courseId);
    }


    //delete a course
    public Course deleteCourse(String courseId) {
        Course deletedCourse = course_Map.get(courseId);
        course_Map.remove(deletedCourse);
        return deletedCourse;
    }

    //delete a student in a course
    public Student deleteStudentInCourse(String courseId, long studentId) {
        Student student = StudentService.student_Map.get(studentId);
        Course course = course_Map.get(courseId);
        course.getStudents().remove(student);
        course_Map.put(courseId, course);
        return student;
    }

    //delete a lecture in a course
    public Lecture deleteLectureInCourse(String courseId, int lecId) {
        Lecture lecture = LectureService.lec_Map.get(lecId);
        Course course = course_Map.get(courseId);
        course.getLectures().remove(lecture);
        course_Map.put(courseId, course);
        return lecture;
    }

    //update a course
    public Course updateCourseInformation(String courseId, Course course) {
        course.setId(courseId);
        course_Map.put(courseId, course);
        return course;
    }
}