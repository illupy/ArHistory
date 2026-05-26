package org.illupy.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Match3SetResponse {
    private Long id;
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String note;
}
