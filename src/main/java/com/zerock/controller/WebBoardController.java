package com.zerock.controller;

import com.zerock.domain.WebBoard;
import com.zerock.persistence.CustomCrudRepository;
import com.zerock.vo.PageMaker;
import com.zerock.vo.PageVO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {
	
/*
	@Autowired
	private WebBoardRepository repo;
*/
	@Autowired
	private CustomCrudRepository repo;

/*
	@GetMapping("/list")
	public void list(
			@PageableDefault(
					direction=Sort.Direction.DESC,
					sort="bno",
					size=10,
					page=0) Pageable page) {
		log.info("list() called..." + page);
	}
*/
/*
	@GetMapping("/list")
	public void list(PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");
		Page<WebBoard> result = repo.findAll(
				repo.makePredicate(vo.getType(), vo.getKeyword()), page);
		log.info("" + page);
		log.info("" + result);
		log.info("" + result.getContent());
		log.info("Total Page Number: " + result.getTotalPages() );
		model.addAttribute("result", new PageMaker(result));
	}
	*/
	@GetMapping("/list")
	public void list(PageVO vo, Model model) {
		Pageable page = vo.makePageable(0, "bno");

		Page<Object[]> result = repo.getCustomPage(vo.getType(), vo.getKeyword(), page);
		log.info("" + page);
		log.info("" + result);
		log.info("Total Page Number: " + result.getTotalPages() );

		model.addAttribute("result", new PageMaker(result));
	}

	@GetMapping("/register")
	public void regsiterGET(@ModelAttribute("vo") WebBoard vo) {
		log.info("register get");
	}

	@PostMapping("/register")
	public String regsterPOST(@ModelAttribute("vo") WebBoard vo, RedirectAttributes rttr) {
		log.info("register post");
		log.info("" + vo);

		repo.save(vo);
		rttr.addFlashAttribute("msg", "success");
		return "redirect:/boards/list";
	}

	@GetMapping("/view")
	public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("" + bno);
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}

	@Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
	@GetMapping("/modify")
	public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("modify bno: " + bno);
		repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));
	}

	@Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
	@PostMapping("/modify")
	public String modifyPOST(WebBoard board, PageVO vo, RedirectAttributes rttr) {
		log.info("modify WebBoard: " + board);

		repo.findById(board.getBno()).ifPresent(origin -> {
			origin.setTitle(board.getTitle());
			origin.setContent(board.getContent());

			repo.save(origin);
			rttr.addFlashAttribute("msg", "success");
			rttr.addAttribute("bno", origin.getBno());
		});

		rttr.addAttribute("page", vo.getPage());
		rttr.addAttribute("size", vo.getSize());
		rttr.addAttribute("type", vo.getType());
		rttr.addAttribute("keyword", vo.getKeyword());

		return "redirect:/boards/view";
	}

	@Secured(value = {"ROLE_BASIC", "ROLE_MANAGER", "ROLE_ADMIN"})
	@PostMapping("/delete")
	public String delete(Long bno, PageVO vo, RedirectAttributes rttr) {
		log.info("delete bno: " + bno);
		repo.deleteById(bno);
		rttr.addFlashAttribute("msg","success");
		rttr.addFlashAttribute("page", vo.getPage());
		rttr.addFlashAttribute("size", vo.getSize());
		rttr.addFlashAttribute("type", vo.getType());
		rttr.addFlashAttribute("keyword", vo.getKeyword());

		return "redirect:/boards/list";
	}



	
}
