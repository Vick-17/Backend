package com.tindMovie.tindMovie.Service;

import com.tindMovie.tindMovie.Model.ActorEntity;
import com.tindMovie.tindMovie.Model.GenreEntity;
import com.tindMovie.tindMovie.Model.MovieEntity;
import com.tindMovie.tindMovie.Model.NoteEntity;
import com.tindMovie.tindMovie.Model.RealisatorEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service pour la logique métier des recommandations basées sur les notes de
 * l'utilisateur.
 */
@Service
public class AlgoService {

    private final ActorService actorService;
    private final RealisatorService realisatorService;
    private final GenreService genreService;

    /**
     * Constructeur du service.
     *
     * @param actorService      Service des acteurs.
     * @param realisatorService Service des réalisateurs.
     * @param genreService      Service des genres.
     */

    public AlgoService(
            ActorService actorService,
            RealisatorService realisatorService,
            GenreService genreService) {
        this.actorService = actorService;
        this.realisatorService = realisatorService;
        this.genreService = genreService;
    }

    /**
     * Obtient la liste des identifiants de films notés avec une note élevée (>= 3)
     * par l'utilisateur.
     *
     * @param userNotes Liste des notes de l'utilisateur.
     * @return Liste des identifiants de films notés avec une note élevée.
     */
    public List<Long> getHighRatingMovieIds(List<NoteEntity> userNotes) {
        return userNotes
                .stream()
                .filter(note -> note.getRating() >= 3)
                .map(NoteEntity::getMovieId)
                .toList();
    }

    /**
     * Obtient la liste des identifiants d'acteurs appréciés par l'utilisateur à
     * partir des films notés.
     *
     * @param highRatingMovieIds Liste des identifiants de films notés avec une note
     *                           élevée par l'utilisateur.
     * @return Liste des identifiants d'acteurs appréciés par l'utilisateur.
     */
    public List<Long> getLikedActors(List<Long> highRatingMovieIds) {
        List<Long> likedActors = new ArrayList<>();

        for (Long movieId : highRatingMovieIds) {
            List<ActorEntity> actorsForMovie = actorService.getActorsForMovie(movieId);
            likedActors.addAll(
                    actorsForMovie.stream() // Crée un flux à partir de la liste actorsForMovie
                        .map(ActorEntity::getId) // Applique une opération de transformation en extrayant l'ID de chaque ActorEntity
                            .toList() // Collecte les résultats transformés dans une liste
            );
        }

        return likedActors;
    }

    /**
     * Obtient la liste des identifiants de réalisateurs appréciés par l'utilisateur
     * à partir des films notés.
     *
     * @param highRatingMovieIds Liste des identifiants de films notés avec une note
     *                           élevée par l'utilisateur.
     * @return Liste des identifiants de réalisateurs appréciés par l'utilisateur.
     */
    public List<Long> getLikedRealisators(List<Long> highRatingMovieIds) {
        List<Long> likedRealisators = new ArrayList<>();

        for (Long movieId : highRatingMovieIds) {
            List<RealisatorEntity> realisatorsForMovie = realisatorService.getRealForMovie(movieId);
            likedRealisators.addAll(
                    realisatorsForMovie.stream().map(RealisatorEntity::getId).toList());
        }

        return likedRealisators;
    }

    /**
     * Obtient la liste des identifiants de genres appréciés par l'utilisateur à
     * partir des films notés.
     *
     * @param highRatingMovieIds Liste des identifiants de films notés avec une note
     *                           élevée par l'utilisateur.
     * @return Liste des identifiants de genres appréciés par l'utilisateur.
     */
    public List<Long> getLikedGenres(List<Long> highRatingMovieIds) {
        List<Long> likedGenres = new ArrayList<>();

        for (Long movieId : highRatingMovieIds) {
            List<GenreEntity> genreForMovie = genreService.getGenreForMovie(movieId);
            likedGenres.addAll(
                    genreForMovie.stream().map(GenreEntity::getId).toList());
        }
        return likedGenres;
    }

