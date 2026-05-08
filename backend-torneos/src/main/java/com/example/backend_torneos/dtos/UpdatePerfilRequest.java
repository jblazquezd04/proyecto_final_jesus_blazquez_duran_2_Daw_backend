package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class UpdatePerfilRequest {
    private String username;
    private String biografia;
    private String telefono;
    private String avatarUrl;
}
