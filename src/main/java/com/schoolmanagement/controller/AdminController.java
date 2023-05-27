package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //Not: save() *********************************************************************************************************************************
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid AdminRequest adminRequest){

        return ResponseEntity.ok(adminService.save(adminRequest));
    }

    //Not: getAll() *********************************************************************************************************************************


    //Not: delete() *********************************************************************************************************************************
}
