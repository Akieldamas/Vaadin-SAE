package com.usmb.but3.td4biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.usmb.but3.td4biblio.entity.TypeAuteur;

public interface TypeAuteurRepo extends JpaRepository<TypeAuteur, Integer> {
    TypeAuteur findByLabel(String label);
}