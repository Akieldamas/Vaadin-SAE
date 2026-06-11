package com.usmb.but3.td4biblio.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.entity.Document;
import com.usmb.but3.td4biblio.entity.Editeur;
import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.repository.DocumentRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    private final AuteurService auteurService;
    private final FormatService formatService;
    private final EditeurService editeurService;
    private final GenreDocumentService genreDocumentService;

    public List<Document> getAllDocuments() {
        return documentRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Document getDocumentById(Integer id) {
        return documentRepo.findById(id).orElse(null);
    }

    public Document saveDocument(Document document) {
        return documentRepo.save(document);
    }

    public Document updateDocument(Document document) {
        return documentRepo.save(document);
    }

    public void deleteDocumentById(Integer id) {
        documentRepo.deleteById(id);
    }

    public List<Document> getByTitreContainingIgnoreCase(String filter) {
        return documentRepo.findByTitreContainingIgnoreCase(filter);
    }
    public List<Document> getByAuteurId(Integer auteurId) {
        return documentRepo.findByAuteurId(auteurId);
    }   

    public void importFromCsv(List<Map<String, String>> rows) {
        
        for (Map<String, String> row : rows) {
            Document newDocument = new Document();
            newDocument.setTitre(row.get("title"));
            newDocument.setDescription(row.get("description"));
            System.out.println("ROW: " + row);
            System.out.println("date_acquisition: " + row.get("date_acquisition"));
            System.out.println("date_publication: " + row.get("date_publication"));
            String genresStr = row.get("genre");
            List<GenreDocument> genreList = new ArrayList<>();

            if (genresStr != null && !genresStr.isEmpty()) {
                for (String g : genresStr.split(",")) {
                    GenreDocument genre = genreDocumentService.getGenreByNom(g.trim());
                    if (genre == null) {
                        GenreDocument newGenre = new GenreDocument();
                        newGenre.setNom(g.trim());
                        genre = genreDocumentService.saveGenre(newGenre);
                    }
                    genreList.add(genre);
                }
            }

            newDocument.setGenres(genreList);


            // most likely subject to change as they were meant to be separated from document
            String nbPagesStr = row.get("nb_pages");
            String dureeStr = row.get("duree");
            if (nbPagesStr != null && !nbPagesStr.isEmpty())
                newDocument.setNbPages(Integer.parseInt(nbPagesStr));

            if (dureeStr != null && !dureeStr.isEmpty())
                System.out.println("");
                //newDocument.setDuree(Integer.parseInt(dureeStr));

            newDocument.setLienGif(row.get("lien_gif"));
            newDocument.setCodeEmplacement(row.get("code_emplacement"));
            newDocument.setCodeIsbn(row.get("code_isbn"));
            newDocument.setCodeEmprunt(row.get("code_emprunt"));
            newDocument.setSpecificite(row.get("specificite"));
            String dateAcquisitionStr = row.get("date_acquisition");
            if (dateAcquisitionStr != null && !dateAcquisitionStr.isEmpty())
                newDocument.setDateAcquisition(LocalDate.parse(dateAcquisitionStr));
            
            String datePublicationStr = row.get("date_publication");
            if (datePublicationStr != null && !datePublicationStr.isEmpty())
                newDocument.setDatePublication(LocalDate.parse(datePublicationStr));

            String fullName = row.get("auteur"); // "George Orwell"
            String[] parts = fullName.split(" ", 2); // ["George", "Orwell"]

            List<Auteur> auteurs;
            if (parts.length == 2) {
                auteurs = auteurService.getAuteursByNomLikeAndPrenomLike(parts[1], parts[0]); // Orwell, George
                if (auteurs.isEmpty()) {
                    auteurs = auteurService.getAuteursByNomLikeAndPrenomLike(parts[0], parts[1]); // maybe reversed
                }
            } else {
                auteurs = auteurService.getByNomContainingIgnoreCase(fullName);
            }

            Auteur auteur = auteurs.isEmpty() ? null : auteurs.get(0);
            newDocument.setAuteur(auteur);

            Editeur editeur = editeurService.getEditeurByNom(row.get("editeur"));
            newDocument.setEditeur(editeur);

            String longueurStr = row.get("longueur");
            String largeurStr = row.get("largeur");
            String poidsStr = row.get("poids");
            
            if (longueurStr != null && largeurStr != null && poidsStr != null && !longueurStr.isEmpty() && !largeurStr.isEmpty() && !poidsStr.isEmpty())
            {
                Format newFormat = new Format();
                newFormat.setLongueur(parseBigDecimalOrNull(longueurStr));
                newFormat.setLargeur(parseBigDecimalOrNull(largeurStr));
                newFormat.setPoids(parseBigDecimalOrNull(poidsStr));
                
                newFormat = formatService.saveFormat(newFormat);
                newDocument.setFormat(newFormat);
            }

            documentRepo.save(newDocument);
        }
    }

    private BigDecimal parseBigDecimalOrNull(String val) {
        return (val != null && !val.isEmpty()) ? new BigDecimal(val) : null;
    }
}