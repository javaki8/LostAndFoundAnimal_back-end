package com.example.mongyangAnimal.lostandfound;

//@Service
public class LostAndFoundService {

	// private RabbitTemplate rabbit;
	// private LostAndFoundRepository repo;

//
//	@Autowired
//	public LostAndFoundService(RabbitTemplate rabbit, LostAndFoundRepository repo) {
//		this.rabbit = rabbit;
//		this.repo= repo;
//	}
//	
//	public void sendAnimal(LostAndFound request) {
//		System.out.println("-----postAnimal-----");
//		System.out.println(request);
//		
//		try {
//			rabbit.convertAndSend("큐이름.request", request);
//		} catch(Exception e) {
//			System.out.println(e.getMessage());
//		}
//		
//	}
//	
//
//	@RabbitListener(queues = "manager.application.status")
//	public void 분실보호다시받는거(분실보호 request) {
//		System.out.println("---- Manager LOG -----");
//		System.out.println(request);
//	}
//}
}
