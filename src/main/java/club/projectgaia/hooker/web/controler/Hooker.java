package club.projectgaia.hooker.web.controler;

import club.projectgaia.hooker.server.HookerServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class Hooker {
    @Autowired
    HookerServer hookerServer;

    @PostMapping("/gitHook")
    public String restartHexo(@RequestBody String gitInfo){
        return hookerServer.restartHexo(gitInfo);
    }
}
