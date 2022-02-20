package org.deepforest.dcinside.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QuerydslConfig(@PersistenceContext val entityManager : EntityManager) {
    @Bean
    fun jpaQueryFacotry() = JpaQueryMethodFactory(entityManager);
}
