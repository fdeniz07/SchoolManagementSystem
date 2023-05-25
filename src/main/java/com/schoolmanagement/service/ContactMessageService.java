package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.ContactMessage;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.schoolmanagement.utils.Messages.ALREADY_SEND_A_MESSAGE_TODAY;

@Service
@RequiredArgsConstructor
//final olan fieldlardan cons olusturur. Eksta bir constructor yazarak kod kalabaliginin önüne gecer

public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    // Not: save() methodu **********************************************************************
    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        // !!! ayni kisi ayni gun icinde sadece 1 defa mesaj gonderebilsin
        boolean isSameMessageWithSameEmailForToday =
                contactMessageRepository.existsByEmailEqualsAndDateEquals(contactMessageRequest.getEmail(), LocalDate.now());

        if (isSameMessageWithSameEmailForToday) {
            throw new ConflictException(String.format(ALREADY_SEND_A_MESSAGE_TODAY)); //static class'in bir field'i cagrilacaksa staticClass.metod yerine
            //class'in import edilmesi, her seferinde sinifin cagrilmasinin önüne gecer
            //ve ilgili field cagrilmis olur

        }
        // ÖDEV --> !!!DTO - POJO dönüsümü

        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(contactMessageRequest.getName());
        contactMessage.setEmail(contactMessageRequest.getEmail());
        contactMessage.setSubject(contactMessageRequest.getSubject());
        contactMessage.setMessage(contactMessageRequest.getMessage());
        contactMessage.setDate(LocalDate.now());
        Object savedData = contactMessageRepository.save(contactMessage);
        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Create Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(createResponse(savedData))
                .build();
    }


}
