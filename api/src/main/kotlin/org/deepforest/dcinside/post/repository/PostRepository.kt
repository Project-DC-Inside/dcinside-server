package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long>,  PostRepositoryCustom{
    fun findByGalleryType(galleryType: GalleryType?)
}
