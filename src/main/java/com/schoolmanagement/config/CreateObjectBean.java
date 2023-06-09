package com.schoolmanagement.config;

import com.schoolmanagement.payload.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //Custom Metodlar üzerinden metotlar olusturup Bean'leri SpringBoot'a göndermis oluyoruz
public class CreateObjectBean {

    //Tek class üzerinden Bean'leri kontrol etmemizi sagliyoruz. Diger türlü @Component annotation'u yazmamiz gerekli
    @Bean
    public DeanMapper deanDTO() {
        return new DeanMapper();
    }

    @Bean
    public ViceDeanMapper viceDeanDTO() {
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
    public LessonProgramMapper lessonProgramRequestDto() {
        return new LessonProgramMapper();
    }

    @Bean
    public TeacherMapper teacherRequestDto() {
        return new TeacherMapper();
    }

    @Bean
    public ContactMessageMapper contactMessageMapper(){return new ContactMessageMapper();}
}






















