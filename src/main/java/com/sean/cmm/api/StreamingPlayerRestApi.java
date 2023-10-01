package com.sean.cmm.api;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/media")
public class StreamingPlayerRestApi {

    @GetMapping("/list")
    public List<Object> getMedias(){
        return Collections.emptyList();
    }

    @PostMapping("/play")
    public void playMedia(@RequestBody JSONObject body){
        String url = body.getString("url");
    }
}
