package org.deepforest.dcinside.gallery

import org.assertj.core.api.Assertions.*
import org.deepforest.dcinside.ServiceTestBase
import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GalleryServiceTest : ServiceTestBase() {

    @Autowired
    private lateinit var galleryService: GalleryService

    @Autowired
    private lateinit var galleryRepository: GalleryRepository

    @Test
    fun 갤러리_생성() {
        // given
        val galleryName = "gallery-service-test-name-1"
        val galleryDto = RequestGalleryDto(GalleryType.MINI, galleryName)

        // when
        val savedGallery = galleryService.save(galleryDto)

        // then
        assertThat(savedGallery.name).isEqualTo(galleryName)
        assertThat(savedGallery.type).isEqualTo(GalleryType.MINI)
    }

    @Test
    fun `갤러리 타입에 해당하는 모든 갤러리 리스트 조회`() {
        // given
        repeat (5) { galleryRepository.save(Gallery(type = GalleryType.MAJOR, name = "GalleryTest2-MAJOR-Name$it")) }
        repeat (10) { galleryRepository.save(Gallery(type = GalleryType.MINOR, name = "GalleryTest2-MINOR-Name$it")) }
        galleryRepository.flush()

        // when
        val miniGalleries = galleryService.findGallery(GalleryType.MINI)
        val majorGalleries = galleryService.findGallery(GalleryType.MAJOR)
        val minorGalleries = galleryService.findGallery(GalleryType.MINOR)
        val allGalleries = galleryService.findGallery()

        // then
        assertThat(miniGalleries.size).isEqualTo(0)
        assertThat(majorGalleries.size).isEqualTo(5)
        assertThat(minorGalleries.size).isEqualTo(10)
        assertThat(allGalleries.size).isEqualTo(15)
    }

    @Test
    fun `갤러리 아이디로 삭제`() {
        // given
        val galleryName = "GalleryTest3"
        galleryRepository.saveAndFlush(Gallery(type = GalleryType.MINOR, name = galleryName))
        val gallery = galleryRepository.findByGalleryName(galleryName)

        // when
        galleryService.delete(gallery.id!!)

        // then
        assertThatExceptionOfType(NoSuchElementException::class.java)
            .isThrownBy { galleryRepository.findByGalleryName(galleryName) }
            .withMessage("$galleryName 갤러리 정보가 없습니다")
    }
}
