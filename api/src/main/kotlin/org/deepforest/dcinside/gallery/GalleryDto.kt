package org.deepforest.dcinside.gallery

import org.deepforest.dcinside.entity.gallery.Gallery
import org.deepforest.dcinside.entity.gallery.GalleryType

class RequestGalleryDto(
    private val type: GalleryType,
    private val name: String
) {
    fun toEntity() = Gallery(type = type, name = name)
}

class ResponseGalleryDto(
    val id: Long,
    val type: GalleryType,
    val name: String
) {
    companion object {
        fun from(gallery: Gallery) =
            ResponseGalleryDto(gallery.id!!, gallery.type, gallery.name)
    }
}
