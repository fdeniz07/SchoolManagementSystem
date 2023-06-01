package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("dean")
public class DeanController {

    private final DeanService deanService;

    //Not: save() *********************************************************************************************************************************
    @PostMapping("/save") //http://localhost:8080/dean/save
    @PreAuthorize("hasAuthority('ADMIN')") //Tek bir kullaniciya rol atamasi yapacaksak hasAuthority kullanabiliriz
    public ResponseMessage<DeanResponse> save(@RequestBody @Valid DeanRequest deanRequest) {

        return deanService.save(deanRequest);
    }
}
