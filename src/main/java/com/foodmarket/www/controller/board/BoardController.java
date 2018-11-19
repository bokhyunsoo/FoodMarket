package com.foodmarket.www.controller.board;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.foodmarket.www.model.shop.dto.Pager;
import com.foodmarket.www.service.board.BoardService;

@Controller
@RequestMapping("board/*")
public class BoardController {

	@Inject
	BoardService boardService;
	
	@RequestMapping("list.do")
	public ModelAndView listAll(@RequestParam(defaultValue="1") int curPage) {
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map = new HashMap<>();
		int boardCount = boardService.boardCount();
		Pager pager = new Pager(boardCount, curPage);
		int start = pager.getPageBegin();
		int end = pager.getPageEnd();
		map.put("pager", pager);
		map.put("count", boardCount);
		map.put("list", boardService.boardList(start, end));
		mav.addObject("map", map);
		mav.setViewName("board/list");
		return mav;
	}
}
