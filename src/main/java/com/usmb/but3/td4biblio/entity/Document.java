package com.usmb.but3.td4biblio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;
    private String description;
    
    @Column(name = "nb_pages")
    private Integer nbPages;
    
    @Column(name = "lien_gif")
    private String lienGif;
    
    @Column(name = "code_emplacement", length = 10)
    private String codeEmplacement;
    
    @Column(name = "code_isbn", length = 13)
    private String codeIsbn;
    
    @Column(name = "code_emprunt")
    private String codeEmprunt;
    
    private String specificite;
    
    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;
    
    @Column(name = "date_publication")
    private LocalDate datePublication;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "auteur_id", nullable = false)
    private Auteur auteur;

    @ManyToOne
    @JoinColumn(name = "format_id", nullable = false)
    private Format format;

    @ManyToOne
    @JoinColumn(name = "editeur_id", nullable = false)
    private Editeur editeur;

    // Relation N-N avec GenreDocument
    @ManyToMany
    @JoinTable(
        name = "document_genre_document",
        joinColumns = @JoinColumn(name = "document_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_document_id")
    )
    private List<GenreDocument> genres;
}