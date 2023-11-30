package project.smoothsaver.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import project.smoothsaver.entity.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate created;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate edited;

    /*private double credit;*/

    public UserResponse(User user, boolean includeAll) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        /*this.credit = user.getCredit();*/

        if (includeAll) {
            this.created = user.getCreated();
            this.edited = user.getEdited();
        }
    }
}