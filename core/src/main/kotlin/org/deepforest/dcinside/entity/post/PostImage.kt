package org.deepforest.dcinside.entity.post

import org.deepforest.dcinside.entity.BaseEntity
import javax.persistence.*

@Table(name = "post_image")
@Entity
class PostImage(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    val id: Long? = null,

    @Column(name = "number")
    val number: Int,

    @Column(name = "url")
    val url: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post
) : BaseEntity()
