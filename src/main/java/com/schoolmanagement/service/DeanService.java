package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.Dean;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.payload.dto.DeanDto;
import com.schoolmanagement.payload.request.DeanRequest;
import com.schoolmanagement.payload.response.DeanResponse;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.repository.DeanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service //service yazmazsak Bean olmaz, bean olmazsa DI yapamayiz
@RequiredArgsConstructor
public class DeanService {

    private final DeanRepository deanRepository;
    private final AdminService adminService;
    private final DeanDto deanDto;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    //Not: save() *********************************************************************************************************************************
    public ResponseMessage<DeanResponse> save(DeanRequest deanRequest) {

        //!!! Dublicate kontrolü
        adminService.checkDuplicate(deanRequest.getUsername(),
                deanRequest.getSsn(),
                deanRequest.getPhoneNumber());

        //!!! DTO - POJO Dönüsümü
        Dean dean = createDtoForDean(deanRequest);

        //Role ve password bilgileri uygun sekilde setleniyor
        dean.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        dean.setPassword(passwordEncoder.encode(dean.getPassword()));

        //!!! DB'ye kayit
        Dean saveDean = deanRepository.save(dean); //aslinda geri döndürmek mecburi degil ama frontend de gözüksün diye gönderiyoruz

        return ResponseMessage.<DeanResponse>builder()
                .message("Dean saved")
                .httpStatus(HttpStatus.CREATED)
                .object(createDeanResponse(saveDean)) //yardimci metot
                .build();
    }

    private Dean createDtoForDean(DeanRequest deanRequest) { //createDtoToPOJO

        return deanDto.dtoDean(deanRequest); //Injection yaparak (1.Yol - kisa)

    }

    private DeanResponse createDeanResponse(Dean dean) {

        return DeanResponse.builder()  //injection yapmadan (2.Yol - uzun)
                .userId(dean.getId())
                .username(dean.getUsername())
                .name(dean.getName())
                .surname(dean.getSurname())
                .birthDay(dean.getBirthDay())
                .birthPlace(dean.getBirthPlace())
                .phoneNumber(dean.getPhoneNumber())
                .gender(dean.getGender())
                .ssn(dean.getSsn())
                .build();
    }
}
