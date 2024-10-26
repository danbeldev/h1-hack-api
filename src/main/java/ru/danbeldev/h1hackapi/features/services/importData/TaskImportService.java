package ru.danbeldev.h1hackapi.features.services.importData;

import org.springframework.web.multipart.MultipartFile;

public interface TaskImportService {

    void importData(Long projectId, MultipartFile file);
}
