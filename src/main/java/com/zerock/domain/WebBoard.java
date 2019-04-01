package com.zerock.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "replies")
@Entity
@Table(name="tbl_webboards")
@EqualsAndHashCode(of="bno")
public class WebBoard {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bno;
	private String title;
	
	private String writer;
	private String content;
	
	@CreationTimestamp
	private Timestamp regdate;
	@CreationTimestamp
	private Timestamp updatedate;

	@OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
	List<WebReply> replies;

}
