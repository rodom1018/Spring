package restapi.practice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import restapi.practice.common.RestDocsConfiguration;
import restapi.practice.common.TestDescription;

import java.time.LocalDateTime;
import java.util.stream.IntStream;


import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {


    @Autowired
    EventRepository eventRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @TestDescription("??????????????? ???????????? ???????????? ?????????")
    public void createEvent() throws Exception{
        //????????? ???(id) ???????????? ???????????? ????????????.

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
                .location("?????????")
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8) // ?????? ?????? ???????????? ?????? json??? ??????????????????
                        .accept(MediaTypes.HAL_JSON) // ?????? ??????2 , ?????? hal_json??? ?????????
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event")

                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),

                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content_type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event")


                        )
                ));

    }


    @Test
    @TestDescription("?????? ?????? ??? ?????? ???????????? ???????????? ?????????")
    public void createEvent_bad_request() throws Exception{
        //????????? ??? ???????????? ???????????? bad request ??????

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
                .location("?????????")
                .build();

        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8) // ?????? ?????? ???????????? ?????? json??? ??????????????????
                        .accept(MediaTypes.HAL_JSON) // ?????? ??????2 , ?????? hal_json??? ?????????
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("bad request")
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
    @TestDescription("?????? ??? ???????????? ??????!")
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
                .location("?????????")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists());
    }

    @Test
    @TestDescription("30?????? ???????????? 10?????? ????????? ????????? ????????????")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0,30).forEach(this::generateEvent);

        //when
        this.mockMvc.perform(get("/api/events")
                        .param("page","3")
                        .param("size","10")
                        .param("sort", "name, DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }




    @Test
    @TestDescription("???????????? ??????????????? ????????????")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .id(index)
                .name("event"+index)
                .description("test event")
                .build();

        return this.eventRepository.save(event);
    }
}
