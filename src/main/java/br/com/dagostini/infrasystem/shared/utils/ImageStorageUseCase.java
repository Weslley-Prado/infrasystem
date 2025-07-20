package br.com.dagostini.infrasystem.shared.utils;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageUseCase {
    String storeImage(MultipartFile file);
}
