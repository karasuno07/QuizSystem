package com.fsoft.quizsystem.config;

import com.fsoft.quizsystem.config.listener.UserEntityEventListener;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
@RequiredArgsConstructor
public class HibernateListenerConfig {

    @PersistenceUnit
    private final EntityManagerFactory factory;

    private final UserEntityEventListener userListener;

    @PostConstruct
    protected void listenerRegistry() {
        SessionFactoryImpl sessionFactory
                = factory.unwrap(SessionFactoryImpl.class);

        EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                                                       .getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_UPDATE)
                .appendListeners(userListener);
    }
}
