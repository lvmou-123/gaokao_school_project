package com.gaokao.advisor.application.repository;

import com.gaokao.advisor.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserIdOrderByPriorityAsc(Long userId);

    List<Application> findByUserIdAndStatus(Long userId, String status);
}
