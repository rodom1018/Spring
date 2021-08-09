package restapi.practice.events;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder().build();
        Assertions.assertThat(event).isNotNull();

    }

}