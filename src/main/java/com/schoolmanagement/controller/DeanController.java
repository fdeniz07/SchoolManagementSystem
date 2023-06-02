package com.schoolmanagement.controller;

import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.service.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    //Not: updateById() *********************************************************************************************************************************
    @PutMapping("/update/{userId}") //http://localhost:8080/dean/update/1
    @PreAuthorize("hasAuthority('ADMIN')")
    //TODO: Dean eklenmeli --getprinciple() metodu ile o anki Dean burayi degistirsin
    public ResponseMessage<DeanResponse> updateById(@RequestBody @Valid DeanRequest deanRequest, @PathVariable Long userId) { //@Valid --> nullpointerexception'u önler

        return deanService.update(deanRequest, userId);

    }

    //Not: delete() *********************************************************************************************************************************
    @DeleteMapping("/delete/{userId}") //http://localhost:8080/dean/delete/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<?> delete(@PathVariable Long userId) {

        return deanService.deleteDean(userId);
    }

    //Not: getById() *********************************************************************************************************************************
    @GetMapping("/getManagerById/{userId}") //http://localhost:8080/dean/getManagerById/1
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseMessage<DeanResponse> getDeanById(@PathVariable Long userId) {

        return deanService.getDeanById(userId);
    }

    //Not: getAll() *********************************************************************************************************************************
    @GetMapping("/getAll") //http://localhost:8080/dean/getAll
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DeanResponse> getAll() {

        return deanService.getAllDean();
    }

    //Not: search() *********************************************************************************************************************************
    @GetMapping("/search") //http://localhost:8080/dean/search
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<DeanResponse> search( //default degerlerde verilebilir //TODO getAllWithPage
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "type") String type
    ) {

        return deanService.search(page, size, sort, type);

    }


}
























