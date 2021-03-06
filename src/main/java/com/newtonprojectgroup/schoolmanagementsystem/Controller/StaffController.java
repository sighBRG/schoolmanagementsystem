package com.newtonprojectgroup.schoolmanagementsystem.Controller;

import com.newtonprojectgroup.schoolmanagementsystem.Entity.Person;
import com.newtonprojectgroup.schoolmanagementsystem.Entity.Program;
import com.newtonprojectgroup.schoolmanagementsystem.Entity.Student;
import com.newtonprojectgroup.schoolmanagementsystem.Repository.iRepositoryPerson;
import com.newtonprojectgroup.schoolmanagementsystem.Repository.iRepositoryProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private iRepositoryPerson repositoryPerson;

    @Autowired
    private iRepositoryProgram repositoryProgram;

    public StaffController() {
    }

    @RequestMapping("/")
    public String staffView(Principal principal, Model theModel) {


        List<Program> programs = repositoryProgram.findAll();
        Person person = repositoryPerson.findById(principal.getName()).orElse(null);

        theModel.addAttribute("programs", programs);
        theModel.addAttribute("Person", person);


        return "faculty-view";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateInformation(Principal principal,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("adress") String adress,
            @RequestParam("email") String email) {

        Person personFromDB = repositoryPerson.findById(principal.getName()).orElse(null);

        assert personFromDB != null;
        personFromDB.setFirstName(firstName);
        personFromDB.setLastName(lastName);
        personFromDB.setAdress(adress);
        personFromDB.setEmail(email);

        repositoryPerson.save(personFromDB);

        return new ModelAndView("redirect:/staff/");
    }

    @RequestMapping("/chosenprogram")
    public String chosenProgram(Principal principal, Model theModel,
            @RequestParam(value = "program") int chosenProgramId) {

        Person person = repositoryPerson.findById(principal.getName()).orElse(null);
        List<Program> programs = repositoryProgram.findAll();
        Program program = repositoryProgram.findById(chosenProgramId).orElse(null);
        assert program != null;
        List<Student> studentList = program.getStudentList();

        theModel.addAttribute("programTitle", program.getProgramName());
        theModel.addAttribute("programs", programs);
        theModel.addAttribute("chosenProgram", program);
        theModel.addAttribute("studentList", studentList);
        theModel.addAttribute("Person", person);

        return "faculty-view";
    }
}