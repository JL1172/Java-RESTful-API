package com.example.restservice.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.restservice.ErrorHandling.UEntityHandler;

@RestController
public class UserController {
    private static ArrayList<UserDto> userList = new ArrayList<UserDto>();
    private final AtomicLong counter = new AtomicLong();

    //post request
    @PostMapping("/create-user")
    public ArrayList<UserDto> createUser(@RequestBody UserDto user) {
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

    //get request (fetches all users)
    @GetMapping("/fetch-users")
    public ArrayList<UserDto> fetchAllUsers() {
        return userList;
    }

    //get by id
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

    //put request
    @PutMapping("/update-user/{id}")
    public ArrayList<UserDto> updateUser(@PathVariable long id, @RequestBody UserDto updatedUser) {
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

    // delete request
    @DeleteMapping("/remove-user/{id}")
    public ArrayList<UserDto> removeUserWithId(@PathVariable long id) {
        List<UserDto> userIsValid = userList.stream().filter(n -> n.getId() == id).collect(Collectors.toList());
        if (userIsValid == null) {
            throw new UEntityHandler(String.format("User With ID %s Not Found", id));
        } else {
            int index = -1;
            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getId() == id) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                throw new UEntityHandler("Incorrect ID.");
            } else {
                userList.remove(index);
            }
            return userList;
        }
    }
}
