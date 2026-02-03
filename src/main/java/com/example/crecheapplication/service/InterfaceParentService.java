package com.example.crecheapplication.service;

import com.example.crecheapplication.model.Activitebebe;
import com.example.crecheapplication.model.Bebe;
import com.example.crecheapplication.model.Parent;

import java.util.List;
import java.util.Map;

public interface InterfaceParentService {
    /**
     * Inscrire un nouveau parent dans le système.
     *
     * @param nom  Nom du parent
     * @param prenom Prénom du parent
     * @param email  Email du parent
     * @param telephone Numéro de téléphone
     * @param password  Mot de passe
     * @return L'objet Parent enregistré
     */
    Parent inscrire(String nom, String prenom, String email, String telephone, String password);

    /**
     * Modifier les infos du parent connecté.
     *
     * @param token  JWT du parent connecté
     * @param parentDetails Objet Parent contenant les nouvelles informations
     * @return l'objet Parent mis à jour
     */
    Parent modifierParent(String token, Parent parentDetails);

    /**
     * Authentifier un parent avec email et mot de passe.
     *
     * @param email    Email du parent
     * @param password Mot de passe
     * @return Map contenant le token JWT et le rôle du parent
     */
    Map<String, String> login(String email, String password);

    /**
     * Obtenir la dernière activité effectuée d'un bébé aujourd'hui jusqu'à l'heure actuelle.
     *
     * @param token  JWT du parent connecté
     * @param idBebe ID du bébé
     * @return L'activité du bébé ou null si aucune activité
     */
    Activitebebe getActiviteMaintenant(String token, Long idBebe);

    /**
     * Obtenir toutes les activités d'un bébé pour la journée en cours.
     *
     * @param token  JWT du parent connecté
     * @param idBebe ID du bébé
     * @return Liste des activités du jour
     */
    List<Activitebebe> getActivitesAujourdhui(String token, Long idBebe);

    /**
     * Récupérer la liste des bébés d'un parent.
     *
     * @param token JWT du parent connecté
     * @return Liste des bébés
     */
    List<Bebe> getBebes(String token);

    /**
     * afficher un parent à partir de son email.
     *
     * @param email Email du parent
     * @return L'objet Parent correspondant
     */
    Parent afficherParentParEmail(String email);
}

