package com.tindMovie.tindMovie.Controller;

import com.tindMovie.tindMovie.Model.MovieEntity;
import com.tindMovie.tindMovie.Model.SwipeEntity;
import org.springframework.http.HttpStatus;
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
}
