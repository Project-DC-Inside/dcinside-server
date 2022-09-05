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

    @Column(name = "nickname", updatable = false)
    val nickname: String,

    @Column(name = "title")
    var title: String,

    @Column(name = "content", columnDefinition = "text")
    var content: String,

    @Column(name = "password", updatable = false)
    val password: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    val gallery: Gallery,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL])
    val images: MutableList<PostImage> = mutableListOf()

) : BaseEntity() {

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val statistics: PostStatistics = PostStatistics(post = this)

    val writtenByNonMember: Boolean get() = (member == null)

    fun wasNotWrittenBy(other: Member): Boolean = (other == this.member)

    fun isSamePassword(otherPassword: String): Boolean = (otherPassword == this.password)
}
