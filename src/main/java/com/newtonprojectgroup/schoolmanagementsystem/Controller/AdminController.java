package com.newtonprojectgroup.schoolmanagementsystem.Controller;

import com.newtonprojectgroup.schoolmanagementsystem.Entity.*;
import com.newtonprojectgroup.schoolmanagementsystem.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private iRepositoryCourse repositoryCourse;

    @Autowired
    private iRepositoryStudent repositoryStudent;

    @Autowired
    private iRepositoryPerson repositoryPerson;

    @Autowired
    private iRepositoryProgram  repositoryProgram;

    @Autowired
    private iRepositoryCredentials repositoryCredentials;

    @Autowired
    private iRepositoryAccountRequests repositoryAccountRequests;

    private List<AccountRequest> accountRequestList;
    private List<Student> studentList;



    @RequestMapping("/accountrequests")
    public String administratorStartView(Model theModel) {

        theModel.addAttribute("courseList", repositoryCourse.findAll());
        theModel.addAttribute("studentList", repositoryStudent.findAll());
        theModel.addAttribute("programList", repositoryProgram.findAll());
        theModel.addAttribute("credential", new Credentials());

        accountRequestList = repositoryAccountRequests.findAll();
        theModel.addAttribute("accountRequestsList", accountRequestList);

        for (AccountRequest accountRequest: accountRequestList) {
            System.out.println(accountRequest.getEmail());
        }

        return "admin-view-accountrequests";
    }

    // This method could possibly have ModelAndView returntype instead.
    @RequestMapping("/manageaccess")
    public String setPendingAccountAccess(
            @RequestParam( value = "permission", required = true) String permission,
            @RequestParam( value = "username") String userName, Model theModel) {

        if(permission.equals("DENIED")) {
            System.out.println("Deleting student from queue: " + userName);
            repositoryAccountRequests.deleteById(userName);
        } else {
            AccountRequest requestToSave = repositoryAccountRequests.findById(userName).orElse(null);
            saveAccountRequestAsCredential(requestToSave, permission);
            savePersonAsCorrectPersonType(requestToSave, permission);

            repositoryAccountRequests.deleteById(userName);

        }

        accountRequestList = repositoryAccountRequests.findAll();
        theModel.addAttribute("accountRequestsList", accountRequestList);

        return "admin-view-accountrequests";
    }

    @RequestMapping("/assignstudents")
    public String studentProgram(Model theModel) {

        List<Student> studentList = repositoryStudent.findAll();
        List<Student> unassignedStudentsList = new ArrayList<>();
        List<Program> programList = repositoryProgram.findAll();

        for (Student unassignedStudent : studentList) {
            if(unassignedStudent.getEnlistedProgram() == null) {
                unassignedStudentsList.add(unassignedStudent);
            }
        }

        theModel.addAttribute("unassignedStudents",unassignedStudentsList);
        theModel.addAttribute("programList", programList);

        return "admin-view-student-program";
    }

    @RequestMapping("/saveassignstudents")
    public ModelAndView saveStudentProgram(
            @RequestParam( value = "program") int programId,
            @RequestParam( value = "username") String userName,
            Model theModel) {

        Student student = (Student) repositoryPerson.findById(userName).orElse(null);
        student.setEnlistedProgram(repositoryProgram.findById(programId).orElse(null));
        student.setSemester(1);
        repositoryStudent.save(student);
        return new ModelAndView("redirect:/assignstudents");
    }

    @RequestMapping("/programcourse")
    public String programCourseLoadModel(Model theModel) {

        theModel.addAttribute("courseList", repositoryCourse.findAll());
        theModel.addAttribute("programList", repositoryProgram.findAll());

        return "admin-view-program-courses";
    }

    @RequestMapping("/savecoursetoprogram")
    public ModelAndView saveCourseToProgram(
            @RequestParam( value = "courseId") int courseId,
            @RequestParam( value = "saveProgramId", required = false) Integer saveToProgramId,
            @RequestParam( value = "deleteProgramId", required = false) Integer deletefromProgramId
    ) {

        Course course = repositoryCourse.findById(courseId).orElse(null);
        Program program = new Program();

        if(saveToProgramId != null) {
            program = repositoryProgram.findById(saveToProgramId).orElse(null);
            if(!program.getCourseList().contains(course)) {
                program.getCourseList().add(course);
            }
        } else {
            program = repositoryProgram.findById(deletefromProgramId).orElse(null);
            if(program.getCourseList().contains(course)) {
                program.getCourseList().remove(course);
            }
        }

        repositoryProgram.save(program);

        return new ModelAndView("redirect:/programcourse");
    }

    @RequestMapping("/removeperson")
    public String removePersonFromSystem(Model theModel) {
        theModel.addAttribute("personList", repositoryPerson.findAll());
        return "admin-remove-person";
    }

    @RequestMapping("/removethisperson")
    public ModelAndView removePersonById(@RequestParam(value = "username") String userName) {

        if(userName != null) {
            Person personToRemove = repositoryPerson.findById(userName).orElse(null);
            if(personToRemove != null) {
                repositoryPerson.delete(personToRemove);
            }
        }

        return new ModelAndView("redirect:/removeperson");
    }

    private void savePersonAsCorrectPersonType(AccountRequest requestToSave, String permission) {

        switch (permission) {
            case "ROLE_STUDENT":
                Student student = new Student();
                student.setPersonId(requestToSave.getUserName());
                student.setFirstName(requestToSave.getFirstName());
                student.setLastName(requestToSave.getLastName());
                student.setAdress(requestToSave.getAdress());
                student.setEmail(requestToSave.getEmail());
                student.setPersonalNumber(requestToSave.getPersonalNumber());
                student.setPersonType(requestToSave.getPersonType());
                repositoryStudent.save(student);
                break;
            default:
                // add case for each personType and add the person to the suiting table as I've done with student.
                break;
        }

    }

    private void saveAccountRequestAsCredential(AccountRequest requestToSave, String permission) {
        Credentials newCredentials = new Credentials();
        newCredentials.setUserName(requestToSave.getUserName());
        newCredentials.setPassword(requestToSave.getPassword());
        newCredentials.setUserPermission(permission);
        newCredentials.setEnabled(true);
        repositoryCredentials.save(newCredentials);

    }

}