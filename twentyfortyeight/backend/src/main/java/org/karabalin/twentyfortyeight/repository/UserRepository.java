package org.karabalin.twentyfortyeight.repository;

import org.karabalin.twentyfortyeight.repository.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private List<User> users = new ArrayList<>();
    private int idCounter = 0;

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public User create(User user) {
        User newUser = new User(getNextId(), user.getName(), user.getSurname());
        users.add(newUser);
        return newUser;
    }

    public User read(int id) {
        return users.stream().filter(x -> id == x.getId()).findFirst().orElseThrow();
    }

    public User update(User user) {
        Optional<User> userOptional = users.stream().filter(x -> user.getId() == x.getId()).findFirst();
        User userToUpdate = null;
        if (userOptional.isPresent()) {
            userToUpdate = userOptional.get();
            userToUpdate.setName(user.getName());
            userToUpdate.setSurname(user.getSurname());
        } else {
            throw new NoSuchElementException("User to update not found!");
        }
        return userToUpdate;
    }

    public void delete(int id) {
        int indexToDelete = -1;
        for (int i = 0; i < users.size(); ++i) {
            User user = users.get(i);
            if (id == user.getId()) {
                indexToDelete = i;
            }
        }
        if (indexToDelete == -1) {
            throw new NoSuchElementException("User to delete not found!");
        } else {
            users.remove(indexToDelete);
        }
    }

    private int getNextId() {
        return ++idCounter;
    }

}
