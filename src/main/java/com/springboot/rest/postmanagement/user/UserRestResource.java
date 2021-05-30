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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserRestResource {

    @Autowired
    private UserDaoService userDaoService;

    @GetMapping (path="/rest/users")
    public List<User> retrieveAllRestUsers() {return userDaoService.findAll();}

    @GetMapping (path="/rest/users/{id}")
    public EntityModel<User> retrieveRestUser(@PathVariable int id) {
        User user = userDaoService.findOne(id);
        if(user == null)
            throw new UserNotFoundException("Id - "+id);

        //HATEOAS to return related links
        EntityModel<User> resource = EntityModel.of(user);
        Link link = WebMvcLinkBuilder.linkTo(
                methodOn(this.getClass()).retrieveAllRestUsers()).withRel("all-users");
        resource.add(link);

        return resource;
    }

    @PostMapping(path = "/rest/users")
    public ResponseEntity<Object> createUser(@Valid UserDto user) {
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
