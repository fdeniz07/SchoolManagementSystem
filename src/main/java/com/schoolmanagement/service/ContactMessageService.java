package com.schoolmanagement.service;

import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.ContactMessageRepository;
import com.schoolmanagement.utils.Messages.;
import lombok.RequiredArgsConstructor;
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

        if (isSameMessageWithSameEmailForToday)
            throw new ConflictException(String.format(ALREADY_SEND_A_MESSAGE_TODAY)); //static class'in bir field'i cagrilacaksa staticClass.metod yerine
                                                                                      //class'in import edilmesi, her seferinde sinifin cagrilmasinin önüne gecer
                                                                                      //ve ilgili field cagrilmis olur

        // ÖDEV --> !!!DTO - POJO dönüsümü


    }
}
