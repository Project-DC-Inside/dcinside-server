package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findByGalleryId(galleryId: Long?) : List<Post>
}
