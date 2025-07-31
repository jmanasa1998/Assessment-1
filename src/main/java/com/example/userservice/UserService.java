package com.example.userservice;
public interface UserService {
    User createUser(String name, String email);
    User getUser(long id) throws UserNotFoundException;
    User updateEmail(long id, String newEmail) throws UserNotFoundException;
    void deleteUser(long id) throws UserNotFoundException;
}