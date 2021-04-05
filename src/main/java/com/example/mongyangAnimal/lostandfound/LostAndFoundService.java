package com.example.mongyangAnimal.lostandfound;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LostAndFoundService {

	private RabbitTemplate rabbit;
	private LostAndFoundRepository repo;

	@Autowired
	public LostAndFoundService(RabbitTemplate rabbit, LostAndFoundRepository repo) {
		this.rabbit = rabbit;
		this.repo = repo;
	}

// 관리자페이지로 전송
	public void sendAnimal(LostAndFound lostandfound) {

		System.out.println("-----postAnimal-----");
		System.out.println(lostandfound);

		try {
			rabbit.convertAndSend("lostandfound.request", lostandfound);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

// 받는것
	@RabbitListener(queues = "manager.animal.status")
	public void receiveStatus(LostAndFound lostandfound) {
		System.out.println("---- Manager LOG -----");
		System.out.println(lostandfound);
		repo.save(lostandfound);
	}
}
