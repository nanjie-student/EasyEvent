package com.example.easyEvent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.easyEvent.custom.AuthContext;
import com.example.easyEvent.entity.EventEntity;
import com.example.easyEvent.entity.UserEntity;
import com.example.easyEvent.mapper.EventEntityMapper;
import com.example.easyEvent.mapper.UserEntityMapper;
import com.example.easyEvent.type.Event;
import com.example.easyEvent.type.EventInput;
import com.example.easyEvent.type.User;
import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.context.DgsContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@DgsComponent
@RequiredArgsConstructor
public class EventDataFetcher {

    private final EventEntityMapper eventEntityMapper;
    private final UserEntityMapper userEntityMapper;


    @DgsQuery
    public List<Event> events(){
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEntity).collect(Collectors.toList());

        return eventList;

    }
    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input, DataFetchingEnvironment dfe){
        //when user authentication successful then we should create events
        AuthContext authContext = DgsContext.getCustomContext(dfe);
        authContext.ensureAuthenticated();
        EventEntity newEventEntity = EventEntity.fromEventInput(input);
        newEventEntity.setCreatorId(authContext.getUserEntity().getId());
        eventEntityMapper.insert(newEventEntity);
        Event newEvent = Event.fromEntity(newEventEntity);
        return newEvent;

    }

//    private void populateWithUser(Event event, Integer userId){
//        //By userId can query this user's info
//        UserEntity userEntity = userEntityMapper.selectById(userId);
//        User user = User.fromEntity(userEntity);
//        event.setCreator(user);
//    }
    @DgsData(parentType="Event", field = "creator")
    public  User creator(DgsDataFetchingEnvironment dfe){
        Event event = dfe.getSource();
        UserEntity userEntity = userEntityMapper.selectById(event.getCreatorId());
        User user = User.fromEntity(userEntity);
        return user;
    }

}
