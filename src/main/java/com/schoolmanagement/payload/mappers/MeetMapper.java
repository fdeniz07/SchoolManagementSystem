package com.schoolmanagement.payload.mappers;

import com.schoolmanagement.entity.concretes.Meet;
import com.schoolmanagement.payload.response.MeetResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Component
@Data
@RequiredArgsConstructor
public class MeetMapper {

    //POJO --> DTO Dönüsümü
    public MeetResponse createMeetResponse(Meet meet) {
        return MeetResponse.builder()
                .id(meet.getId())
                .date(meet.getDate())
                .startTime(meet.getStartTime())
                .stopTime(meet.getStopTime())
                .description((meet.getDescription()))
                .advisorTeacherId(meet.getAdvisorTeacher().getId())
                .teacherSsn(meet.getAdvisorTeacher().getTeacher().getSsn())
                .teacherName(meet.getAdvisorTeacher().getTeacher().getName())
                .students(meet.getStudentList())
                .build();
    }
}












