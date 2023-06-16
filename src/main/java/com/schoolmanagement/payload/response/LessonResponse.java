package com.schoolmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LessonResponse implements Serializable {

    private Long lessonId;
    private String lessonName;
    private int creditScore;
    private boolean isCompulsory;

}
