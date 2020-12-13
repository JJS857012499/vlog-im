package cn.zd.im.api.controller;


import cn.zd.im.push.DefaultMessagePusher;
import cn.zd.im.server.model.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/message")
public class MessageController  {

	@Resource
	private DefaultMessagePusher defaultMessagePusher;


	@PostMapping(value = "/send")
	public ResponseEntity<Long> send(Message message)  {

		message.setId(System.currentTimeMillis());

		defaultMessagePusher.push(message);

		return ResponseEntity.ok(message.getId());
	}

}
