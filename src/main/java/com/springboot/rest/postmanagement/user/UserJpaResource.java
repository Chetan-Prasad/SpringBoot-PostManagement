package com.springboot.rest.postmanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class UserJpaResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @ModelAttribute("user")
    public UserDto userRegistrationModel() {
        return new UserDto();
    }

    @ModelAttribute("post")
    public Post userPostModel() {
        return new Post();
    }

    @GetMapping(path = "/jpa/register")
    public String showUserRegitration() {
        return "jparegistration";
    }

    @GetMapping (path = "/jpa/users")
    public String retrieveAllUsers(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        return "usersjpapage";
    }

    @GetMapping (path = "jpa/users/{id}")
    public String retrieveUser(@PathVariable int id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("Id - "+id);

        User returnedUser = user.get();
        model.addAttribute("user", returnedUser);

        return "userprofilepage";
    }

    @GetMapping(path = "/jpa/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return "redirect:/jpa/users";
    }

    @PostMapping(path = "/jpa/users")
    public String createUser(@ModelAttribute("user") @Valid UserDto user) {
        User saveUser = new User();
        saveUser.setName(user.getName());
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsed = format.parse(user.getDate());
            saveUser.setBirthDate(parsed);
        }
        catch (ParseException e){
            Date parsed = new Date();
            saveUser.setBirthDate(parsed);
        }
        userRepository.save(saveUser);
        return "redirect:/register?success";
    }

    @GetMapping (path = "/jpa/users/{id}/posts")
    public String retrieveAllPosts(@PathVariable int id, Model model) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent())
            throw new UserNotFoundException("Id - "+id);

        List<Post> posts = optionalUser.get().getPosts();
        model.addAttribute("posts", posts);

        return "userpostspage";
    }

    @PostMapping (path = "/jpa/users/{id}/posts")
    public String createPost(@PathVariable int id, @ModelAttribute("post") Post post) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent())
            throw new UserNotFoundException("Id - "+id);

        User user = optionalUser.get();
        post.setUser(user);
        postRepository.save(post);

        String url = "redirect:/jpa/users/"+id+"?success";

        return url;
    }
}
