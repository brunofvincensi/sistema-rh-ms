package com.rh.email.dtos;

import java.util.UUID;

public record EmailRecordDto(String emailTo,
                             String subject,
                             String text) {
}
