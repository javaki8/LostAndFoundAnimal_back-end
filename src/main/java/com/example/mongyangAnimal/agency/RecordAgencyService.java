package com.example.mongyangAnimal.agency;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class RecordAgencyService {
	private String serviceKey = "mjbSlSItCYAFiRSVR2xCQ%2BSJVNW%2BqfnSU%2B9RALx54IWluxDCgII99r29wSYKlw0ds1hNzcf2aT6ANkye4HH1Rw%3D%3D";
	private RecordAgencyRepository repo;

	@Autowired
	public RecordAgencyService(RecordAgencyRepository repo) {
		this.repo = repo;
	}

	@Scheduled(cron = "* 50 * * * *")
	// recordAgency 에서 데이터 요청하는 메소드
	private void getAgencyData() throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append("http://openapi.animal.go.kr/openapi/service/rest");
		builder.append("/recordAgencySrvc/recordAgency");
		builder.append("?pageNo=1");
		builder.append("&numOfRows=1000");
		builder.append("&ServiceKey=" + serviceKey);

		URL url = new URL(builder.toString()); // URL 객체 생성

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		byte[] result = con.getInputStream().readAllBytes();

		String data = new String(result);

		JSONObject json = XML.toJSONObject(data);

		String jsonPrettyPrintString = json.toString(4);

		// JSON String을 Java Object로 변환을 한다.
		RecordAgencyResponse response = new Gson().fromJson(jsonPrettyPrintString, RecordAgencyResponse.class);

		for (RecordAgencyResponse.ResponseItem item : response.getResponse().getBody().getItems().getItem()) {

			RecordAgency agencyList = new RecordAgency(item);

			// ex)<orgAddr>경기도 성남시 수정구 태평동</orgAddr>
			// <orgAddr>경기도 성남시 분당구 동판교로 226 (삼평동, 봇들마을4단지아파트)</orgAddr>

			String sido = agencyList.getAddr();
			String[] sidoArray = sido.split(" ");

			agencyList.setSido(sidoArray[0]);

			String addDo = agencyList.getSido();
			if (addDo.equals("경기")) {
				agencyList.setSido("경기도");
			}

			String gugun = sido.substring(sido.indexOf(" ") + 1);

			agencyList.setGugun(gugun);

			// 데이터가 없으면 추가
			if (repo.findByAddrAndAddrDtl(agencyList.getAddr(), agencyList.getAddrDtl()) == null) {
				repo.save(agencyList);

			}
		}

	}
}
