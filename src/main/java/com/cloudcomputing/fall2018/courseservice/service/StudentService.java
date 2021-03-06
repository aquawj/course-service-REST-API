package com.cloudcomputing.fall2018.courseservice.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.cloudcomputing.fall2018.courseservice.datamodel.*;

import java.util.List;

public class StudentService {

	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper; 
    DynamoDBQueryExpression<Student> queryExpression;
    DynamoDBScanExpression scanExpression;
	
	public StudentService(){
		dynamoDb = new DynamoDbConnector();
		DynamoDbConnector.init();
		mapper = new DynamoDBMapper(DynamoDbConnector.getClient());
		queryExpression = new DynamoDBQueryExpression<Student>();
	    scanExpression = new DynamoDBScanExpression();
	}

    //get all students
    public List<Student> getAllStudents(){
    	return mapper.scan(Student.class, scanExpression);
    }

    //get students enrolled in a course
//    public List<Student> getStudentsByCourse(String courseId){
//        List<Student> studentList = new ArrayList<>();
//        if(CourseService.course_Map.containsKey(courseId)){
//            Course course = CourseService.course_Map.get(courseId);
//            for(long id : course.getStudents()){
//                studentList.add(student_Map.get(id));
//            }
//        }
//        return studentList;
//    }

    public Student addStudent(Student student) {
        mapper.save(student);
        return student;
    }

    // get a student by ID
    public List<Student> getStudent(String studentId) {
    	List<Student> students = queryStudents(studentId);
		return students;
    }

    //delete a student
    public Student deleteStudent(String studentId) {
    	for(Student s : getStudent(studentId)) {
			mapper.delete(s);
		}
		Student delete = getStudent(studentId).get(0);
		RegisterService registerService = new RegisterService();
		for (String courseId: delete.getEnrolledCourses()) {
			if (delete.getEmail() != null) {
				registerService.unSubscribeTopic("arn:aws:sns:us-west-2:821043062437:" + courseId, delete.getEmail());
			}
		}
		return delete;
    }

    //update a student
    public Student updateStudentInformation(String studentId, Student student) {
    	List<Student> delete = getStudent(studentId);
    	for(Student d : delete) {
    		mapper.delete(d);
    	}
		RegisterService registerService = new RegisterService();
    	student.setStudentId(studentId);
    	for (String courseId: student.getEnrolledCourses()) {
			if (student.getEmail() != null) {
				registerService.subscribeTopic("arn:aws:sns:us-west-2:821043062437:" + courseId, student.getEmail());
			}
		}
		mapper.save(student);
		return student;	
    }
    
    
    private List<Student> queryStudents(String studentId) {
        Student student = new Student();
        student.setStudentId(studentId);
        queryExpression.setHashKeyValues(student);
        queryExpression.withIndexName("studentId-index");
        queryExpression.setConsistentRead(false);
        List<Student> students = mapper.query(Student.class, queryExpression);
        return students;
    }

}
