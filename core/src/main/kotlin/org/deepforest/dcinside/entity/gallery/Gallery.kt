package org.deepforest.dcinside.entity.gallery

import org.deepforest.dcinside.entity.BaseEntity
import org.deepforest.dcinside.entity.post.Post
import javax.persistence.*

@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["gallery_type", "gallery_name"])
    ]
)
@Entity
class Gallery(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "gallery_type")
    val type: GalleryType,

    @Column(name = "gallery_name")
    val name: String,

    @OneToMany(mappedBy = "gallery")
    val posts: MutableList<Post> = mutableListOf(),
) : BaseEntity()

enum class GalleryType {
    MAJOR, MINOR, MINI
}
