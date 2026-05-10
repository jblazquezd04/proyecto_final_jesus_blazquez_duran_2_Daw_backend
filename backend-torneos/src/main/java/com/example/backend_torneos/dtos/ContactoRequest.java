package com.example.backend_torneos.dtos;

import lombok.Data;

@Data
public class ContactoRequest {
    private String nombre;
    private String email;
    private String mensaje;
}
