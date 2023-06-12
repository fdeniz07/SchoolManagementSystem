package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.StudentRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentResponse;
import com.schoolmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    //Not: save() ************************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PostMapping("/save")
    public ResponseMessage<StudentResponse> save(@RequestBody @Valid StudentRequest studentRequest) {

        return studentService.save(studentRequest);
    }

    //Not: changeActiveStatus() ***********************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/changeStatus") //@PatchMapping de yapilabilirdi
    public ResponseMessage<?> changeStatus(@RequestParam Long id, @RequestParam boolean status) {

        return studentService.changeStatus(id, status);
    }

    //Not: getAllStudents () ***************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @GetMapping("/getAll")
    public List<StudentResponse> getAllStudent() {

        return studentService.getAllStudent();
    }

    //Not: updateStudent() *******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER')")
    @PutMapping("/update/{userId}")
    public ResponseMessage<StudentResponse> updateStudent(@PathVariable Long userId,
                                                          @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.updateStudent(userId,studentRequest);
    }


}





























