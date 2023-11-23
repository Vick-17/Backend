package com.tindMovie.tindMovie.Controller;

import com.tindMovie.tindMovie.Model.MovieEntity;
import org.springframework.http.HttpStatus;
import com.tindMovie.tindMovie.Model.MatchEntity;
import com.tindMovie.tindMovie.Repository.MatchRepository;
import com.tindMovie.tindMovie.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/match")
public class MatchController {


    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MovieRepository movieRepository;

    @CrossOrigin
    @GetMapping(value = "/matchByUser/{userId1}/{userId2}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<MovieEntity>> getMatchByUser(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            if (userId1 == null || userId2 == null) {
                throw new RuntimeException("user_id est absent");
            } else {
                // Récupère les matchs des utilisateurs
                List<MatchEntity> userMatch = matchRepository.findByUserId1AndUserId2OrUserId1AndUserId2(userId1, userId2, userId2, userId1);

                List<Long> matchedMovies = new ArrayList<>();

                // Parcourt les matchs pour extraire les ID des films likés
                for (MatchEntity match : userMatch) {
                    matchedMovies.add(match.getFilmId());
                }
                // Récupère les films correspondant aux ID des films
                List<MovieEntity> matchedMoviesList = movieRepository.findByIdIn(matchedMovies);

                return ResponseEntity.ok(matchedMoviesList);
            }
        } catch (Exception e) {
            // Gère les exceptions et renvoie une réponse appropriée
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @PutMapping("/watchedMatch")
//    @CrossOrigin
//    public ResponseEntity<Void> setMovieWatch(@RequestBody MatchEntity match) {
//        try {
//            // Recherche du swipe en fonction de l'ID de l'utilisateur et de l'ID du film
//            Optional<MatchEntity> optionalMatch = matchRepository.findOneByUserId1AndUserId2OrUserId1AndUserId2(match.getUserId1(), match.getUserId2(), match.getUserId1(), match.getUserId2());
//
//            if (optionalMatch.isPresent()) {
//                MatchEntity existingMatch = optionalMatch.get();
//                // Mettez à jour la propriété isWatched à true
//                existingMatch.setWatched(true);
//                matchRepository.save(existingMatch);
//                return ResponseEntity.ok().build();
//            } else {
//                // Si le swipe n'est pas trouvé, retournez une réponse 404 Not Found
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
}
