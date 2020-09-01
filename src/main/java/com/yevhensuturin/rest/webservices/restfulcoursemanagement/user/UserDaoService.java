package com.yevhensuturin.rest.webservices.restfulcoursemanagement.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static Long currentUserID = 3L;

    static {
        users.add(new User(1L, "Adam", new Date()));
        users.add(new User(2L, "Eve", new Date()));
        users.add(new User(3L, "Jack", new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if(user.getId() == null ) {
            user.setId(++currentUserID);
        }
        users.add(user);
        return user;
    }

    private User findBy(Predicate<User> criteria){
        Iterator<User> userIterator = users.iterator();
        while(userIterator.hasNext()){
            User currUser = userIterator.next();
            if(criteria.test(currUser)){
                return currUser;
            }
        }
        return null;
    }

    public User findByID(int id){
        return findBy(user -> user.getId() == id);
    }

    public User deleteByID(int id){
        User userToDelete = findByID(id);
        if(userToDelete != null) {
            User result = userToDelete;
            users.remove(userToDelete);
            return result;
        }
        return null;
    }
}
