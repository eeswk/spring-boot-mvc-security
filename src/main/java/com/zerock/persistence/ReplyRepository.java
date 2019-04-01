package com.zerock.persistence;

import com.zerock.domain.WebBoard;
import com.zerock.domain.WebReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReplyRepository extends CrudRepository<WebReply, Long> {

    @Query("select r from WebReply r where r.board = ?1 " +
            " and  r.rno > 0 order by r.rno asc ")
    public List<WebReply> getRepliesOfBoard(WebBoard board);
}
