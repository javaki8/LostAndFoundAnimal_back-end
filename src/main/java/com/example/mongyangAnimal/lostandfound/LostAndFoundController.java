package com.example.mongyangAnimal.lostandfound;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mongyangAnimal.configuration.ApiConfiguration;

@RestController
public class LostAndFoundController {

	private LostAndFoundRepository repo;
	private AnimalFileRepository fileRepo;
	private final Path FILE_PATH = Paths.get("animal_file");
	private LostAndFoundService service;

	@Autowired
	private ApiConfiguration apiConfig;

	@Autowired
	public LostAndFoundController(LostAndFoundRepository repo, AnimalFileRepository fileRepo,
			LostAndFoundService service) {
		this.repo = repo;
		this.fileRepo = fileRepo;
		this.service = service;
	}

	// 관리자 "승인"된 것만 목록 조회
	// http://localhost:8080/lostandfounds?status=승인&page=1&size=3
	@GetMapping(value = "/lostandfounds")
	public Page<LostAndFound> getLostAndFounds(@RequestParam("status") String status, @RequestParam("page") int page,
			@RequestParam("size") int size) {

		Page<LostAndFound> list = repo.findByStatusContaining(status,
				PageRequest.of(page, size, Sort.by("id").descending()));

		for (LostAndFound lostandfound : list) {
			for (AnimalFile file : lostandfound.getFiles()) {
				file.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + file.getId());
			}
		}

		return list;
	}

	// 글 1건 추가
	@PostMapping(value = "/lostandfounds")
	public LostAndFound createLostAndFound(@RequestBody LostAndFound lostandfound) {

		System.out.println(lostandfound);

		repo.save(lostandfound);
		// 관리자 전송

		service.sendAnimal(lostandfound);
		return lostandfound;

	}

	// 상세보기 {id}
	@GetMapping(value = "/lostandfounds/{id}")
	public @ResponseBody LostAndFound detailAnimal(@PathVariable("id") long id, HttpServletResponse res) {

		LostAndFound lostandfound = repo.findById(id).orElse(null);

		if (lostandfound == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		for (AnimalFile file : lostandfound.getFiles()) {
			file.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + file.getId());
		}

		return lostandfound;
	}

	// 전화번호 조회
	// http://localhost:8080/lostandfounds/search/number?keyword=010-1111-2222
	@GetMapping(value = "/lostandfounds/search/number")
	public @ResponseBody LostAndFound getLostAndFoundsByNumber(@RequestParam("keyword") String keyword,
			HttpServletResponse res) {

		LostAndFound list = repo.findByNumber(keyword);
		// isEmpty() --> null 값 비교
		if (list == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		for (AnimalFile file : list.getFiles()) {
			file.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + file.getId());
		}

		return list;
	}

	// 1건 수정
	@PatchMapping(value = "/lostandfounds/{id}")
	public LostAndFound modifyAnimal

	(@PathVariable("id") long id, @RequestBody LostAndFound lostandfound, HttpServletResponse res) {

		LostAndFound list = repo.findById(id).orElse(null);

		if (lostandfound == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;

		}
		for (AnimalFile file : list.getFiles()) {
			file.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + file.getId());
		}

		repo.save(lostandfound);
		return lostandfound;
	}

	// {id}인 lostAndFound 파일 1개 추가
	@PostMapping(value = "/lostandfounds/{id}/animal-files")
	public AnimalFile createLostAndFoundFile(@PathVariable("id") long id, @RequestPart("data") MultipartFile file,
			HttpServletResponse res) throws IOException {

		if (repo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		System.out.println(file.getOriginalFilename());

		// 디렉토리가 없으면 생성
		if (!Files.exists(FILE_PATH)) {
			Files.createDirectories(FILE_PATH);
		}

		// 파일 저장
		FileCopyUtils.copy(file.getBytes(), new File(FILE_PATH.resolve(file.getOriginalFilename()).toString()));
		// 파일 메타 데이터 저장
		AnimalFile animalFile = AnimalFile.builder().lostfoundId(id).fileName(file.getOriginalFilename())
				.contentType(file.getContentType()).build();

		fileRepo.save(animalFile);
		animalFile.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + animalFile.getId());
		System.out.println("URL : " + animalFile);

		// 관리자 전송
		service.sendAnimalFile(animalFile);
		return animalFile;
	}

	// {id}인 lostAndFound 에 lostandfound-files 목록 조회
	@GetMapping(value = "/lostandfounds/{id}/animal-files")
	public List<AnimalFile> getAnimalFiles(@PathVariable("id") long id, HttpServletResponse res) {

		if (repo.findById(id).orElse(null) == null) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		List<AnimalFile> animalFiles = fileRepo.findBylostfoundId(id);
		for (AnimalFile file : animalFiles) {
			file.setDataUrl(apiConfig.getBasePath() + "/animal-files/" + file.getId());
		}
		System.out.println(animalFiles);

		return animalFiles;
	}

	// 파일 처리
	@GetMapping(value = "/animal-files/{id}")
	public ResponseEntity<byte[]> getFeedFile(@PathVariable("id") long id, HttpServletResponse res) throws IOException {
		AnimalFile animalFile = fileRepo.findById(id).orElse(null);

		if (animalFile == null) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", animalFile.getContentType() + ";charset=UTF-8");

		responseHeaders.set("Content-Disposition",
				"inline; filename=" + URLEncoder.encode(animalFile.getFileName(), "UTF-8"));

		return ResponseEntity.ok().headers(responseHeaders)
				.body(Files.readAllBytes(FILE_PATH.resolve(animalFile.getFileName())));
	}

}