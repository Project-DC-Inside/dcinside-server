package org.deepforest.dcinside.gallery

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GalleryService(
    private val galleryRepository: GalleryRepository
) {
    fun findGallery(type: GalleryType? = null): List<ResponseGalleryDto> {
        return when (type) {
            null -> galleryRepository.findAll()
            else -> galleryRepository.findByType(type)
        }.map {
            ResponseGalleryDto.from(it)
        }
    }

    @Transactional
    fun save(galleryDto: RequestGalleryDto): ResponseGalleryDto {
        return galleryDto.toEntity().let {
            validateGallery(it)
            galleryRepository.save(it)
        }.let {
            ResponseGalleryDto.from(it)
        }
    }

    /**
     * 갤러리 타입 MAJOR, MINOR 는 서로 이름이 겹친 갤러리가 존재할 수 없음
     * MINI 는 별도라서 가능
     */
    private fun validateGallery(gallery: Gallery) {
        if (gallery.type == GalleryType.MINI) {
            return
        }

        val hasDuplicatedGallery = galleryRepository.findByName(gallery.name).any { it.type != GalleryType.MINI }

        if (hasDuplicatedGallery) {
            throw IllegalArgumentException("${gallery.name} 이름과 중복되는 갤러리가 존재합니다.")
        }
    }

    fun delete(id: Long) = galleryRepository.deleteById(id)
}
