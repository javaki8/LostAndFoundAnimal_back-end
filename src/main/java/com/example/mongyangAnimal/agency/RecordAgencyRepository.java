package com.example.mongyangAnimal.agency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordAgencyRepository extends JpaRepository<RecordAgency, Long> {
	RecordAgency findByAddr(String addr);

}
