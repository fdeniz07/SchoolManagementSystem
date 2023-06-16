package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.AdvisorTeacher;
import com.schoolmanagement.entity.concretes.Meet;
import com.schoolmanagement.entity.concretes.Student;
import com.schoolmanagement.exception.BadRequestException;
import com.schoolmanagement.exception.ConflictException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.MeetMapper;
import com.schoolmanagement.payload.request.MeetRequestWithoutId;
import com.schoolmanagement.payload.response.MeetResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.MeetRepository;
import com.schoolmanagement.repository.StudentRepository;
import com.schoolmanagement.utils.Messages;
import com.schoolmanagement.utils.TimeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetService implements Serializable {

    private final MeetRepository meetRepository;
    private final AdvisorTeacherService advisorTeacherService;
    private final StudentRepository studentRepository;
    private final StudentService studentService;
    private final MeetMapper meetMapper;

    //Not: save() *******************************************************************************************************************************
    public ResponseMessage<MeetResponse> save(String username, MeetRequestWithoutId meetRequest) {

        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME)));

        //!!! Toplanti saat kontrolü
        if (TimeControl.check(meetRequest.getStartTime(), meetRequest.getStopTime()))
            throw new BadRequestException(Messages.TIME_NOT_VALID_MESSAGE);

        //!!! Toplantiya katilacak ögrenciler icin yeni meeting saatlerinde cakisma var mi kontrolü
        for (Long studentId : meetRequest.getStudentIds()) {
            boolean check = studentRepository.existsById(studentId);
            if (!check) throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE));
            checkMeetConflict(studentId, meetRequest.getDate(), meetRequest.getStartTime(), meetRequest.getStopTime());
        }

        //!!! Meet'e katilacak olan Student'ler getiriliyor mu?
        List<Student> students = studentService.getStudentByIds(meetRequest.getStudentIds());

        //!!! Meet nesnesi olusturup ilgili fieldlar setleniyor --> Build kullanmadigimiz icin bu sekilde yazdik
        Meet meet = new Meet();
        meet.setDate(meetRequest.getDate());
        meet.setStartTime(meetRequest.getStartTime());
        meet.setStopTime(meetRequest.getStopTime());
        meet.setStudentList(students);
        meet.setDescription(meetRequest.getDescription());
        meet.setAdvisorTeacher(advisorTeacher);

        //!!! save islemi
        Meet savedMeet = meetRepository.save(meet);

        //!!! Response nesnesi olusturuluyor
        return ResponseMessage.<MeetResponse>builder()
                .message("Meet Saved Successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(meetMapper.createMeetResponse(savedMeet))
                .build();
    }

    private void checkMeetConflict(Long studentId, LocalDate date, LocalTime startTime, LocalTime stopTime) {
        List<Meet> meets = meetRepository.findByStudentList_IdEquals(studentId);
        //TODO : meet size kontrol edilecek
        for (Meet meet : meets) {
            LocalTime existingStartTime = meet.getStartTime();
            LocalTime existingStopTime = meet.getStopTime();

            if (meet.getDate().equals(date) && ((startTime.isAfter(existingStartTime) && startTime.isBefore(existingStopTime)) || //yenisi eskisinin icinde mi
                    (stopTime.isAfter(existingStartTime) && stopTime.isBefore(existingStopTime)) || //yenisinin stopu eskisinin (bas-bitis) arasinda mi
                    (startTime.isBefore(existingStartTime) && stopTime.isAfter(existingStopTime)) || //yenisi eskisini kapsiyor mu
                    (startTime.equals(existingStartTime) && stopTime.equals(existingStopTime)))) { //eskisi ve yenisi ayni zaman diliminde mi
                throw new ConflictException(Messages.MEET_EXIST_MESSAGE);
            }
        }
    }

    //Not: getAll() *******************************************************************************************************************************
    public List<MeetResponse> getAll() {
        return meetRepository.findAll()
                .stream()
                .map(meetMapper::createMeetResponse)
                .collect(Collectors.toList());
    }

    //Not: getMeetById() *******************************************************************************************************************************
    public ResponseMessage<MeetResponse> getMeetById(Long meetId) {

        Meet meet = meetRepository.findById(meetId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(Messages.MEET_NOT_FOUND_MESSAGE, meetId)));

        return ResponseMessage.<MeetResponse>builder()
                .message("Meet Successfully found")
                .httpStatus(HttpStatus.OK)
                .object(meetMapper.createMeetResponse(meet))
                .build();
    }

    //Not: getAllMeetByAdvisorAsPage() *************************************************************************************************************
    public Page<MeetResponse> getAllMeetByAdvisorTeacherAsPage(String username, Pageable pageable) {

        AdvisorTeacher advisorTeacher = advisorTeacherService.getAdvisorTeacherByUsername(username).orElseThrow(()->
        new ResourceNotFoundException(String.format(Messages.NOT_FOUND_ADVISOR_MESSAGE_WITH_USERNAME)));

        return meetRepository.findByAdvisorTeacher_IdEquals(advisorTeacher.getId(), pageable) // advisorTeacher.getMeet()
                .map(meetMapper::createMeetResponse);
    }
}

























