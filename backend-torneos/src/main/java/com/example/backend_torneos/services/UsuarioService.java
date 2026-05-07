package com.example.backend_torneos.services;

import com.example.backend_torneos.dtos.AuthResponse;
import com.example.backend_torneos.dtos.CambiarModoRequest;
import com.example.backend_torneos.dtos.ChangePasswordRequest;
import com.example.backend_torneos.dtos.UpdatePerfilRequest;
import com.example.backend_torneos.dtos.UsuarioDTO;

public interface UsuarioService {
    UsuarioDTO getMe(String email);
    AuthResponse cambiarModo(String email, CambiarModoRequest request);
    UsuarioDTO updatePerfil(String email, UpdatePerfilRequest request);
    void cambiarPassword(String email, ChangePasswordRequest request);
    void eliminarCuenta(String email);
}
