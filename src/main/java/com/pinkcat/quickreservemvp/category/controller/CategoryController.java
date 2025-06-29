package com.pinkcat.quickreservemvp.category.controller;

import com.pinkcat.quickreservemvp.category.dto.CategoryListResponseDTO;
import com.pinkcat.quickreservemvp.category.service.CategoryService;
import com.pinkcat.quickreservemvp.common.model.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    @ResponseBody
    public BaseResponse<CategoryListResponseDTO> getCategories() {
        return new BaseResponse<>(categoryService.getCategories());
    }
}
