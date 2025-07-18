package com.mutassemalmahamid.ecommerce.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private String message;
    private boolean success = true;

    public MessageResponse(String message) {
        this.message = message;
        this.success = true;
    }
}