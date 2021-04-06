package com.example.mongyangAnimal.agency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordAgencyController {

	private RecordAgencyRepository repo;

	@Autowired
	RecordAgencyController(RecordAgencyRepository repo) {
		this.repo = repo;
	}

	// 시도+구군 목록조회
	// http://localhost:8080/recordAgency?sido=경기&gugun=성남&page=1&size=1
	@GetMapping(value = "/recordAgency")
	public List<RecordAgency> getAgencyList(@RequestParam("sido") String sido, @RequestParam("gugun") String gugun,
			@RequestParam("page") int page, @RequestParam("size") int size) {

		// findByCategoryContainingAndNameContaining
		// where category like '%abc%' and name like '%%'

		return repo.findBySidoContainingAndGugunContaining(sido, gugun, PageRequest.of(page, size));

	}

	// 시도 목록조회
	// http://localhost:8080/sidoRecordAgency?sido=경기&page=1&size=10
	@GetMapping(value = "/sidoRecordAgency")
	public List<RecordAgency> getAgencySido(@RequestParam("sido") String sido, @RequestParam("page") int page,
			@RequestParam("size") int size) {

		return repo.findBySidoContaining(sido, PageRequest.of(page, size));

	}
}
