package com.example.easyEvent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.easyEvent.entity.EventEntity;
import com.example.easyEvent.entity.UserEntity;
import com.example.easyEvent.mapper.EventEntityMapper;
import com.example.easyEvent.mapper.UserEntityMapper;
import com.example.easyEvent.type.*;
import com.example.easyEvent.util.TokenUtil;
import com.netflix.graphql.dgs.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
@Slf4j
public class UserDataFetcher {

    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final EventEntityMapper eventEntityMapper;

    public UserDataFetcher(UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder,
                           EventEntityMapper eventEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventEntityMapper = eventEntityMapper;
    }
    @DgsQuery
    public List<User> users(){
        List<UserEntity> userEntityList = userEntityMapper.selectList(null);
        List<User> userList = userEntityList.stream()
                .map(User::fromEntity)
                .collect(Collectors.toList());
        return userList;
    }
    @DgsQuery
    public AuthData login(@InputArgument LoginInput loginInput){
        UserEntity userEntity = this.findUserByEmail(loginInput.getEmail());
        if(userEntity == null){
            throw new RuntimeException("this email address doesn't exist");
        }
        boolean match = passwordEncoder.matches(loginInput.getPassword(),userEntity.getPassword());
        if(!match){
            throw new RuntimeException("password doesn't right");
        }
        String token = TokenUtil.signToken(userEntity.getId(),1);
        AuthData authData = new AuthData()
                .setUserId(userEntity.getId())
                .setToken(token)
                .setTokenExpiration(1);
        return authData;
    }
    @DgsMutation
    public User createUser(@InputArgument UserInput userInput){
        //Make sure this user doesn't exist
        ensureUserNotExists(userInput);
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setEmail(userInput.getEmail());
        //Password needs to be stored encrypted
        newUserEntity.setPassword(passwordEncoder.encode(userInput.getPassword()));
        userEntityMapper.insert(newUserEntity);

        User newUser = User.fromEntity(newUserEntity);
        newUser.setPassword(null);
        return newUser;
    }
    @DgsData(parentType = "User",field ="createdEvents")
    public List<Event> createdEvents(DgsDataFetchingEnvironment dfe){
        User user = dfe.getSource();
        QueryWrapper<EventEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EventEntity::getCreatorId,user.getId());
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(queryWrapper);
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEntity)
                .collect(Collectors.toList());
        return eventList;
    }
    //when create a user email should check its exist
    private void ensureUserNotExists(UserInput userInput){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(UserEntity::getEmail,userInput.getEmail());
        if(userEntityMapper.selectCount(queryWrapper) >= 1){
            throw new RuntimeException("This email has existed");
        }
    }
    //function: find User by email, if UserEntity has
    private UserEntity findUserByEmail(String email){
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getEmail, email);
        return userEntityMapper.selectOne(queryWrapper);
    }
}
