package ru.danbeldev.h1hackapi.features.services.importData.update;

import org.springframework.web.multipart.MultipartFile;

public interface TaskUpdateImportService {

    void importData(Long projectId, MultipartFile file);
}
