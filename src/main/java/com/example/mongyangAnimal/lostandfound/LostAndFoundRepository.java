package com.example.mongyangAnimal.lostandfound;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository: entity를 처리하는 객체
@Repository
public interface LostAndFoundRepository // <entity타입, id타입>
		extends JpaRepository<LostAndFound, Long> {

	// public LostAndFound findByName(String name);

	public LostAndFound findByNumber(String number);

	// public List<LostAndFound> findByName(String name, Pageable pageable);

}
