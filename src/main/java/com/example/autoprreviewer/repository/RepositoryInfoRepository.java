package com.example.autoprreviewer.repository;

import com.example.autoprreviewer.model.RepositoryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryInfoRepository extends JpaRepository<RepositoryInfo, Long> {
    // 필요한 추가 쿼리 메서드 정의 가능
}
