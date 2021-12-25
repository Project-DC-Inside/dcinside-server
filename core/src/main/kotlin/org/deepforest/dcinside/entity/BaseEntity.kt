package org.deepforest.dcinside.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity {
    @CreatedDate
    @Column(name = "createdAt")
    var createdAt: LocalDateTime = LocalDateTime.MIN

    @LastModifiedDate
    @Column(name = "updatedAt")
    var updatedAt: LocalDateTime = LocalDateTime.MIN
}
