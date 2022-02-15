package com.fsoft.quizsystem.config.listener;

import com.fsoft.quizsystem.object.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserEntityEventListener implements PostUpdateEventListener {

    private final RedissonClient redissonClient;
    private RMapCache<Long, User> userMap;

    @PostConstruct
    public void init() {
        userMap = redissonClient.getMapCache("USER_MAP");
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof User) {
            log.info("ON UPDATED User: " + event.getEntity());
            User user = (User) event.getEntity();
            userMap.replace(user.getId(), user);
        }
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        log.info(persister.getEntityName());
        return true;
    }
}
