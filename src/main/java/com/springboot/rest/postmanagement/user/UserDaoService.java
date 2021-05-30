package com.springboot.rest.postmanagement.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {

    private static List<User> users = new ArrayList<>();
    private static int userCount = 3;

    static {
        users.add(new User(1, "Asuna", new Date()));
        users.add(new User(2, "Mai", new Date()));
        users.add(new User(3, "Misaki", new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(UserDto userin) {
        User user = new User();
        if(user.getId() == null) {
            user.setId(++userCount);
        }
        user.setName(userin.getName());
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date parsed = format.parse(userin.getDate());
            user.setBirthDate(parsed);
        }
        catch (ParseException e){
            Date parsed = new Date();
            user.setBirthDate(parsed);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for(User user: users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User DeleteById(int id) {
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()) {
            User user = iterator.next();
            if(user.getId() == id) {
                iterator.remove();
                return user;
            }
        }
        return null;
    }

}
