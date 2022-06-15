package com.cvg.mockito.test.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Banco {
    private Long id;
    private String nombre;
    private Integer totalTransferencias;
}
