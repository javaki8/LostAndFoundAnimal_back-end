package com.example.mongyangAnimal.lostandfound;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LostAndFound {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String status; // 관리자 승인/거절

	@Column(columnDefinition = "TEXT")

	private long lostId; // 관리자ID

	private String name; // 사용자이름

	private String area; // 지역

	private String color; // 털색

	private String gender; // 성별

	private String number; // 연락처

	private String state; // 분실or보호

	private String date; // 날짜

	private String content; // 특징

	private String type; // 축종(종류)

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "lostfoundId")
	private List<AnimalFile> files;
}
