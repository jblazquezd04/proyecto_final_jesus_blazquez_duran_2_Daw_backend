package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String passwordActual;
    private String passwordNueva;
}
