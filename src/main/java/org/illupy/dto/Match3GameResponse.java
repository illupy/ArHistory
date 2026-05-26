package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Match3GameResponse {
    private List<Match3SetResponse> sets;
    private List<String> allImages;
}
