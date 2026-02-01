package com.careersim.careersim.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSummaryDTO {

    private Long totalEvents;
    private Long unreadEvents;
    private Long matchEvents;
    private Long progressionEvents;
    private Long transferEvents;
    private Long awardEvents;
}