package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.DataNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private  final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto saveCat(NewCategoryDto newCategoryDto) {
        Category newCategory = CategoryMapper.fromDto(newCategoryDto);
        try {
            return CategoryMapper.toDto(categoryRepository.save(newCategory));
        } catch (Exception e) {
            throw new DataConflictException("Такое имя уже существует.");
        }
    }

    @Transactional
    public void deleteCat(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryDto updateCat(Long id, NewCategoryDto newCategoryDto) {
        Category oldCat = categoryRepository.findById(id).
                orElseThrow(()-> new DataNotFoundException("Категория не найдена"));

        if (newCategoryDto.getName() != null) {
           oldCat.setName(newCategoryDto.getName());
        }

        return CategoryMapper.toDto(categoryRepository.save(oldCat));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findCats(Integer from, Integer size) {
        List<Category> cats = categoryRepository.getCats(from,size);
        return cats.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findCatById(Long id) {
        return CategoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Категория не найдена")));
    }
}
