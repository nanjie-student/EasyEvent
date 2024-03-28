package com.example.easyEvent.type;

import com.example.easyEvent.entity.EventEntity;
import com.example.easyEvent.util.DateUtil;
import lombok.Data;

@Data
public class Event {
    private String id;
    private String title;
    private String description;
    private Float price;
    private String date;
    //help us to understand resolver
    private Integer creatorId;
    //can check which user created this event
    private User creator;
    /**
     * static factory method*/

    public static Event fromEntity(EventEntity eventEntity){
        Event event = new Event();
        event.setId(eventEntity.getId().toString());
        event.setTitle(eventEntity.getTitle());
        event.setDescription(eventEntity.getDescription());
        event.setPrice(eventEntity.getPrice());
        event.setDate(DateUtil.formatDateInISOString(eventEntity.getDate()));
        //enevtEntity数据层实体返回回来，填充到传输层creatorId上
        event.setCreatorId(eventEntity.getCreatorId());
        return event;
    }



}
