package com.example.autoprreviewer.model;

import jakarta.persistence.*;

@Entity
public class RepositoryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoId;
    private String name;
    private String fullName;
    private String owner;

    // 기타 필요한 필드 추가 (예: webhook 등록 상태 등)

    // 기본 생성자, getter, setter
    public RepositoryInfo() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRepoId() {
        return repoId;
    }
    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
