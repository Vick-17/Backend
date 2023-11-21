package com.tindMovie.tindMovie.Controller;

import com.tindMovie.tindMovie.Model.MovieEntity;
import com.tindMovie.tindMovie.Model.SwipeEntity;
import org.springframework.http.HttpStatus;
import com.tindMovie.tindMovie.Model.MatchEntity;
import com.tindMovie.tindMovie.Model.MovieEntity;
import com.tindMovie.tindMovie.Model.SwipeEntity;
import com.tindMovie.tindMovie.Repository.MatchRepository;
import com.tindMovie.tindMovie.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/match")
public class MatchController {

/*    @GetMapping("/match/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public List<MovieEntity> getAllSwipeByUser(@PathVariable Long userId) {
        if (userId == null) {
            throw new RuntimeException("user_id est absent");
        } else {
            // Récupére le swipe de l'utilisateur
            List<SwipeEntity> userSwipe = swipeRepository.findByUserId(userId);

            List<Long> likedFilmIds = new ArrayList<>();

            // Parcour les swipes pour extraire les ID des films likés
            for (SwipeEntity swipe : userSwipe) {
                if (swipe.isLiked() && !swipe.isWatched()) {
                    likedFilmIds.add(swipe.getFilmId());
                }
            }
            // Récupére les films correspondant aux ID des films aimées
            return movieRepository.findByIdIn(likedFilmIds);
        }
    }*/
import java.util.Optional;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MovieRepository movieRepository;

    @CrossOrigin
    @GetMapping(value = "/macthByUser/{userId1}/{userId2}")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> getMatchByUser(@PathVariable Long userId1, @PathVariable Long userId2) {
        if (userId1 == null || userId2 == null) {
            throw new RuntimeException("user_id est absent");
        } else {
            // Récupére les match des utilisateurs
            List<MatchEntity> userMatch = matchRepository.findByUserId1AndUserId2OrUserId1AndUserId2(userId1, userId2, userId2, userId1);

            List<Long> matchedMovies = new ArrayList<>();

            // Parcour les match pour extraire les ID des films likés
            for (MatchEntity match : userMatch) {
                matchedMovies.add(match.getFilmId());
            }
            // Récupére les films correspondant aux ID des films
            return movieRepository.findByIdIn(matchedMovies);
        }
    }
}
