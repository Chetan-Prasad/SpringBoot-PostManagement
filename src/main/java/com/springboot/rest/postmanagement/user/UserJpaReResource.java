package com.springboot.rest.postmanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaReResource {

    @Autowired
    private UserRepository userRepository;

    @GetMapping (path = "/jpa/rest/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping (path = "/jpa/rest/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            throw new UserNotFoundException("Id - "+id);

        //HATEOAS to return related links
        User returnedUser = user.get();
        EntityModel<User> resource = EntityModel.of(returnedUser);
        Link link = WebMvcLinkBuilder.linkTo(
                methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
        resource.add(link);
        return resource;
    }

    @PostMapping(path = "/jpa/rest/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
