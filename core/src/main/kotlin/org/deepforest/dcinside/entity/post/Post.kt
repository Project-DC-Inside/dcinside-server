package org.deepforest.dcinside.entity.post

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.member.Member
import javax.persistence.*

@Table(name = "post", indexes = [Index(columnList = "created_at")])
@Entity
class Post(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    val gallery : Gallery,

    @Column(updatable = false)
    val nickname : String,

    val title: String,

    @Column(name = "content", columnDefinition="text")
    val content: String,

    @Column(updatable = false)
    val password: String,

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var statistics: PostStatistics? = null,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val id: Long = 0L
) : BaseEntity()

