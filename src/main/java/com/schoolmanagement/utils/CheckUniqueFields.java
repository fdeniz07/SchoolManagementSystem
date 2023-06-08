package com.schoolmanagement.utils;

import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component //DI icin eklendi
@RequiredArgsConstructor
public class CheckUniqueFields {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final ViceDeanRepository viceDeanRepository;
    private final DeanRepository deanRepository;
    private final TeacherRepository teacherRepository;
    private final GuestUserRepository guestUserRepository;

    public void checkDuplicate(String... values) { //varargs kullanimi sayesinde istedigimiz kadar parametre girebiliriz
        String parameter1 = values[0];
        String parameter2 = values[1];
        String parameter3 = values[2];
        String parameter4 = "";

        if (values.length == 4) {
            parameter4 = values[3];
        }

        if (adminRepository.existsByUsername(parameter1) || deanRepository.existsByUsername(parameter1) ||
                studentRepository.existsByUsername(parameter1) || teacherRepository.existsByUsername(parameter1) ||
                viceDeanRepository.existsByUsername(parameter1) || guestUserRepository.existsByUsername(parameter1)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME, parameter1));
        } else if (adminRepository.existsBySsn(parameter2) || deanRepository.existsBySsn(parameter2) ||
                studentRepository.existsBySsn(parameter2) || teacherRepository.existsBySsn(parameter2) ||
                viceDeanRepository.existsBySsn(parameter2) || guestUserRepository.existsBySsn(parameter2)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_SSN, parameter2));
        } else if (adminRepository.existsByPhoneNumber(parameter3) || deanRepository.existsByPhoneNumber(parameter3) ||
                studentRepository.existsByPhoneNumber(parameter3) || teacherRepository.existsByPhoneNumber(parameter3) ||
                viceDeanRepository.existsByPhoneNumber(parameter3) || guestUserRepository.existsByPhoneNumber(parameter3)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, parameter3));
        } else if (studentRepository.existsByEmail(parameter4) || teacherRepository.existsByEmail(parameter4)) {
            throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_EMAIL, parameter4));
        }

        /** Vararrgs kullanimi olmadan yazmak istersek
         *
         *  public void checkDuplicate(String username, String ssn, String phone) {
         *         if (adminRepository.existsByUsername(username) ||
         *                 deanRepository.existsByUsername(username) ||
         *                 studentRepository.existsByUsername(username) ||
         *                 teacherRepository.existsByUsername(username) ||
         *                 viceDeanRepository.existsByUsername(username) ||
         *                 guestUserRepository.existsByUsername(username)) {
         *             throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
         *         } else if (adminRepository.existsBySsn(ssn) ||
         *                 deanRepository.existsBySsn(ssn) ||
         *                 studentRepository.existsBySsn(ssn) ||
         *                 teacherRepository.existsBySsn(ssn) ||
         *                 viceDeanRepository.existsBySsn(ssn) ||
         *                 guestUserRepository.existsBySsn(ssn)) {
         *             throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
         *         } else if (adminRepository.existsByPhoneNumber(phone) ||
         *                 deanRepository.existsByPhoneNumber(phone) ||
         *                 studentRepository.existsByPhoneNumber(phone) ||
         *                 teacherRepository.existsByPhoneNumber(phone) ||
         *                 viceDeanRepository.existsByPhoneNumber(phone) ||
         *                 guestUserRepository.existsByPhoneNumber(phone)) {
         *             throw new ConflictException(String.format(Messages.ALREADY_REGISTER_MESSAGE_PHONE_NUMBER, phone));
         *         }
         * }
         */

    }
}