package com.example.easyEvent.custom;

import com.example.easyEvent.entity.UserEntity;
import com.example.easyEvent.mapper.UserEntityMapper;
import com.example.easyEvent.util.TokenUtil;
import com.netflix.graphql.dgs.context.DgsCustomContextBuilderWithRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
@Slf4j
public class AuthContextBuilder implements DgsCustomContextBuilderWithRequest {

    private final UserEntityMapper userEntityMapper;

    static final String AUTHORIZATION_HEADER = "Authorization";

    public AuthContextBuilder(UserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Object build(@Nullable Map map, @Nullable HttpHeaders httpHeaders, @Nullable WebRequest webRequest) {
        log.info("Building Auth Context...");
        AuthContext authContext = new AuthContext();
        if(!httpHeaders.containsKey(AUTHORIZATION_HEADER)){
            log.info("This user doesn't verification");
            return authContext;
        }
        //If get the Authorization
        String authorization = httpHeaders.getFirst(AUTHORIZATION_HEADER);
        String token = authorization.replace("Bearer","");
        Integer userID;
        try {
            userID = TokenUtil.verifyToken(token);
        }catch(Exception ex){
            authContext.setTokenInvalid(true);
            return authContext;
        }
        UserEntity userEntity = userEntityMapper.selectById(userID);
        if(userEntity == null){
            authContext.setTokenInvalid(true);
            return authContext;
        }
        authContext.setUserEntity(userEntity);
        log.info("User authentication successful,userId = {}",userID);
        return authContext;
    }

}
