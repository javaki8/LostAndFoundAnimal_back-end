package com.example.mongyangAnimal.agency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecordAgency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String addrDtl; // 상세주소
	private String orgNm; // 업체명
	private String tel; // 업체 전화번호
	private String addr; // 주소
	private String sido;
	private String gugun;

	public RecordAgency(RecordAgencyResponse.ResponseItem res) {

		this.addr = res.getOrgAddr();
		this.addrDtl = res.getOrgAddrDtl();
		this.orgNm = res.getOrgNm();
		this.tel = res.getTel();
		this.sido = res.getSido();
		this.gugun = res.getGugun();
	}
}
