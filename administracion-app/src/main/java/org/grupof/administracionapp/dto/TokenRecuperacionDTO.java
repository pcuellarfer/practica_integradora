package org.grupof.administracionapp.dto;

import jakarta.persistence.Id;

public class TokenRecuperacionDTO {
    @Id
    private Long id;

    private String token;
    private String email;

}
