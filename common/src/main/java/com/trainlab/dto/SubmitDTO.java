package com.trainlab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Submit dto")
public class SubmitDTO {
    private Map<Long, Long> results;
    private long time;
}
