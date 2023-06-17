package com.schoolmanagement.controller;

import com.schoolmanagement.payload.response.AdvisorTeacherResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.AdvisorTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advisorTeacher")
@RequiredArgsConstructor
public class AdvisorTeacherController {

    private final AdvisorTeacherService advisorTeacherService;

    //Not: deleteAdvisorTeacher() *********************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete{id}")
    public ResponseMessage<?> deleteAdvisorTeacher(@PathVariable Long id ){

        return advisorTeacherService.deleteAdvisorTeacher(id);
    }

    //Not: getAllAdvisorTeacher() *********************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/getAll")
    public List<AdvisorTeacherResponse> getAllAdvisorTeacher(){

        return advisorTeacherService.getAllAdvisorTeacher();
    }

    //Not: getAllAdvisorTeacherWithPage() *************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @GetMapping("/search")
    public Page<AdvisorTeacherResponse> search(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return advisorTeacherService.search(page,size,sort,type);
    }
}















