package org.deepforest.dcinside.post

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.member.Member
import org.deepforest.dcinside.entity.member.MemberRole
import org.deepforest.dcinside.entity.post.Post
import org.deepforest.dcinside.gallery.GalleryRepository
import org.deepforest.dcinside.member.MemberRepository
import org.deepforest.dcinside.post.dto.PostUpdateDto
import org.deepforest.dcinside.post.dto.PostWrittenByMemberDto
import org.deepforest.dcinside.post.repository.PostRepository
import org.deepforest.dcinside.post.repository.findByPostId
import org.deepforest.dcinside.post.service.PostForMemberService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@SpringBootTest
@Transactional
class PostForMemberServiceTest {

    @Autowired
    private lateinit var postForMemberService: PostForMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    @DisplayName("회원이 작성한 글 저장")
    fun testSavePostByRealMember() {
        // given
        val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
        memberRepository.saveAndFlush(member)

        val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
        galleryRepository.saveAndFlush(gallery)

        // when
        val dto = PostWrittenByMemberDto(gallery.id!!, "title", "content")
        val savedPostId = postForMemberService.savePost(dto, member.id!!)

        // then
        val findPost = postRepository.findByPostId(savedPostId)
        assertThat(findPost.member).isEqualTo(member)
        assertThat(findPost.nickname).isEqualTo(member.nickname)
        assertThat(findPost.password).isNull()
        assertThat(findPost.writtenByNonMember).isFalse

        assertThat(findPost.gallery).isEqualTo(gallery)
        assertThat(findPost.title).isEqualTo(dto.title)
        assertThat(findPost.content).isEqualTo(dto.content)

        assertThat(findPost.statistics.id).isNotNull
        assertThat(findPost.statistics.viewCount).isEqualTo(0L)
        assertThat(findPost.statistics.likeCount).isEqualTo(0L)
        assertThat(findPost.statistics.dislikeCount).isEqualTo(0L)
        assertThat(findPost.statistics.commentCount).isEqualTo(0L)
    }

    @Nested
    @DisplayName("게시글 수정 관련 성공/실패 테스트")
    inner class UpdatePost {

        @Test
        @DisplayName("게시글 수정 성공 테스트")
        fun updateSuccess() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "title", "content", null, member, gallery)
            postRepository.saveAndFlush(post)

            // when
            val dto = PostUpdateDto("new-title", "new-content")
            postForMemberService.updatePost(post.id!!, dto, member.id!!)

            // then
            val findPost = postRepository.findByPostId(post.id!!)
            assertThat(findPost.member).isEqualTo(member)
            assertThat(findPost.title).isEqualTo(dto.title)
            assertThat(findPost.content).isEqualTo(dto.content)
        }

        @Test
        @DisplayName("현재 사용자와 수정하려는 게시글 작성자가 일치하지 않아서 실패")
        fun updateFailWhenMemberIsDifferent() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)
            val otherMember = Member(null, "username-2", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(otherMember)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "title", "content", null, member, gallery)
            postRepository.saveAndFlush(post)

            // when
            val dto = PostUpdateDto("new-title", "new-content")

            assertThatExceptionOfType(IllegalStateException::class.java)
                .isThrownBy {
                    postForMemberService.updatePost(post.id!!, dto, otherMember.id!!)
                }
        }
    }

    @Nested
    @DisplayName("게시글 삭제 관련 성공/실패 테스트")
    inner class DeletePost {

        @Test
        @DisplayName("게시글 삭제 성공")
        fun deleteSuccess() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "title", "content", null, member, gallery)
            postRepository.saveAndFlush(post)

            // when
            postForMemberService.deletePost(post.id!!, member.id!!)

            // then
            assertThatExceptionOfType(NoSuchElementException::class.java)
                .isThrownBy {
                    postRepository.findByPostId(post.id!!)
                }
                .withMessage("id: ${post.id} 게시글 정보가 없습니다")
        }

        @Test
        @DisplayName("현재 사용자와 삭제하려는 게시글 작성자가 일치하지 않아서 실패")
        fun deleteFailWhenMemberIsDifferent() {
            // given
            val member = Member(null, "username", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(member)
            val otherMember = Member(null, "username-2", "email", "nickname", "password", MemberRole.ROLE_FIXED)
            memberRepository.saveAndFlush(otherMember)

            val gallery = Gallery(null, GalleryType.MAJOR, "major-gallery")
            galleryRepository.saveAndFlush(gallery)

            val post = Post(null, "nickname", "title", "content", null, member, gallery)
            postRepository.saveAndFlush(post)

            // when
            assertThatExceptionOfType(IllegalStateException::class.java)
                .isThrownBy {
                    postForMemberService.deletePost(post.id!!, otherMember.id!!)
                }
        }
    }
}
