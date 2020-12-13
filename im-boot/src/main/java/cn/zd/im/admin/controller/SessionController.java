package cn.zd.im.admin.controller;

import cn.zd.im.service.IMSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/console/session")
public class SessionController {

	@Resource
	private IMSessionService IMSessionService;
	
	@GetMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("sessionList", IMSessionService.list());
		return "console/session/manage";
	}
}
