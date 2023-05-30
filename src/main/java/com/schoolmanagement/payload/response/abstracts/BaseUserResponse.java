package com.schoolmanagement.payload.response.abstracts;

import com.schoolmanagement.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
//@MappedSuperclass --> response de gereksiz bir annotation
public abstract class BaseUserResponse {

    private Long userId;

    private String username;

    private String ssn;

    private String name;

    private String surname;

    private LocalDate birthDay;

    private String birthPlace;

    private String phoneNumber;

    private Gender gender;

    //!!! Not: Burada UserRole ve Password field(alan)larinin User base class'inda Write_Only olarak erisimi belirtildigi icin veri akisi Client-->DB yönündedir.
    // Bu nedenle hem güvenlik önlemi alinmis, hem de özel bilgilerin client tarafina gönderilmesi engellenmis olmaktadir.

}
