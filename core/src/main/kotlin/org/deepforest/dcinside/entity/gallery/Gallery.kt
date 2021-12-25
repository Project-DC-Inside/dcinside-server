package org.deepforest.dcinside.entity.gallery

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.post.Post
import javax.persistence.*

@Entity
class Gallery(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "gallery_type")
    val type: GalleryType,

    @Column(name = "gallery_name")
    val name: String,

    @OneToMany(mappedBy = "gallery")
    val posts: MutableList<Post>
) : BaseEntity()

enum class GalleryType {
    MAJOR, MINOR, MINI
}