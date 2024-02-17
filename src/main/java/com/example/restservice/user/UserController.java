package com.example.restservice.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.ErrorHandling.UnprocessableEntityException;
import com.example.restservice.user.dto.UserDto;

@RestController
public class UserController {

    private final AtomicLong counter = new AtomicLong();
    private static List<UserDto> userList = new ArrayList<UserDto>();

    @PostMapping("/user")
    public List<UserDto> createUser(@RequestBody UserDto user) {
        if (user.getFullName() == null && user.getId() == -1) {
            throw new UnprocessableEntityException("Full Name and ID are Required");
        } else {
        UserDto newUser = new UserDto(user.getFullName(), counter.incrementAndGet());
        userList.add(newUser);
        return userList;
        }
    }
    @GetMapping("/user")
    public List<UserDto> getAllUsers() {
        return userList;
    }
}
