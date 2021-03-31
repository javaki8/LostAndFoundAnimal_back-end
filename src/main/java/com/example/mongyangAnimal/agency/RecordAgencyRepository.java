package com.example.mongyangAnimal.agency;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordAgencyRepository extends JpaRepository<RecordAgency, Long> {

	RecordAgency findByAddrAndAddrDtl(String addr, String addrDtl);

	List<RecordAgency> findBySidoContainingAndGugunContaining(String sido, String gugun, Pageable pageable);
}
