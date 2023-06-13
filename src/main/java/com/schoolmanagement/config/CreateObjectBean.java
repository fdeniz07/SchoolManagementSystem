package com.schoolmanagement.config;

import com.schoolmanagement.payload.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //Custom Metodlar üzerinden metotlar olusturup Bean'leri SpringBoot'a göndermis oluyoruz
public class CreateObjectBean {

    //Tek class üzerinden Bean'leri kontrol etmemizi sagliyoruz. Diger türlü @Component annotation'u yazmamiz gerekli
    @Bean
    public DeanMapper deanMapper() {
        return new DeanMapper();
    }

    @Bean
    public ViceDeanMapper viceDeanMapper() {
        return new ViceDeanMapper();
    }

    @Bean
    public AdminMapper adminMapper(){
        return new AdminMapper();
    }

    @Bean
    public EducationTermMapper educationTermMapper(){
        return new EducationTermMapper();
    }

    @Bean
    public LessonProgramMapper lessonProgramMapper() {
        return new LessonProgramMapper();
    }

    @Bean
    public TeacherMapper teacherMapper() {
        return new TeacherMapper();
    }

    @Bean
    public ContactMessageMapper contactMessageMapper(){return new ContactMessageMapper();}

    @Bean
    public AdvisorTeacherMapper advisorTeacherMapper(){return new AdvisorTeacherMapper();}

    @Bean
    public StudentMapper studentMapper(){return new StudentMapper();}

    @Bean
    public LessonProgramTeacherStudentMapper lessonProgramTeacherStudentMapper(){return new LessonProgramTeacherStudentMapper();}
}






















