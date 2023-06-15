package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.StudentInfoRequestWithoutTeacherId;
import com.schoolmanagement.payload.request.UpdateStudentInfoRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.StudentInfoResponse;
import com.schoolmanagement.service.StudentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/studentInfo")
@RequiredArgsConstructor
public class StudentInfoController {

    private final StudentInfoService studentInfoService;

    //Not: save() *******************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/save") //http://localhost:8080/studentInfo/save
    public ResponseMessage<StudentInfoResponse> save(HttpServletRequest httpServletRequest,
                                                     @RequestBody @Valid StudentInfoRequestWithoutTeacherId request) {

        String username = (String) httpServletRequest.getAttribute("username");
        return studentInfoService.save(username, request);
    }

    // Not: delete()*****************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @DeleteMapping("/delete/{studentInfoId}")
    public ResponseMessage<?> delete(@PathVariable Long studentInfoId) {
        return studentInfoService.deleteStudentInfo(studentInfoId);
    }

    // Not: update()*****************************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','TEACHER')")
    @PutMapping("/update/{studentInfoId}")
    public ResponseMessage<StudentInfoResponse> update(@RequestBody @Valid UpdateStudentInfoRequest studentInfoRequest,
                                                       @PathVariable Long studentInfoId) {
        return studentInfoService.update(studentInfoRequest, studentInfoId);
    }

    // Not: getAllForAdmin() *********************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN'")
    @GetMapping("/getAllForAdmin")
    public ResponseEntity<Page<StudentInfoResponse>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        //Pageable obje olusturma islemini Service katmaninda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());  //Bu kisimda secimi (3. ve 4. parametreyi) ön tarafa birakmiyoruz
        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllForAdmin(pageable);

        return new ResponseEntity<>(studentInfoResponse, HttpStatus.OK);
    }

    // Not: getAllForTeacher() ******************************************************************************************************************
    // -->Bir ögretmen kendi ögrencilerinin bilgilerini almak istedigi zaman bu method calisacak
    @PreAuthorize("hasAnyAuthority('TEACHER'")
    @GetMapping("/getAllForTeacher")
    public ResponseEntity<Page<StudentInfoResponse>> getAllForTeacher(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        //Pageable obje olusturma islemini Service katmaninda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String username = (String) httpServletRequest.getAttribute("username");

        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllTeacher(pageable, username);

        return new ResponseEntity<>(studentInfoResponse, HttpStatus.OK); // return new ResponseEntity.ok(studentInfoResponse); bu kod soldaki ile ayni
    }

    // Not: getAllForStudent() ******************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllByStudent")
    public ResponseEntity<Page<StudentInfoResponse>> getAllByStudent(
            HttpServletRequest httpServletRequest,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size

    ) {
        // Pageable obje olusturma islemini Service katinda yazilmasi best-practice
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        String username = (String) httpServletRequest.getAttribute("username");
        Page<StudentInfoResponse> studentInfoResponse = studentInfoService.getAllStudentInfoByStudent(username, pageable);
        return ResponseEntity.ok(studentInfoResponse);
    }

    // Not: getStudentInfoByStudentId() *************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASISTANTMANAGER','TEACHER')")
    @GetMapping("/getByStudentId/{studentId}")
    public ResponseEntity<List<StudentInfoResponse>> getStudentId(@PathVariable Long studentId) {

        List<StudentInfoResponse> studentInfoResponse = studentInfoService.getStudentInfoByStudentId(studentId);
        return ResponseEntity.ok(studentInfoResponse);
    }

    // Not: getStudentInfoById() ********************************************************************************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANTMANAGER','TEACHER')")
    @GetMapping("/get/{id}")
    public ResponseEntity<StudentInfoResponse> get(@PathVariable Long id) {

        StudentInfoResponse studentInfoResponse = studentInfoService.findStudentInfoById(id);
        return ResponseEntity.ok(studentInfoResponse);
    }


//ÖDEV
    // Not: getAllWithPage() *************************************************************************************************************************

}


























