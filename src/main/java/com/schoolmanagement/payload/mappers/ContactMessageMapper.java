package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.ContactMessage;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

//@Component //-->Biz IOC container'i bean lerle biz yönetecegiz. CreateObjectBean'e bu classi ekliyoruz
@Data
@RequiredArgsConstructor
public class ContactMessageMapper {

    //!!! DTO - POJO Dönüsümü icin yardimci method
    public ContactMessage createObject(ContactMessageRequest contactMessageRequest) {

        return ContactMessage.builder()
                .name(contactMessageRequest.getName())
                .subject(contactMessageRequest.getSubject())
                .message(contactMessageRequest.getMessage())
                .email(contactMessageRequest.getEmail())
                .date(LocalDate.now())
                .build();
    }

    //!!! POJO - DTO Dönüsümü icin yardimci method
    public ContactMessageResponse createResponse(ContactMessage contactMessage) {

        return ContactMessageResponse.builder()
                .name(contactMessage.getName())
                .subject(contactMessage.getSubject())
                .message(contactMessage.getMessage())
                .email(contactMessage.getEmail())
                .date(LocalDate.now())
                .build();
    }
}



