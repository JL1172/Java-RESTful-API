package com.example.restservice.user.dto;

public class UserDto {
    private String fullName;
    private long id = -1;

    public UserDto() {
    }

    public UserDto(String fullName, long id) {
        this.fullName = fullName;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}