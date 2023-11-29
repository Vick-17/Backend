package com.tindMovie.tindMovie.Controller;

import com.tindMovie.tindMovie.Model.MatchEntity;
import com.tindMovie.tindMovie.Model.MovieEntity;
import com.tindMovie.tindMovie.Model.LikeEntity;
import com.tindMovie.tindMovie.Model.UsersEntity;
import com.tindMovie.tindMovie.Repository.MatchRepository;
import com.tindMovie.tindMovie.Repository.MovieRepository;
import com.tindMovie.tindMovie.Repository.LikeRepository;
import com.tindMovie.tindMovie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/swipe")
public class LikeController {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    @PostMapping(value = "/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeEntity createSwipe(@RequestBody LikeEntity swipe) {

        if (!likeRepository.existsByUserIdAndFilmId(swipe.getUserId(), swipe.getFilmId())) {
            if ("left".equals((swipe.getSwipeDirection()))) {
                swipe.setLiked(true);

            } else if ("right".equals(swipe.getSwipeDirection())) {
                swipe.setLiked(false);
            }

            likeRepository.save(swipe);
            Optional<UsersEntity> userOptional = userRepository.findById(swipe.getUserId());
            if (userOptional.isPresent()) {
                UsersEntity users = userOptional.get();
                if (users.getPartenaire() != null) {
                    Optional<LikeEntity> userSwipeOptional = likeRepository.findByUserIdAndFilmId(swipe.getUserId(), swipe.getFilmId());
                    Optional<LikeEntity> partnerSwipeOptional = likeRepository.findByUserIdAndFilmId(users.getPartenaire().getId(), swipe.getFilmId());

                    if (userSwipeOptional.isPresent() && partnerSwipeOptional.isPresent() &&
                            userSwipeOptional.get().isLiked() && partnerSwipeOptional.get().isLiked()) {
                        MatchEntity match = new MatchEntity();
                        match.setUserId1(swipe.getUserId());
                        match.setUserId2(users.getPartenaire().getId());
                        match.setFilmId(swipe.getFilmId());
                        matchRepository.save(match);
                    }
                }
            }
        }
        return swipe;
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSwipe(@RequestBody LikeEntity swipe) {
        // Recherche du swipe à suprimmer
        Optional<LikeEntity> optionalSwipe = likeRepository.findByUserIdAndFilmId(swipe.getUserId(), swipe.getFilmId());
        if (optionalSwipe.isPresent()) {
            // Si le swipe existe, alors on le supprime
            likeRepository.delete(optionalSwipe.get());
            ;
            // Retourne une réponse avec le statut 204 No Content pour indiquer le delete est bon
            return ResponseEntity.noContent().build();
        } else {
            // si le swipe n'est pas trouvé, retourne une réponse 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allSwipe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> getAllSwipeByUser(@PathVariable Long userId) {
        if (userId == null) {
            throw new RuntimeException("user_id est absent");
        } else {
            // Récupére le swipe de l'utilisateur
            List<LikeEntity> userSwipe = likeRepository.findByUserId(userId);

            List<Long> likedFilmIds = new ArrayList<>();

            // Parcour les swipes pour extraire les ID des films likés
            for (LikeEntity swipe : userSwipe) {
                if (swipe.isLiked() && !swipe.isWatched()) {
                    likedFilmIds.add(swipe.getFilmId());
                }
            }
            // Récupére les films correspondant aux ID des films aimées
            return movieRepository.findByIdIn(likedFilmIds);
        }
    }

    @GetMapping("/watchedMovie/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieEntity> getAllWatchedMovieByUser(@PathVariable Long userId) {
        if (userId == null) {
            throw new RuntimeException("user_id est absent");
        } else {
            // Récupére le swipe de l'utilisateur
            List<LikeEntity> userSwipe = likeRepository.findByUserId(userId);

            List<Long> likedFilmIds = new ArrayList<>();

            // Parcour les swipes pour extraire les ID des films likés
            for (LikeEntity swipe : userSwipe) {
                if (swipe.isLiked() && swipe.isWatched()) {
                    likedFilmIds.add(swipe.getFilmId());
                }
            }
            // Récupére les films correspondant aux ID des films aimées
            return movieRepository.findByIdIn(likedFilmIds);
        }
    }

    @PutMapping("/watched")
    public ResponseEntity<Void> movieWatch(@RequestBody LikeEntity swipe) {
        try {
            // Recherche du swipe en fonction de l'ID de l'utilisateur et de l'ID du film
            Optional<LikeEntity> optionalSwipe = likeRepository.findByUserIdAndFilmId(swipe.getUserId(), swipe.getFilmId());

            if (optionalSwipe.isPresent()) {
                LikeEntity existingSwipe = optionalSwipe.get();
                // Mettez à jour la propriété isWatched à true
                existingSwipe.setWatched(true);
                likeRepository.save(existingSwipe);
                return ResponseEntity.ok().build();
            } else {
                // Si le swipe n'est pas trouvé, retournez une réponse 404 Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
