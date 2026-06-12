package com.usmb.but3.td4biblio.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.Format;

public interface FormatRepo extends JpaRepository<Format, Integer> {
    List<Format> findByLongueurAndLargeur(BigDecimal longueur, BigDecimal largeur);
}