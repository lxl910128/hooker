package club.projectgaia.hooker.web.controler;

import club.projectgaia.hooker.server.HookerServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Hooker {
    @Autowired
    HookerServer hookerServer;

    @PostMapping("/gitHook")
    public String restartHexo(@RequestBody(required = false) String gitInfo) {
        return hookerServer.restartHexo(gitInfo);
    }
}
