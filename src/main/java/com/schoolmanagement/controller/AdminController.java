package com.schoolmanagement.controller;

import com.schoolmanagement.entity.concretes.Admin;
import com.schoolmanagement.payload.request.AdminRequest;
import com.schoolmanagement.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //Not: save() *********************************************************************************************************************************
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid AdminRequest adminRequest) {

        return ResponseEntity.ok(adminService.save(adminRequest));
    }

    //Not: getAll() *********************************************************************************************************************************
    @GetMapping("/getAll")
    public ResponseEntity<Page<Admin>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        Page<Admin>  author =  adminService.getAllAdmin(pageable);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    //Not: delete() *********************************************************************************************************************************
    @DeleteMapping("/delete/{id}") //delete islemlerini id üzerinden yapmak daha performanslidir ve best practice dir
    public ResponseEntity<String> delete(@PathVariable Long id){

        return ResponseEntity.ok(adminService.deleteAdmin(id));

    }




}
