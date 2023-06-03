package com.schoolmanagement.config;

import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.dto.ViceDeanDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //Custom Metodlar üzerinden metotlar olusturup Bean'leri SpringBoot'a göndermis oluyoruz
public class CreateObjectBean {

    //Tek class üzerinden Bean'leri kontrol etmemizi sagliyoruz. Diger türlü @Component annotation'u yazmamiz gerekli

    @Bean
    public DeanDto deanDTO() {

        return new DeanDto();
    }

    @Bean
    public ViceDeanDto viceDeanDTO() {

        return new ViceDeanDto();
    }

}























