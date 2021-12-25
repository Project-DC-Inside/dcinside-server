package org.deepforest.dcinside.upload

import org.deepforest.dcinside.dto.ResponseDto
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api/v1/image")
@RestController
class ImageUploadController(
    private val imageUploadService: ImageUploadService
) {

    @PostMapping("/{path}")
    fun upload(
        @PathVariable("path") path: FolderName,
        @RequestParam file: MultipartFile
    ): ResponseDto<String> = ResponseDto.ok(
        imageUploadService.upload(path, file)
    )
}
