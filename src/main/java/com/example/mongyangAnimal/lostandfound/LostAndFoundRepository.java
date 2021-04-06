package com.example.mongyangAnimal.lostandfound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostAndFoundRepository // <entity타입, id타입>
		extends JpaRepository<LostAndFound, Long> {

	public LostAndFound findByNumber(String number);

	public Page<LostAndFound> findByStatusContaining(String status, Pageable pageable);

}
