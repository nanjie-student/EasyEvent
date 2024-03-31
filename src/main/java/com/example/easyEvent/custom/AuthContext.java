package com.example.easyEvent.custom;

import com.example.easyEvent.entity.UserEntity;
import lombok.Data;

@Data
public class AuthContext {
    private UserEntity userEntity;
    //variable tokenInvalid mean the token invalid
    private boolean tokenInvalid;

    //this method means the user has been authenticated
    public void ensureAuthenticated(){
        if(tokenInvalid) throw new RuntimeException("The token invalid");
        if(userEntity == null){
            throw new RuntimeException("Not logged in, please log in!");
        }
    }
}
