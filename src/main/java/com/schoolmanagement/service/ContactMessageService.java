package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.ContactMessage;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.payload.mappers.ContactMessageMapper;
import com.schoolmanagement.payload.request.ContactMessageRequest;
import com.schoolmanagement.payload.response.ContactMessageResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static com.schoolmanagement.utils.Messages.ALREADY_SEND_A_MESSAGE_TODAY;

@Service
@RequiredArgsConstructor //final olan fieldlardan cons olusturur. Eksta bir constructor yazarak kod kalabaliginin önüne gecer
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;

    // Not: save() methodu ********************************************************************************************************************************
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
        ContactMessage contactMessage = contactMessageMapper.createObject(contactMessageRequest);
        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.createResponse(savedData))
                .build();

        /** Benim yaptigim ödev - uzun yol
         ContactMessage contactMessage = new ContactMessage();
         contactMessage.setName(contactMessageRequest.getName());
         contactMessage.setEmail(contactMessageRequest.getEmail());
         contactMessage.setSubject(contactMessageRequest.getSubject());
         contactMessage.setMessage(contactMessageRequest.getMessage());
         contactMessage.setDate(LocalDate.now());
         Object savedData = contactMessageRepository.save(contactMessage);
         return ResponseMessage.<ContactMessageResponse>builder()
         .message("Contact Message Created Successfully")
         .httpStatus(HttpStatus.CREATED)
         .object(createResponse(savedData))
         .build();

         */
    }

    //Not: getAll() methodu ***********************************************************************************************************************************
    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findAll(pageable).map(contactMessageMapper::createResponse);

        //Yukaridaki kodun uzun hali
        //return  contactMessageRepository.findAll(pageable).map(r -> contactMessageMapper.createResponse(r));
    }

    //Not: searchByEmail() *************************************************************************************************************************************
    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findByEmailEquals(email,pageable).map(contactMessageMapper::createResponse); //Repository den bize POJO geldi ve bizde bunu
                                                                                                                             // response dönüstürdük
    }

    //Not: searchBySubject() *************************************************************************************************************************************
    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubjectEquals(subject,pageable).map(contactMessageMapper::createResponse); //Repository den bize POJO geldi ve bizde bunu
                                                                                                                                 // response dönüstürdük
    }
}






















