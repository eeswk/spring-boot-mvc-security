package com.zerock.persistence;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.zerock.domain.QWebBoard;
import com.zerock.domain.WebBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WebBoardRepository extends CrudRepository<WebBoard, Long>, QuerydslPredicateExecutor<WebBoard> {
	
	
	public default Predicate makePredicate(String type, String keyword) {
		
		BooleanBuilder builder = new BooleanBuilder();
		
		QWebBoard board = QWebBoard.webBoard;
		//type if ~ else
		// bno > 0
		builder.and(board.bno.gt(0));
		
		if (type == null) {
			return builder;
		}
		
		switch (type) {
		case "t":
			builder.and(board.title.like("%" + keyword + "%"));
			break;
		case "c":
			builder.and(board.content.like("%" + keyword + "%"));
			break;
		case "w":
			builder.and(board.writer.like("%" + keyword + "%"));
			break;
		}
		
		return builder;
	}

	@Query("select b.bno, b.title, b.writer, b.regdate, count(r) from WebBoard b "+
			"left outer join b.replies r where b.bno > 0 group by b")
	public List<Object[]> getListWithQuery(Pageable page);

}
