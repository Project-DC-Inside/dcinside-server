package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.post.Post
import org.springframework.data.jpa.repository.JpaRepository

fun PostRepository.findByPostId(id: Long): Post =
    findById(id).orElseThrow { NoSuchElementException("id: $id 게시글 정보가 없습니다") }

fun PostRepository.findByGalleryWithPaging(gallery: Gallery, postId: Long = Long.MAX_VALUE): List<Post> =
    findTop20ByGalleryAndIdLessThanOrderByIdDesc(gallery, postId)

interface PostRepository : JpaRepository<Post, Long> {
    fun findTop20ByGalleryAndIdLessThanOrderByIdDesc(gallery: Gallery, postId: Long): List<Post>
}
