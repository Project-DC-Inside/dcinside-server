package org.deepforest.dcinside.post.service

import org.deepforest.dcinside.common.SecurityUtil
import org.deepforest.dcinside.post.dto.*
import org.springframework.stereotype.Service

@Service
class PostService(
    val postReadQueryService: PostReadQueryService,
    val postReadService: PostReadService,
    val postForMemberService: PostForMemberService,
    val postForNonMemberService: PostForNonMemberService,
) {
    fun findPostsByGalleryId(
        galleryId: Long,
        lastPostId: Long? = Long.MAX_VALUE
    ): List<PostResponseDto> {
        return postReadQueryService.findPostsByGalleryId(galleryId, lastPostId)
    }

    fun findPostByIdAndIncreaseViewCount(postId: Long): PostResponseDto {
        return postReadService.findPostByIdAndIncreaseViewCount(postId)
    }

    fun savePost(dto: PostWrittenByMemberDto, memberId: Long): Long {
        return postForMemberService.savePost(dto, memberId)
    }

    fun updatePost(postId: Long, dto: PostUpdateDto, memberId: Long): Long {
        return postForMemberService.updatePost(postId, dto, memberId)
    }

    fun savePost(dto: PostWrittenByNonMemberDto): Long {
        return postForNonMemberService.savePost(dto)
    }

    fun updatePost(postId: Long, dto: PostUpdateDto): Long {
        return postForNonMemberService.updatePost(postId, dto)
    }

    fun deletePost(postId: Long) {
        postForNonMemberService.deletePost(postId)
    }

    fun deletePost(postId: Long, memberId: Long) {
        postForMemberService.deletePost(postId, SecurityUtil.getCurrentMemberId())
    }

    fun checkAccess(dto: PostAccessDto): Boolean {
        return postForNonMemberService.checkAccess(dto)
    }
}