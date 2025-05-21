package com.mookaps.cms.files;

import lombok.Data;

@Data
public class UploadRequest {
    private String filename;
    private String path;
    private String base64;
}
