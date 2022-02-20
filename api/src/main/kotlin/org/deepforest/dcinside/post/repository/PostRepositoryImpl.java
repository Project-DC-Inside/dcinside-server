package org.deepforest.dcinside.post.repository;

import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

public class PostRepositoryImpl(
        val queryFactory :JpaQueryCreator
) implements PostRepositoryCustom {
}
