package com.example.mongyangAnimal.lostandfound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mongyangAnimal.configuration.ApiConfiguration;

@RestController
public class LostAndFoundController {

	private LostAndFoundRepository repo;
	private AnimalFileRepository fileRepo;
	private final Path FILE_PATH = Paths.get("animal_file");

	@Autowired
	private ApiConfiguration apiConfig;

	@Autowired // @Repository에 해당하는 인터페이스
				// 구현체(object, instance <- class)를
				// 생성해서 주입
	public LostAndFoundController(LostAndFoundRepository repo, AnimalFileRepository fileRepo) {
		this.repo = repo;
		this.fileRepo = fileRepo;
	}

	// 목록조회
	@RequestMapping(value = "/lostandfounds", method = RequestMethod.GET)
	public List<LostAndFound> getLostAndFounds(HttpServletRequest req) {

		List<LostAndFound> list = repo.findAll(Sort.by("id").descending());
		for (LostAndFound lostandfound : list) {
			for (AnimalFile file : lostandfound.getFiles()) {
				file.setDataUrl(apiConfig.getBasePath() + "/lostandfound-files/" + file.getId());
			}
		}

		return list;
	}

	// 전화번호 조회
	// http://localhost:8080/lostandfounds/search/number?keyword=010-1111-2222
	@GetMapping(value = "/lostandfounds/search/number")
	public List<LostAndFound> getLostAndFoundsByNumber(@RequestParam("keyword") String keyword) {
		return repo.findByNumber(keyword);
	}

	// 1건 추가
	@RequestMapping(value = "/lostandfounds", method = RequestMethod.POST)
	public LostAndFound createLostAndFound(@RequestBody LostAndFound lostandfound) {

		repo.save(lostandfound);
		return lostandfound;
	}

	// {id}인 feed 파일 1개 추가
	@RequestMapping(value = "/lostandfounds/{id}/animal-files", method = RequestMethod.POST)
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
		animalFile.setDataUrl(apiConfig.getBasePath() + "/lostandfound-files/" + animalFile.getId());
		return animalFile;
	}

	// 전단지
//	@RequestMapping(value = "/lostandfounds", method = RequestMethod.GET)
//	public LostAndFound getLostAndFound(@PathVariable("id") long id) {
//
//		List<LostAndFound> list = repo.findAll(Sort.by("id").descending());
//		for (LostAndFound lostandfound : list) {
//			for (AnimalFile file : lostandfound.getFiles()) {
//				file.setDataUrl(apiConfig.getBasePath() + "/lostandfound-files/" + file.getId());
//			}
//		}
//
//		return;
//	}

}
