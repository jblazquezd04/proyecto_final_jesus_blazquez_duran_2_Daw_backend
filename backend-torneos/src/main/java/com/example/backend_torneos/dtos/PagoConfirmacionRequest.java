package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class PagoConfirmacionRequest {
    private String orderId;
    private String total;
}
