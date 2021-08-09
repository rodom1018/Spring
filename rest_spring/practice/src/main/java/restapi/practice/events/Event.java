package restapi.practice.events;

import lombok.Builder;

@Builder
public class Event {

    private String name;
    private String description;
}
