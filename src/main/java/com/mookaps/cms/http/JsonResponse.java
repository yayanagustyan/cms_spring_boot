package com.mookaps.cms.http;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponse<T> {
    private int status;
    private String message;
    private int count;
    private List<T> data;
}
