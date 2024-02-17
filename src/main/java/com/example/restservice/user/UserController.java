package com.example.restservice.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/fetch-user/{id}")
    public UserDto fetchUserById(@PathVariable long id) {
        UserDto userById = null;
        for (UserDto userDto : userList) {
            if (userDto.getId() == id) {
                userById = userDto;
                break;
            }
        }
        if (userById == null) {
            throw new UEntityHandler(String.format("User With ID: %s Does Not Exist.", id));
        } else {
            return userById;
        }
    }

    @PutMapping("/update-user/{id}")
    public List<UserDto> updateUser(@PathVariable long id, @RequestBody UserDto updatedUser) {
        if (updatedUser.getFullName() == null || updatedUser.getId() == -1) {
            throw new UEntityHandler("Full Name and ID Required.");
        }
        UserDto userFound = null;
        for (UserDto userDto: userList) {
            if (userDto.getId() == id) {
                userFound = userDto;
                break;
            }
        }
        if (userFound == null) {
            throw new UEntityHandler(String.format("User With ID %s does not exist", id));
        } else {
            if (updatedUser.getId() != userFound.getId()) {
                throw new UEntityHandler("Incorrect ID In Payload.");
            } else {
                for (UserDto userToUpdate: userList) {
                    if (userToUpdate.getId() == id && userToUpdate.getId() == updatedUser.getId()) {
                        userToUpdate.setFullName(updatedUser.getFullName());
                        userToUpdate.setId(updatedUser.getId());
                        break;
                    }
                }
            }
            return userList;
        }
    }
}
