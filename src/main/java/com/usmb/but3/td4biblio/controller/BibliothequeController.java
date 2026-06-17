package com.usmb.but3.td4biblio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usmb.but3.td4biblio.entity.Auteur;
import com.usmb.but3.td4biblio.service.AuteurService;

import java.util.List;

/**
 * Controller for handling Auteur-related HTTP requests.
 */
@RestController
@RequestMapping("/biblio/auteur")
@RequiredArgsConstructor
@Validated
public class BibliothequeController {

}
