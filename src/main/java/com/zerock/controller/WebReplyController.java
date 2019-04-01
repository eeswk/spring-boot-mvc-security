package com.zerock.controller;

import com.zerock.domain.WebBoard;
import com.zerock.domain.WebReply;
import com.zerock.persistence.ReplyRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/replies")
@Log
public class WebReplyController {

    @Autowired
    private ReplyRepository replyRepository;

    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @PostMapping("/{bno}")
    public ResponseEntity<List<WebReply>> addReply(@PathVariable("bno") Long bno, @RequestBody WebReply reply) {
        log.info("addReply....");
        log.info("bno: " + bno);
        log.info("reply: "+ reply);

        WebBoard board = new WebBoard();
        board.setBno(bno);

        reply.setBoard(board);
        replyRepository.save(reply);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.CREATED);
    }

    private List<WebReply> getListByBoard(WebBoard board ) {
        log.info("getListByBoard..." + board);
        return replyRepository.getRepliesOfBoard(board);
    }

    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @DeleteMapping("/{bno}/{rno}")
    public ResponseEntity<List<WebReply>> remove(
            @PathVariable("bno") Long bno,
            @PathVariable("rno") Long rno) {

        log.info("delete reply: "+ rno);
        replyRepository.deleteById(rno);

        WebBoard board = new WebBoard();
        board.setBno(bno);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }

    @Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
    @Transactional
    @PutMapping("/{bno}")
    public ResponseEntity<List<WebReply>> modify(@PathVariable("bno")Long bno, @RequestBody WebReply reply) {
        log.info("modify reply: "+ reply);
        replyRepository.findById(reply.getRno()).ifPresent(origin -> {
            origin.setReplyText(reply.getReplyText());
            replyRepository.save(origin);
        });

        WebBoard board = new WebBoard();
        board.setBno(bno);

        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }

    @GetMapping("/{bno}")
    public ResponseEntity<List<WebReply>> getReplies(@PathVariable("bno") Long bno) {
        log.info("get All Replies...");

        WebBoard board = new WebBoard();
        board.setBno(bno);
        return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    }


}
