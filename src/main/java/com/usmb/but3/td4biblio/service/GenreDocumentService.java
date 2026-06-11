package com.usmb.but3.td4biblio.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.GenreDocument;
import com.usmb.but3.td4biblio.repository.GenreDocumentRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreDocumentService {

    private final GenreDocumentRepo genreDocumentRepo;

    public List<GenreDocument> getAllGenres() {
        return genreDocumentRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public GenreDocument getGenreById(Integer id) {
        return genreDocumentRepo.findById(id).orElse(null);
    }

    public GenreDocument saveGenre(GenreDocument genreDocument) {
        return genreDocumentRepo.save(genreDocument);
    }

    public GenreDocument updateGenre(GenreDocument genreDocument) {
        return genreDocumentRepo.save(genreDocument);
    }

    public void deleteGenreById(Integer id) {
        genreDocumentRepo.deleteById(id);
    }
}