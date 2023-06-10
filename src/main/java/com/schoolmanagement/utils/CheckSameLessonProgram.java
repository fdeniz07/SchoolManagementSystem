package com.schoolmanagement.utils;

import com.schoolmanagement.entity.concretes.LessonProgram;
import com.schoolmanagement.exception.BadRequestException;

import java.util.HashSet;
import java.util.Set;

public class CheckSameLessonProgram {

    public static void checkLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest) {

        //BusinessLogic yapilacak. Örnegin bir ögretmen iki farkli derse giriyor (Türkce-Edebiyat) ayni gün ayni saatte veremez. Bu yüzden bunun kontrolünün yapilmasi lazim.
        //Ayrica günleri ayni ama starttime lari birbiri arasina girerse de bunun kontrolü de yapilacak
        // Eger bir de sinif(konum) olayi olsa idi, bu sinif icin bir class yazip onu da kontrol etmek gerekecekti.ha

        // TODO size degerlerine field lar kontrol edilecek

        if (existLessonProgram.isEmpty() && lessonProgramRequest.size() > 1) {  //>1 yapmamizin nedeni varsa karsilastirmak icin
            checkDuplicateLessonPrograms(lessonProgramRequest);
        } else {
            checkDuplicateLessonPrograms(lessonProgramRequest);
            checkDuplicateLessonPrograms(existLessonProgram, lessonProgramRequest);
        }
    }

    private static void checkDuplicateLessonPrograms(Set<LessonProgram> lessonProgramRequest) {

        Set<String> uniqueLessonProgramKeys = new HashSet<>();
        for (LessonProgram lessonProgram : lessonProgramRequest) {
            String lessonProgramKey = lessonProgram.getDay().name() + lessonProgram.getStartTime();
            if (uniqueLessonProgramKeys.contains(lessonProgramKey)) {
                throw new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
            }
            uniqueLessonProgramKeys.add(lessonProgramKey);
        }
    }

    public static void checkDuplicateLessonPrograms(Set<LessonProgram> existLessonProgram, Set<LessonProgram> lessonProgramRequest) {

        for (LessonProgram requestLessonProgram : lessonProgramRequest) {
            if (existLessonProgram.stream().anyMatch(lessonProgram ->
                    lessonProgram.getStartTime().equals(requestLessonProgram.getStartTime()) && lessonProgram.getDay().name().equals(requestLessonProgram.getDay().name()))) {
                throw new BadRequestException(Messages.LESSON_PROGRAM_EXIST_MESSAGE);
            }
        }
    }
    //TODO : startTime baska bir LessonProgram'in startTime ve endTime arasinda mi kontrolü eklenecek
}