    /**
     * Obtient les acteurs appréciés fréquemment (au moins 2 fois).
     *
     * @param likedActors Liste des identifiants des acteurs appréciés par
     *                    l'utilisateur.
     * @return Liste des identifiants des acteurs appréciés fréquemment.
     */
    public List<Long> getCommonActors(List<Long> likedActors) {
        // Crée une carte qui associe l'ID de l'acteur à son nombre d'occurrences dans la liste
        Map<Long, Long> actorCounts = likedActors
                .stream()
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        // Transforme la carte en un flux d'entrées de carte (ID de l'acteur et son nombre d'occurrences)
        return actorCounts
                .entrySet()
                .stream()
                // Filtre pour inclure uniquement les acteurs appréciés fréquemment (au moins deux fois)
                .filter(entry -> entry.getValue() >= 2)
                // Transforme le flux en un flux d'ID d'acteurs
                .map(Map.Entry::getKey)
                // Convertit le flux résultant en une liste
                .toList();
    }

    /**
     * Obtient les genres appréciés fréquemment (au moins 2 fois).
     *
     * @param likedGenre Liste des identifiants des genres appréciés par
     *                   l'utilisateur.
     * @return Liste des identifiants des genres appréciés fréquemment.
     */
    public List<Long> getCommonGenres(List<Long> likedGenre) {
        Map<Long, Long> genreCounts = likedGenre
                .stream()
                .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));

        return genreCounts
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Obtient les réalisateurs appréciés fréquemment (au moins 2 fois).
     *
     * @param likedRealisators Liste des identifiants des réalisateurs appréciés par
     *                         l'utilisateur.
     * @return Liste des identifiants des réalisateurs appréciés fréquemment.
     */
    public List<Long> getCommonRealisators(List<Long> likedRealisators) {
        Map<Long, Long> realisatorCounts = likedRealisators
                .stream()
                .collect(
                        Collectors.groupingBy(realisator -> realisator, Collectors.counting()));

        return realisatorCounts
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Obtient les films recommandés en fonction des acteurs, réalisateurs, genres
     * appréciés et des films notés.
     *
     * @param commonActors       Liste des identifiants d'acteurs appréciés
     *                           fréquemment.
     * @param commonRealisators  Liste des identifiants de réalisateurs appréciés
     *                           fréquemment.
     * @param commonGenres       Liste des identifiants de genres appréciés
     *                           fréquemment.
     * @param highRatingMovieIds Liste des identifiants de films notés avec une note
     *                           élevée par l'utilisateur.
     * @return Liste des films recommandés.
     */
    public List<MovieEntity> getRecommendedMovies(
            List<Long> commonActors,
            List<Long> commonRealisators,
            List<Long> commonGenres,
            List<Long> highRatingMovieIds) {
        Set<MovieEntity> recommendedMovies = new HashSet<>();

        for (Long actorId : commonActors) {
            List<MovieEntity> moviesByActor = actorService.getMoviesByActorId(actorId);
            moviesByActor
                    .stream()
                    .filter(movie -> !highRatingMovieIds.contains(movie.getId()))
                    .forEach(recommendedMovie -> recommendedMovies.add(recommendedMovie));
        }

        for (Long realisatorId : commonRealisators) {
            List<MovieEntity> moviesByRealisator = realisatorService.getMoviesByRealId(realisatorId);
            moviesByRealisator
                    .stream()
                    .filter(movie -> !highRatingMovieIds.contains(movie.getId()))
                    .forEach(recommendedMovie -> recommendedMovies.add(recommendedMovie));
        }

        for (Long genreId : commonGenres) {
            List<MovieEntity> moviesByGenre = genreService.getMoviesByGenreId(genreId);
            moviesByGenre
                    .stream()
                    .filter(movie -> !highRatingMovieIds.contains(movie.getId()))
                    .forEach(recommendedMovie -> recommendedMovies.add(recommendedMovie));
        }

        return new ArrayList<>(recommendedMovies);
    }
}
