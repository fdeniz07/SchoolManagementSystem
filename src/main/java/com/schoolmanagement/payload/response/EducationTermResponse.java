package com.schoolmanagement.payload.response;

import com.schoolmanagement.entity.enums.Term;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class EducationTermResponse implements Serializable {

    private Long id;
    private Term term;
    private LocalDate startDate; //10 Juni 2023
    private LocalDate endDate;  //15 Sept. 2023
    private LocalDate lastRegistrationDate; //30 Mai 2023
}
