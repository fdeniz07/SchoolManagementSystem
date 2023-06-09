package com.schoolmanagement.payload.request;

import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TeacherRequest extends BaseUserRequest {

    @NotNull(message = "Please select lesson")
    private Set<Long> lessonsIdList;

    @NotNull(message = "Please select isAdvisor Teacher")
    private boolean isAdvisorTeacher; //Lombok Boolean türündeki wrapper class larda is... ile baslayan kelimelerin getter'inde sorun yasiyor.

    @NotNull(message = "Please enter your email")
    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 50, message = "Your email should be between 5 and 50 chars")
    private String email;
}


















