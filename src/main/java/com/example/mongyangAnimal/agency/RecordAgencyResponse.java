package com.example.mongyangAnimal.agency;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordAgencyResponse {
	private ResponseData response;

	@Data
	@NoArgsConstructor
	class ResponseData {

		private ResponseBody body;

	}

	@Data
	@NoArgsConstructor
	class ResponseBody {
		private int totalCount;
		private ResponseItems items;
	}

	@Data
	@NoArgsConstructor
	class ResponseItems {
		private List<ResponseItem> item;
	}

	@Data
	@NoArgsConstructor
	class ResponseItem {
		private String addrDtl; // 상세주소
		private String orgNm; // 업체명
		private String tel; // 업체 전화번호
		private String addr; // 주소
	}
}
