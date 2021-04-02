package com.example.mongyangAnimal.agency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordAgencyController {

	private RecordAgencyRepository repo;

	@Autowired
	RecordAgencyController(RecordAgencyRepository repo) {
		this.repo = repo;
	}

	// 목록조회

	// http://localhost:8080/recordAgency?sido=경기&gugun=성남&page=1&size=1
	@GetMapping(value = "/recordAgency")
	public List<RecordAgency> getAgencyList(@RequestParam("sido") String sido, @RequestParam("gugun") String gugun,
			@RequestParam("page") int page, @RequestParam("size") int size) {

		// findByCategoryContainingAndNameContaining
		// where category like '%abc%' and name like '%%'

		return repo.findBySidoContainingAndGugunContaining(sido, gugun, PageRequest.of(page, size));

		// return feedRepo.findByName(keyword, PageRequest.of(page, size,
		// Sort.by("id").descending()));
	}
}
