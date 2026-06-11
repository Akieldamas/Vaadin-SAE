package com.usmb.but3.td4biblio.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.usmb.but3.td4biblio.entity.RoleUtilisateur;
import com.usmb.but3.td4biblio.repository.RoleUtilisateurRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleUtilisateurService {

    private final RoleUtilisateurRepo roleUtilisateurRepo;

    public List<RoleUtilisateur> getAllRoles() {
        return roleUtilisateurRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public RoleUtilisateur getRoleById(Integer id) {
        return roleUtilisateurRepo.findById(id).orElse(null);
    }

    public RoleUtilisateur saveRole(RoleUtilisateur roleUtilisateur) {
        return roleUtilisateurRepo.save(roleUtilisateur);
    }

    public RoleUtilisateur updateRole(RoleUtilisateur roleUtilisateur) {
        return roleUtilisateurRepo.save(roleUtilisateur);
    }

    public void deleteRoleById(Integer id) {
        roleUtilisateurRepo.deleteById(id);
    }
}