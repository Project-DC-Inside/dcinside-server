package org.deepforest.dcinside.gallery

import org.deepforest.dcinside.entity.gallery.GalleryType
import org.springframework.stereotype.Service

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

    fun save(galleryDto: RequestGalleryDto): ResponseGalleryDto {
        return galleryDto.toEntity().let {
            galleryRepository.save(it)
        }.let {
            ResponseGalleryDto.from(it)
        }
    }

    fun delete(id: Long) = galleryRepository.deleteById(id)
}
