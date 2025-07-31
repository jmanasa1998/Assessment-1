package com.example.userservice;

import com.example.userservice.InMemoryUserService;
import com.example.userservice.User;
import com.example.userservice.UserNotFoundException;
import com.example.userservice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserServiceTest {
    private UserService service;

    @BeforeEach
    void setup() {
        service = new InMemoryUserService(new ConcurrentHashMap<>());
    }

    @Test
    void testCreateAndRetrieveUser() throws Exception {
        User user = service.createUser("Alice", "alice@example.com");
        assertEquals("Alice", service.getUser(user.getId()).getName());
    }

    @Test
    void testUpdateEmail() throws Exception {
        User user = service.createUser("Bob", "bob@example.com");
        service.updateEmail(user.getId(), "bob@new.com");
        assertEquals("bob@new.com", service.getUser(user.getId()).getEmail());
    }

    @Test
    void testDeleteUser() throws Exception {
        User user = service.createUser("Carol", "carol@example.com");
        service.deleteUser(user.getId());
        assertThrows(UserNotFoundException.class, () -> service.getUser(user.getId()));
    }

    @Test
    void testUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> service.getUser(999));
    }
}
