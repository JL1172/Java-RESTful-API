package com.example.restservice.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.ErrorHandling.UEntityHandler;

@RestController
public class UserController {
    private static List<UserDto> userList = new ArrayList<UserDto>();
    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/create-user")
    public List<UserDto> createUser(@RequestBody UserDto user) {
        if (user.getFullName() == null) {
            throw new UEntityHandler("Full Name Required");
        } else {
            for (UserDto userDto : userList) {
                if (userDto.getFullName().equals(user.getFullName())) {
                    throw new UEntityHandler("User Already Exists");
                }
            }
            UserDto newUser = new UserDto(user.getFullName(), counter.incrementAndGet());
            userList.add(newUser);
        }
        return userList;
    }

    @GetMapping("/fetch-users")
    public List<UserDto> fetchAllUsers() {
        return userList;
    }

    @GetMapping("/fetch-user/:id")
    public UserDto fetchUserById(@RequestParam long id) {
        UserDto userById = null;
        for (UserDto userDto : userList) {
            if (userDto.getId() == id) {
                userById = userDto;
            }
        }
        if (userById == null) {
            throw new UEntityHandler(String.format("User With ID: %s Does Not Exist.", id));
        } else {
            return userById;
        }
    }
}
