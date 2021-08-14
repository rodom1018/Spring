package restapi.practice.events;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
@Entity
public class Event {


    @GeneratedValue
    @Id
    private Integer id;

    @NotNull
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;

    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus= EventStatus.DRAFT;

    public void update(){
        //update free
        if(this.basePrice == 0 && this.maxPrice == 0){
            this.free=true;
        }else{
            this.free = false;
        }

        //update offline
        if(this.location == null || this.location.isBlank()){
            this.offline=false;
        }else{
            this.offline = true;
        }
    }
}
