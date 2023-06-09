package com.schoolmanagement.service;

import com.schoolmanagement.entity.concretes.ViceDean;
import com.schoolmanagement.entity.enums.RoleType;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.payload.mappers.ViceDeanMapper;
import com.schoolmanagement.payload.request.ViceDeanRequest;
import com.schoolmanagement.payload.response.ResponseMessage;
import com.schoolmanagement.payload.response.ViceDeanResponse;
import com.schoolmanagement.repository.ViceDeanRepository;
import com.schoolmanagement.utils.CheckParameterUpdateMethod;
import com.schoolmanagement.utils.CheckUniqueFields;
import com.schoolmanagement.utils.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViceDeanService {

    private final ViceDeanRepository viceDeanRepository;
    private final ViceDeanMapper viceDeanMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final CheckUniqueFields checkUniqueFields;

    //Not: save() **************************************************************************************************************************************
    public ResponseMessage<ViceDeanResponse> save(ViceDeanRequest viceDeanRequest) {

        // !!! Dublicate kontrolu
        checkUniqueFields.checkDuplicate(viceDeanRequest.getUsername(), viceDeanRequest.getSsn(), viceDeanRequest.getPhoneNumber());

        ViceDean viceDean = viceDeanMapper.createPojoFromDTO(viceDeanRequest);
        // ViceDean viceDean = viceDeanDto.dtoViceBean(viceDeanRequest); yardimci metodu yazmadan bu sekilde yukaridaki kod yazilabilirdi

        //Role ve Password encode islemleri
        viceDean.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANTMANAGER));
        viceDean.setPassword(passwordEncoder.encode(viceDeanRequest.getPassword()));

        viceDeanRepository.save(viceDean);

        //response nesnesi olusturulacak
        return ResponseMessage.<ViceDeanResponse>builder()
                .message("Vice Dean Saved")
                .httpStatus(HttpStatus.CREATED)
                .object(viceDeanMapper.createViceDeanResponse(viceDean))
                .build();
    }

    //Not: updateById() *********************************************************************************************************************************
    public ResponseMessage<ViceDeanResponse> update(ViceDeanRequest newViceDean, Long managerId) {

        Optional<ViceDean> viceDean = viceDeanRepository.findById(managerId);

        if (!viceDean.isPresent()) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, managerId));
        } else if (!CheckParameterUpdateMethod.checkParameter(viceDean.get(), newViceDean)) {
            checkUniqueFields.checkDuplicate(newViceDean.getUsername(), newViceDean.getSsn(), newViceDean.getPhoneNumber());
        }

        ViceDean updatedViceDean = viceDeanMapper.createUpdatedViceDean(newViceDean, managerId);
        updatedViceDean.setPassword(passwordEncoder.encode(newViceDean.getPassword()));
        updatedViceDean.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANTMANAGER));

        viceDeanRepository.save(updatedViceDean);

        return ResponseMessage.<ViceDeanResponse>builder()
                .message("Vice Dean Updated")
                .httpStatus(HttpStatus.CREATED)
                .object(viceDeanMapper.createViceDeanResponse(updatedViceDean))
                .build();
    }

    //Not: delete() *************************************************************************************************************************************
    public ResponseMessage<?> deleteViceDean(Long managerId) {

        Optional<ViceDean> viceDean = viceDeanRepository.findById(managerId);

        if (!viceDean.isPresent()) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, managerId));
        }

        viceDeanRepository.deleteById(managerId);

        return ResponseMessage.builder()
                .message("Vice Dean Deleted")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //Not: getById() *************************************************************************************************************************************
    public ResponseMessage<ViceDeanResponse> getViceDeanById(Long managerId) {

        Optional<ViceDean> viceDean = viceDeanRepository.findById(managerId);
        if (!viceDean.isPresent()) {
            throw new ResourceNotFoundException(String.format(Messages.NOT_FOUND_USER2_MESSAGE, managerId));
        }
        return ResponseMessage.<ViceDeanResponse>builder()
                .message("Vice Dean Successfully Found")
                .httpStatus(HttpStatus.OK)
                .object(viceDeanMapper.createViceDeanResponse(viceDean.get()))
                .build();
    }

    //Not: getAll() **************************************************************************************************************************************
    public List<ViceDeanResponse> getAllViceDean() {

        return viceDeanRepository.findAll()
                .stream()
                .map(viceDeanMapper::createViceDeanResponse)
                .collect(Collectors.toList());
    }

    //Not: getAllWithPage() ******************************************************************************************************************************
    public Page<ViceDeanResponse> getAllWithPage(int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        //Eger default degerler geliyorsa yani hicbir deger girilmediyse asagidaki kod calisir
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return viceDeanRepository.findAll(pageable).map(viceDeanMapper::createViceDeanResponse);
    }
}




