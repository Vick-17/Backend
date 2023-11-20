package com.tindMovie.tindMovie.Repository;

import com.tindMovie.tindMovie.Model.MatchEntity;
import com.tindMovie.tindMovie.Model.SwipeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<MatchEntity, Long> {
    List<MatchEntity> findByUserId1AndUserId2OrUserId1AndUserId2(Long userId1, Long userId2, Long userId2Again, Long userId1Again);
}
