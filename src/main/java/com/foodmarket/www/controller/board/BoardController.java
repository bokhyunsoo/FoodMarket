package com.foodmarket.www.controller.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.foodmarket.www.model.board.dto.BoardDTO;
import com.foodmarket.www.model.shop.dto.Pager;
import com.foodmarket.www.service.board.BoardService;

@Controller
@RequestMapping("board/*")
public class BoardController {

	@Inject
	BoardService boardService;
	
	@RequestMapping("list.do")
	public ModelAndView listAll(@RequestParam(defaultValue="1") int curPage, @RequestParam(defaultValue="all") String search_option, @RequestParam(defaultValue="") String keyword) {
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map = new HashMap<>();
		int boardCount = boardService.boardCount(search_option, keyword);
		Pager pager = new Pager(boardCount, curPage);
		int start = pager.getPageBegin();
		int end = pager.getPageEnd();
		List<BoardDTO> list = boardService.boardList(start, end, search_option, keyword);
		map.put("search_option", search_option);
		map.put("keyword", keyword);
		map.put("pager", pager);
		map.put("count", list.size());
		map.put("list", list);
		mav.addObject("map", map);
		mav.setViewName("board/list");
		return mav;
	}
	
	@RequestMapping("write.do")
	public String write() {
		return "board/write";
	}
	
	@RequestMapping("insert.do")
	public String insertBoard(BoardDTO dto, HttpSession session) {
		String userid = (String)session.getAttribute("userid");
		dto.setWriter(userid);
		boardService.insertBoard(dto);
		return "redirect:/board/list.do";
	}
	
	@RequestMapping("view.do")
	public ModelAndView detailBoard(@RequestParam int bno, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		boardService.increaseViewcnt(bno, session);
		mav.addObject("dto", boardService.detailBoard(bno));
		mav.setViewName("board/view");
		return mav;
	}
	
	@RequestMapping("update.do")
	public String updateBoard(BoardDTO dto) {
		boardService.updateBoard(dto);
		return "redirect:/board/list.do";
	}
	
	@RequestMapping("delete.do")
	public String deleteBoard(@RequestParam int bno) {
		boardService.deleteBoard(bno);
		return "redirect:/board/list.do";
	}
}
