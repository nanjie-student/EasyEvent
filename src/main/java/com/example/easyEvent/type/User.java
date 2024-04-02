package com.example.easyEvent.type;

import com.example.easyEvent.entity.UserEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private Integer Id;
    private String email;
    private String password;
    private List<Event> createdEvents = new ArrayList<>();
    //add field about adding booking function
    //Initial it's a null list
    private List<Booking> bookings = new ArrayList<>();

    //
    public static User fromEntity(UserEntity userEntity){
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        return user;
    }
}
