package com.gaokao.advisor.favorite.repository;

import com.gaokao.advisor.favorite.entity.SchoolFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<SchoolFavorite, Long> {

    Optional<SchoolFavorite> findByUserIdAndSchoolId(Long userId, Long schoolId);

    boolean existsByUserIdAndSchoolId(Long userId, Long schoolId);

    Page<SchoolFavorite> findByUserId(Long userId, Pageable pageable);

    List<SchoolFavorite> findByUserIdAndSchoolIdIn(Long userId, List<Long> schoolIds);

    void deleteByUserIdAndSchoolId(Long userId, Long schoolId);

    long countByUserId(Long userId);
}
