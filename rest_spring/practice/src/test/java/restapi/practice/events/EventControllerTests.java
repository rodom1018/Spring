package restapi.practice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import restapi.practice.common.TestDescription;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception{
        //이상한 값(id) 같은것이 들어가면 무시한다.

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11,22,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11,23,14,21))
                .beginEventDateTime(LocalDateTime.of(2018, 11,24,14,21))
                .endEventDateTime(LocalDateTime.of(2018, 11,28,23,40))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("노원역")
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문 내용 서술하는 부분 json을 서술하고있다
                        .accept(MediaTypes.HAL_JSON) // 본문 내용2 , 나는 hal_json을 원한다
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists());

    }


    @Test
    @TestDescription("입력 받을 수 없는 이벤트를 생성하는 테스트")
    public void createEvent_bad_request() throws Exception{
        //이상한 값 같은것이 들어가면 bad request 출력

        Event event = Event.builder()
                .name("Spring")
                .id(100)
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11,25,14,21))
                .beginEventDateTime(LocalDateTime.of(2018, 11,10,14,21))
                .endEventDateTime(LocalDateTime.of(2018, 11,20,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("노원역")
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8) // 본문 내용 서술하는 부분 json을 서술하고있다
                        .accept(MediaTypes.HAL_JSON) // 본문 내용2 , 나는 hal_json을 원한다
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("빈 request")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name(null)
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @TestDescription("틀린 값 넣었을시 오류!")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {

        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2020, 12,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2010, 11,25,14,21))
                .beginEventDateTime(LocalDateTime.of(2020, 12,10,14,21))
                .endEventDateTime(LocalDateTime.of(2010, 1,20,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("노원역")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists());
    }
}
