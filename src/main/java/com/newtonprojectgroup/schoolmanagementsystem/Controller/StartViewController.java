package com.newtonprojectgroup.schoolmanagementsystem.Controller;

import com.newtonprojectgroup.schoolmanagementsystem.Entity.*;
import com.newtonprojectgroup.schoolmanagementsystem.Repository.iRepositoryGrade;
import com.newtonprojectgroup.schoolmanagementsystem.Repository.iRepositoryStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

///
/// Dernna klassen kommer att tas bort om vi skall använda Spring Securtity
///

@Controller
public class StartViewController {

    private Person user;
    private Credentials credentials;

    @Autowired
    private iRepositoryStudent repositoryStudent;

    @Autowired
    private iRepositoryGrade repositoryGrade;

    private List<Grade> gradeList;
    private List<Student> studentList;


    public StartViewController() {
    }

    @RequestMapping("/greetuser")
    public String greetUser(Model theModel) {

        studentList = repositoryStudent.findAll();
        gradeList = repositoryGrade.findAll();

        for (Student person : studentList) {
            System.out.println("Name: " + person.getFirstName() + " " + person.getLastName());
            System.out.println("Role: " + person.getPersonType().getPersonTypeTitle());
            System.out.println("StudentId: " + person.getPersonId());
            System.out.println("Student semester: " + person.getSemester());
            System.out.println("Enlisted on program " + person.getEnlistedProgram().getProgramName());
            System.out.println("Courses in program: ");
            for (Course course : person.getEnlistedProgram().getCourseList()) {
                System.out.println(course.getCourseName());
            }
            System.out.println("====GRADES===\n");

            for (StudentGrade studentGrade : person.getStudentGrades()) {
                System.out.println(studentGrade.getCourse().getCourseName() + ": " + studentGrade.getGrade().getScore());
            }

            for (Grade grade : gradeList) {
                System.out.println(grade.getScore());
            }
            System.out.println("====END OF PERSON===\n");
        }

        gradeList = repositoryGrade.findAll();

        theModel.addAttribute("theuser", user);

        return "welcome-" + credentials.getUserPermission();
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}