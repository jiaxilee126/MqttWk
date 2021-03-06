package cn.wizzer.iot.mqtt.server.store.message;

import cn.wizzer.iot.mqtt.server.common.message.IMessageIdService;
import org.nutz.integration.jedis.RedisService;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by wizzer on 2018
 */
@IocBean(create = "init")
public class MessageIdService implements IMessageIdService {

    @Inject
    private RedisService redisService;

    @Override
    public int getNextMessageId() {
        try {
            int nextMsgId = (int) (redisService.incr("mqttwk:messageid:num") % 65536);
            if (nextMsgId == 0)
                return this.getNextMessageId();
            return nextMsgId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 每次重启的时候初始化
     */
    public void init() {
        redisService.del("mqttwk:messageid:num");
    }
}
