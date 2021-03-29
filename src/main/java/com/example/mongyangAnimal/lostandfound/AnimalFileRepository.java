package com.example.mongyangAnimal.lostandfound;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalFileRepository extends JpaRepository<AnimalFile, Long> {

	public List<AnimalFile> findBylostfoundId(long lostfoundId);

}
