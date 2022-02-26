package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.dto.ErrorCode
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.exception.ApiException
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.gallery.findByGalleryId
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.dto.PostAccessDto
import org.deepforest.dcinside.post.dto.PostRequestDto
import org.deepforest.dcinside.post.dto.PostResponseDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostService(
    private val postRepository: PostRepository,
    private val postStatisticsService: PostStatisticsService,
    private val galleryRepository: GalleryRepository,
    private val memberRepository: MemberRepository,
    private val memberPostStatisticsService: MemberPostStatisticsService
) {

    @Transactional(readOnly = true)
    fun findPostsByGalleryId(galleryId: Long?): List<PostResponseDto> =
        postRepository.findByGalleryId(galleryId)
            .asSequence()
            .map(PostResponseDto::from)
            .toList()

    fun findPost(postId: Long): PostResponseDto =
        postRepository.findById(postId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
            .let {
                postStatisticsService.view(it.id!!)
                PostResponseDto.from(it)
            }


    fun add(postRequestDto: PostRequestDto) {
        val galley: Gallery = galleryRepository.findByGalleryId(postRequestDto.galleryId)
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }

        val post: Post = postRequestDto.toEntity(member, galley)
        postRepository.save(post)
        postStatisticsService.save(post)
    }

    fun update(postId: Long, postRequestDto: PostRequestDto) {
        val post: Post = postRepository.findById(postId).orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }

        if (post.member.username != member.username) {
            throw ApiException(ErrorCode.FORBIDDEN)
        }

        val galley: Gallery = galleryRepository.findByGalleryId(postRequestDto.galleryId)

        post.content = postRequestDto.content
        post.title = postRequestDto.title
        post.gallery = galley
    }

    fun delete(postId: Long) {
        val post: Post = postRepository.findById(postId).orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
        val member: Member = memberRepository.findById(SecurityUtil.getCurrentMemberId())
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }

        if (post.member.username != member.username) {
            throw ApiException(ErrorCode.FORBIDDEN)
        }

        postRepository.delete(post)
    }

    fun dislikePost(postId: Long) {
        val currentMemberId = SecurityUtil.getCurrentMemberId()
        val member: Member = memberRepository.findById(currentMemberId)
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }
        val postStatistics = postStatisticsService.findPostStatistics(postId)

        when (val memberPostStatistics = memberPostStatisticsService.find(currentMemberId, postStatistics.id!!)) {
            null -> {
                postStatisticsService.dislike(postId)
                memberPostStatisticsService.addDislike(true, member, postStatistics)
            }
            else -> {
                if (memberPostStatistics.liked == true) {
                    throw ApiException(ErrorCode.CONFLICT)
                }

                if (memberPostStatistics.disliked == true) {
                    postStatisticsService.cancelDislike(postId)
                    memberPostStatistics.disliked = false
                } else {
                    postStatisticsService.dislike(postId)
                    memberPostStatistics.disliked = true
                }
            }
        }
    }

    fun likePost(postId: Long) {
        val currentMemberId = SecurityUtil.getCurrentMemberId()
        val member: Member = memberRepository.findById(currentMemberId)
            .orElseThrow { ApiException(ErrorCode.UNAUTHORIZED_SECURITY_CONTEXT) }
        val postStatistics = postStatisticsService.findPostStatistics(postId)

        when (val memberPostStatistics = memberPostStatisticsService.find(currentMemberId, postStatistics.id!!)) {
            null -> {
                postStatisticsService.like(postId)
                memberPostStatisticsService.addLike(true, member, postStatistics)
            }
            else -> {
                if (memberPostStatistics.disliked == true) {
                    throw ApiException(ErrorCode.CONFLICT)
                }

                if (memberPostStatistics.liked == true) {
                    postStatisticsService.cancelLike(postId)
                    memberPostStatistics.liked = false
                } else {
                    postStatisticsService.like(postId)
                    memberPostStatistics.liked = true
                }
            }
        }
    }

    fun access(postAccessDto: PostAccessDto): Boolean {
        val postId = postAccessDto.postId
        val password = postAccessDto.password

        val post = postRepository.findById(postId).orElseThrow { ApiException(ErrorCode.NOT_FOUND) }

        return post.password == password
    }
}
