package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Message;
import com.example.springboot.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 *  留言相关接口
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 新增
     */
    @PostMapping
    public Result add(@RequestBody Message message) {
        messageService.add(message);
        return Result.success(message);
    }

    /**
     * 根据ID删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        messageService.delete(id);
        return Result.success();
    }

    /**
     * 查询所有
     */
    @GetMapping
    public Result all() {
        return Result.success(messageService.findAll());
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Result page(@RequestBody Message search,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.success(messageService.findPage(search, pageNum, pageSize));
    }
}
