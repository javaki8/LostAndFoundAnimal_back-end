package com.example.mongyangAnimal.agency;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RecordAgencyService {
	private String serviceKey = "mjbSlSItCYAFiRSVR2xCQ%2BSJVNW%2BqfnSU%2B9RALx54IWluxDCgII99r29wSYKlw0ds1hNzcf2aT6ANkye4HH1Rw%3D%3D";
	private RecordAgencyRepository repo;

	@Autowired
	public RecordAgencyService(RecordAgencyRepository repo) {
		this.repo = repo;
	}

	@Scheduled(cron = "0 * * * * *")
	public void requestDustHourlyDate() throws IOException {
		getAgencyData("경기도");

	}

	private void getAgencyData(String addr) throws IOException {
		System.out.println(new Date().toLocaleString() + ":" + addr);

		StringBuilder builder = new StringBuilder();
		builder.append("http://openapi.animal.go.kr/openapi/service/rest");
		builder.append("/recordAgencySrvc/recordAgency");
		builder.append("?addr=" + addr);
		builder.append("&pageNo=1");
		builder.append("&numOfRows=50");
		builder.append("&ServiceKey=" + serviceKey);

		URL url = new URL(builder.toString());

		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		byte[] result = con.getInputStream().readAllBytes();

		String data = new String(result);
		JSONObject json = XML.toJSONObject(data);

		System.out.println(json);
	}
}
