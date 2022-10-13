package com.simple.web.controller;

import cn.hutool.core.io.FileUtil;
import com.simple.api.common.Result;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import com.simple.api.user.service.UserService;
import com.simple.api.util.ThreadLocalUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    UserService userService;

    @Value("${avatar.path}")
    String  imgPath;

    @PostMapping("/upload_avatar")
    public Result<String> result(MultipartFile file) throws IOException {
        UserVO user = ThreadLocalUtil.getUserVO();
        String imgName = user.getId() + ".jpg";
        FileUtil.writeFromStream(file.getInputStream(), new File(imgPath + imgName));
        String avatarUrl = "/img/" + imgName;
        User u = new User();
        u.setId(user.getId());
        u.setAvatar(avatarUrl);
        userService.updateUser(u);
        return Result.success(avatarUrl);
    }
}
