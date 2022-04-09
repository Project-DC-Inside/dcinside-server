package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.gallery.findByGalleryId
import org.deepforest.dcinside.post.dto.PostAccessDto
import org.deepforest.dcinside.post.dto.PostUpdateDto
import org.deepforest.dcinside.post.dto.PostWrittenByNonMemberDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostForNonMemberService(
    private val postRepository: PostRepository,
    private val galleryRepository: GalleryRepository
) {
    fun savePost(dto: PostWrittenByNonMemberDto): Long {
        val gallery = galleryRepository.findByGalleryId(dto.galleryId)
        val post = dto.toEntity(gallery)
        return postRepository.save(post).id!!
    }

    fun updatePost(postId: Long, dto: PostUpdateDto): Long {
        return postRepository.findByPostId(postId).apply {
            this.title = dto.title
            this.content = dto.content
        }.id!!
    }

    fun deletePost(postId: Long) {
        postRepository.findByPostId(postId).also {
            postRepository.delete(it)
        }
    }

    @Transactional(readOnly = true)
    fun checkAccess(postAccessDto: PostAccessDto): Boolean {
        return postRepository.findByPostId(postAccessDto.postId)
            .isSamePassword(postAccessDto.password)
    }
}
