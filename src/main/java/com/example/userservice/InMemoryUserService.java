package com.example.userservice;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserService implements UserService {
    private final ConcurrentHashMap<Long, User> users;
    private final AtomicLong idGenerator;

    public InMemoryUserService(ConcurrentHashMap<Long, User> store) {
        this.users = store;
        this.idGenerator = new AtomicLong(0);
    }

    @Override
    public User createUser(String name, String email) {
        long id = idGenerator.incrementAndGet();
        User user = new User(id, name, email);
        users.put(id, user);
        return user;
    }

    @Override
    public User getUser(long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null) throw new UserNotFoundException("User not found");
        return user;
    }

    @Override
    public User updateEmail(long id, String newEmail) throws UserNotFoundException {
        User existing = users.get(id);
        if (existing == null) throw new UserNotFoundException("User not found");
        User updated = new User(existing.getId(), existing.getName(), newEmail);
        users.put(id, updated);
        return updated;
    }

    @Override
    public void deleteUser(long id) throws UserNotFoundException {
        if (users.remove(id) == null)
            throw new UserNotFoundException("User not found");
    }
}