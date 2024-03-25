package com.example.easyEvent.fetcher;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.easyEvent.entity.EventEntity;
import com.example.easyEvent.mapper.EventEntityMapper;
import com.example.easyEvent.type.Event;
import com.example.easyEvent.type.EventInput;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.stream.Collectors;

@DgsComponent
public class eventDataFetcher {

    private final EventEntityMapper eventEntityMapper;

    public eventDataFetcher(EventEntityMapper eventEntityMapper) {
        this.eventEntityMapper = eventEntityMapper;
    }

    @DgsQuery
    public List<Event> events(){
        List<EventEntity> eventEntityList = eventEntityMapper.selectList(new QueryWrapper<>());
        List<Event> eventList = eventEntityList.stream()
                .map(Event::fromEntity).collect(Collectors.toList());

        return eventList;

    }
    @DgsMutation
    public Event createEvent(@InputArgument(name = "eventInput") EventInput input){
        EventEntity eventEntity = EventEntity.fromEventInput(input);
        eventEntityMapper.insert(eventEntity);
        return Event.fromEntity(eventEntity);

    }

}
