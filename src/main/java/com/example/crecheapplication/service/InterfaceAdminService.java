package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;

import java.time.LocalDate;
import java.util.List;

public interface InterfaceAdminService {
    /**
     * Récupère tous les parents ayant le rôle ROLE_PARENT.
     * @return Liste de parents
     */
    List<Parent> getAllParents();

    /**
     * Récupère tous les admins ayant le rôle ROLE_ADMIN.
     * @return Liste d'admins
     */
    List<Parent> getAllAdmins();

    /**
     * Crée un nouveau parent avec le rôle ROLE_PARENT.
     * @param nom Nom du parent
     * @param prenom Prénom du parent
     * @param email Email du parent
     * @param telephone Téléphone du parent
     * @param password Mot de passe du parent (sera encodé)
     * @return Parent créé
     */
    Parent createParent(String nom, String prenom, String email, String telephone, String password);

    /**
     * Met à jour un parent existant.
     * @param id ID du parent
     * @param nom Nouveau nom
     * @param prenom Nouveau prénom
     * @param email Nouvel email
     * @param telephone Nouveau téléphone
     * @param password Nouveau mot de passe (optionnel)
     * @param role Nouveau rôle (ROLE_ADMIN ou ROLE_PARENT)
     * @return Parent mis à jour
     */
    Parent updateParent(Long id, String nom, String prenom, String email, String telephone, String password, String role);

    /**
     * Supprime un parent par son ID.
     * @param idParent ID du parent
     */
    void deleteParent(Long idParent);

    /**
     * Supprime un admin par son ID.
     * @param adminId ID de l'admin à supprimer
     * @param emailFromToken Email de l'admin connecté (pour éviter la suppression de soi-même)
     */
    void deleteAdmin(Long adminId, String emailFromToken);

    /**
     * Crée un nouvel admin avec le rôle ROLE_ADMIN.
     * @param nom Nom de l'admin
     * @param prenom Prénom de l'admin
     * @param email Email de l'admin
     * @param telephone Téléphone de l'admin
     * @param password Mot de passe de l'admin (sera encodé)
     * @return Admin créé
     */
    Parent inscrireadmin(String nom, String prenom, String email, String telephone, String password);

    /**
     * Récupère tous les bébés.
     * @return Liste de bébés
     */
    List<Bebe> getAllBebes();

    /**
     * Ajoute un bébé pour un parent donné.
     * @param parentId ID du parent
     * @param nom Nom du bébé
     * @param prenom Prénom du bébé
     * @param dateNais Date de naissance du bébé
     * @return Bébé créé
     */
    Bebe ajouterBebe(Long parentId, String nom, String prenom, LocalDate dateNais);

    /**
     * Met à jour un bébé existant.
     * @param id ID du bébé
     * @param bebeDetails Détails du bébé à mettre à jour
     * @return Bébé mis à jour
     */
    Bebe updateBebe(Long id, Bebe bebeDetails);

    /**
     * Supprime un bébé par son ID.
     * @param id ID du bébé
     */
    void deleteBebe(Long id);

    /**
     * Récupère toutes les activités.
     * @return Liste d'activités
     */
    List<Activitebebe> getAllActivites();

    /**
     * Récupère toutes les activités d'un bébé.
     * @param idBebe ID du bébé
     * @return Liste d'activités
     */
    List<Activitebebe> getActivitesOfBebe(Long idBebe);


    /**
     * Ajoute une activité pour un bébé.
     * @param idBebe ID du bébé
     * @param activiteBebe Activité à ajouter
     * @return Activité créée
     */
    Activitebebe ajouterActivite(Long idBebe, Activitebebe activiteBebe);

    /**
     * Modifie une activité existante.
     * @param idActivite ID de l'activité
     * @param activiteDetails Détails à modifier
     * @return Activité modifiée
     */
    Activitebebe modifierActivite(Long idActivite, Activitebebe activiteDetails);

    /**
     * Supprime une activité par son ID.
     * @param idActivite ID de l'activité
     */
    void supprimerActivite(Long idActivite);

    /**
     * Récupère le parent d'un bébé.
     * @param idBebe ID du bébé
     * @return Parent associé
     */
    Parent getParentOfBebe(Long idBebe);

    /**
     * Récupère les activités du jour pour un bébé .
     * @param idBebe ID du bébé
     * @return Liste d'activités
     */
    List<Activitebebe> ActivitesAujourdhuiAdmin(Long idBebe);
}
