package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"utilisateur", "document"})
@Entity
@Table(name = "emprunt")
@IdClass(EmpruntId.class)
public class Emprunt {

    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @Id
    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(name = "debut_emprunt")
    private LocalDate debutEmprunt;

    @Column(name = "fin_emprunt")
    private LocalDate finEmprunt;

    private Boolean prolongation;

    @Column(name = "fin_prolongation")
    private LocalDate finProlongation;
}