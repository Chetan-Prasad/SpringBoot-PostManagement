package com.springboot.rest.postmanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserResource {

    @Autowired
    private UserDaoService userDaoService;

    @ModelAttribute("user")
    public UserDto userRegistrationModel() {
        return new UserDto();
    }

    @GetMapping(path = "/register")
    public String showUserRegitration() {
        return "registration";
    }

    @GetMapping(path = "/users")
    public String retrieveAllUsers(Model model) {
        List<User> users = userDaoService.findAll();
        model.addAttribute("users", users);
        return "userspage";
    }

    @GetMapping(path = "/users/{id}")
    public String retrieveUser(@PathVariable int id, Model model) {
        User user = userDaoService.findOne(id);

        if (user == null)
            throw new UserNotFoundException("Id - " + id);

        model.addAttribute("user", user);

        return "userprofilepage";
    }

    @PostMapping(path = "/users")
    public String createUser(@ModelAttribute("user") @Valid UserDto user) {
        User savedUser = userDaoService.save(user);

        return "redirect:/register?success";
    }

    @GetMapping(path = "/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        User user = userDaoService.DeleteById(id);
        if (user == null)
            throw new UserNotFoundException("Id - " + id);
        return "redirect:/users";
    }
}