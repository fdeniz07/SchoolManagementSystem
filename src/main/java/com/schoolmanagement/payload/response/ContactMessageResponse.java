package com.schoolmanagement.payload.response;

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
public class ContactMessageResponse implements Serializable {

    //Datalar, DB den ön tarafa gidecegi icin validasyonlara gerek yoktur. Yaparsak performans kaybi yasariz gereksiz yere.
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDate date;
}
