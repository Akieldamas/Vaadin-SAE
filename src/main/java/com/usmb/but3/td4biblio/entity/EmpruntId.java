package com.usmb.but3.td4biblio.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpruntId implements Serializable {
    private Integer utilisateur;
    private Integer document;
}