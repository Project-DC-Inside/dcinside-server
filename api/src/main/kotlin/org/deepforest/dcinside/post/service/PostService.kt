package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.exception.ApiException
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.dto.PostRequestDto
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Transactional(readOnly = true)
@Service
class PostService(
    private val postRepository: PostRepository,
    private val galleryRepository: GalleryRepository,
    private val memberRepository: MemberRepository
) {
    fun findPosts(galleryType: GalleryType?): List<PostResponseDto> =
        postRepository.findByGalleryType(galleryType)
            .stream()
            .map(PostResponseDto::from)
            .collect(Collectors.toList())

    fun findPost(postId: Long): PostResponseDto =
        postRepository.findById(postId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
            .let {
                PostResponseDto.from(it)
            }


    @Transactional
    fun add(postRequestDto: PostRequestDto) {
        val galley: Gallery = galleryRepository.findByGalleryTypeAndName(postRequestDto.galleryType, postRequestDto.galleryName)
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }
        val post: Post = postRequestDto.toEntity(member, galley)
        postRepository.save(post)
    }

    @Transactional
    fun update(postId: Long, postRequestDto: PostRequestDto) {
        val post: Post = postRepository.findById(postId).orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }

        if (post.member.username != member.username) {
            throw ApiException(ErrorCode.FORBIDDEN)
        }

        val galley: Gallery = galleryRepository.findByGalleryTypeAndName(postRequestDto.galleryType, postRequestDto.galleryName)

        post.content = postRequestDto.content
        post.title = postRequestDto.title
        post.gallery = galley
    }

    @Transactional
    fun delete(postId: Long) {
        val post: Post = postRepository.findById(postId).orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }

        if (post.member.username != member.username) {
            throw ApiException(ErrorCode.FORBIDDEN)
        }

        postRepository.delete(post)
    }


}
