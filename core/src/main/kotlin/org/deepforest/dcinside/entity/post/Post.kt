package org.deepforest.dcinside.entity.post

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.member.Member
import javax.persistence.*

@Table(name = "post", indexes = [Index(columnList = "created_at")])
@Entity
class Post(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    var gallery : Gallery,

    @Column(updatable = false)
    val nickname : String,

    var title: String,

    @Column(name = "content", columnDefinition="text")
    var content: String,

    @Column(updatable = false)
    val password: String,

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var statistics: PostStatistics? = null
) : BaseEntity()

