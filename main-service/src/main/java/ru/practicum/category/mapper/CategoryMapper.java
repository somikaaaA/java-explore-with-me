package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

@UtilityClass
public class CategoryMapper {
    public static Category fromDto(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public static Category fromDto(Long id, NewCategoryDto newCategoryDto) {
        return Category.builder()
                .id(id)
                .name(newCategoryDto.getName())
                .build();
    }

    public static CategoryDto toDto(Category cat) {
        return new CategoryDto(
                cat.getId(),
                cat.getName()
        );
    }
}
