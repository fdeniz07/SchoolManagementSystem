package com.schoolmanagement.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL) //Json icindeki null olanlarin gözükmemesini sagliyoruz
public class ResponseMessage<E> { //Generic yapilar bize loose coupling bir yapi olusturmamizi sagliyor. Gelismis bir polymorphism sagliyor.

    private E object;
    private String message;
    private HttpStatus httpStatus;

}
