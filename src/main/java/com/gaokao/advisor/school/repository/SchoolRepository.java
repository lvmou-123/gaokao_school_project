package com.gaokao.advisor.school.repository;

import com.gaokao.advisor.school.entity.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>,
        JpaSpecificationExecutor<School> {

    List<School> findByProvince(String province);

    List<School> findByNameContaining(String keyword);

    Page<School> findByIdIn(List<Long> ids, Pageable pageable);
}
