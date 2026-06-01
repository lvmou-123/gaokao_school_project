package com.gaokao.advisor.major.repository;

import com.gaokao.advisor.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long>,
        JpaSpecificationExecutor<Major> {

    @Query("select m from Major m join m.schools s where s.id = :schoolId")
    List<Major> findBySchoolId(Long schoolId);

    List<Major> findByCategory(String category);

    List<Major> findByName(String name);
}
