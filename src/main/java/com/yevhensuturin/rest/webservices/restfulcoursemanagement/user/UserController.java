package com.yevhensuturin.rest.webservices.restfulcoursemanagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    UserDaoService userDaoService;

    @Autowired
    UserJPARepository userJPARepository;

    @GetMapping("/jpa/users")
    public List<User> getJPAUsers(){
        return userJPARepository.findAll();
    }

    @GetMapping("/jpa/users/{id}")
    public EntityModel getJPAUseById(@PathVariable Long id){
        Optional<User> user = userJPARepository.findById(id);

        if (! user.isPresent()){
            throw new UserNotFoundException("id ="+id);
        }

        EntityModel resource = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getJPAUsers());
        resource.add(linkTo.withRel("all-JPA-users"));
        return resource;
    }

    @PostMapping(path = "/jpa/users")
    public ResponseEntity<Object> createJPAUser(@Valid @RequestBody User user){
        User savedUser = userJPARepository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/jpa/users/{id}")
    public ResponseEntity<Object> deleteJPAUser(@PathVariable Long id){

        Optional<User> deletedUser = userJPARepository.findById(id);
        if(!deletedUser.isPresent()){
            throw new UserNotFoundException("id -"+id);
        }

        userJPARepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping(path="/users")
    public List<User> getAllUsers(){
        return userDaoService.findAll();
    }

    @GetMapping(path = "users/{id}")
    public EntityModel<User> getUserById(@PathVariable int id){
        User user = userDaoService.findByID(id);
        if(user == null){
            throw new UserNotFoundException("id - "+id);
        }

        //"all-users", SERVER_PATH + "/users"
        //retrieveAllUsers
        EntityModel<User> resource = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllUsers());
        resource.add(linkTo.withRel("all-users"));

        return resource;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
        User savedUser = userDaoService.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id){
        User deletedUser = userDaoService.deleteByID(id);
        if(deletedUser == null){
            throw new UserNotFoundException("id -"+id);
        }


        return ResponseEntity.noContent().build();
    }
}
