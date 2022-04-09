package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.gallery.findByGalleryId
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.member.findByMemberId
import org.deepforest.dcinside.post.dto.PostUpdateDto
import org.deepforest.dcinside.post.dto.PostWrittenByMemberDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostForMemberService(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    private val galleryRepository: GalleryRepository
) {
    fun savePost(dto: PostWrittenByMemberDto, memberId: Long): Long {
        val member = memberRepository.findByMemberId(memberId)
        val gallery = galleryRepository.findByGalleryId(dto.galleryId)
        val post = dto.toEntity(gallery, member)
        return postRepository.save(post).id!!
    }

    fun updatePost(postId: Long, dto: PostUpdateDto, memberId: Long): Long {
        val member = memberRepository.findByMemberId(memberId)
        val post = postRepository.findByPostId(postId)

        check (post.wasNotWrittenBy(member)) {
            "게시글 작성자가 일치하지 않습니다. member: ${member.id}, post.member: ${post.member?.id}"
        }

        post.title = dto.title
        post.content = dto.content
        return post.id!!
    }

    fun deletePost(postId: Long, memberId: Long) {
        val member = memberRepository.findByMemberId(memberId)
        val post = postRepository.findByPostId(postId)

        check (post.wasNotWrittenBy(member)) {
            "게시글 작성자가 일치하지 않습니다. member: ${member.id}, post.member: ${post.member?.id}"
        }

        postRepository.delete(post)
    }
}
