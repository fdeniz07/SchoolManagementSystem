package com.schoolmanagement.payload.request;

import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class AdminRequest extends BaseUserRequest {

    private boolean built_in; //Bu field true ise, bu kullanici asla silinemez

}
