package com.usmb.but3.td4biblio.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.Format;
import com.usmb.but3.td4biblio.repository.FormatRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormatService {

    private final FormatRepo formatRepo;

    public List<Format> getAllFormats() {
        return formatRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Format getFormatById(Integer id) {
        return formatRepo.findById(id).orElse(null);
    }

    public Format saveFormat(Format format) {
        return formatRepo.save(format);
    }

    public Format updateFormat(Format format) {
        return formatRepo.save(format);
    }

    public void deleteFormatById(Integer id) {
        formatRepo.deleteById(id);
    }
    public List<Format> findByDimensions(BigDecimal longueur, BigDecimal largeur) {
        return formatRepo.findByLongueurAndLargeur(longueur, largeur);
    }
}