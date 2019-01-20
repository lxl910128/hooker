package club.projectgaia.hooker.server.impl;

import club.projectgaia.hooker.server.HookerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("HookerServer")
public class HookerServerImpl implements HookerServer {
    private Logger logger = LoggerFactory.getLogger(HookerServer.class);
    @Override
    public String restartHexo(String gitInfo) {
        logger.info(gitInfo);
        return "success";
    }
}
