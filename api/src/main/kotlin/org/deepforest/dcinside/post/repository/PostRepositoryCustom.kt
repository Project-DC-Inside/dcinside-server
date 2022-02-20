package org.deepforest.dcinside.post.repository

import org.deepforest.dcinside.entity.gallery.GalleryType
import org.deepforest.dcinside.entity.post.Post

interface PostRepositoryCustom {
    fun findByGalleryType(galleryType: GalleryType?) : List<Post>
}
