package com.tindMovie.tindMovie.Repository;

import com.tindMovie.tindMovie.Model.LikeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends CrudRepository<LikeEntity, Long> {
    boolean existsByUserIdAndFilmId(Long userId, Long filmId);

    List<LikeEntity> findFilmIdsByUserId(Long userId);

    List<LikeEntity> findByUserId(Long userId);

    Optional<LikeEntity> findByUserIdAndFilmId(Long id, Long filmId);

    LikeEntity findByFilmId(Long swipeId);
}
