package com.example.mongyangAnimal.lostandfound;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LostAndFoundService {

	private RabbitTemplate rabbit;
	private LostAndFoundRepository repo;
	private AnimalFileRepository fileRepo;

	@Autowired
	public LostAndFoundService(RabbitTemplate rabbit, LostAndFoundRepository repo, AnimalFileRepository fileRepo) {
		this.rabbit = rabbit;
		this.repo = repo;
		this.fileRepo = fileRepo;
	}

// 관리자페이지로 전송
	public void sendAnimal(LostAndFound lostandfound) {

		System.out.println("-----postAnimal  LOG-----");
		System.out.println(lostandfound);

		try {
			rabbit.convertAndSend("lostandfound.request", lostandfound);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

// 관리자 페이지로 사진 전송
	public void sendAnimalFile(AnimalFile animalFile) {
		System.out.println("-----File  LOG-----");
		System.out.println(animalFile);

		try {
			rabbit.convertAndSend("animal.file", animalFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

// 관리자로 부터 받음
	@RabbitListener(queues = "manager.animal.status")
	public void receiveStatus(LostAndFound lost) {

		System.out.println("---- Manager LOG -----");
		System.out.println(lost);

		// 관리자 lostId를 Id로 / status 만 받아서 저장.
		LostAndFound lostAndFound = repo.findById(lost.getLostId()).orElse(null);
		if (lostAndFound != null) {
			lostAndFound.setStatus(lost.getStatus());
			repo.save(lostAndFound);
		}
	}
}
