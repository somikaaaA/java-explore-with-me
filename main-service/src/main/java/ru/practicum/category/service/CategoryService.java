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
    private final CategoryRepository catRepository;

    @Transactional
    public CategoryDto saveCat(NewCategoryDto newCategoryDto) {
        Category newCat = CategoryMapper.fromDto(newCategoryDto);
        try {
            return CategoryMapper.toDto(catRepository.save(newCat));
        } catch (Exception e) {
            throw new DataConflictException("Нарушение уникальности имени");
        }
    }

    @Transactional
    public void deleteCat(Long catId) {
        catRepository.deleteById(catId);
    }

    @Transactional
    public CategoryDto updateCat(Long id, NewCategoryDto newCategoryDto) {
        Category oldCat = catRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Категория не найдена"));
        if (newCategoryDto.getName() != null)
            oldCat.setName(newCategoryDto.getName());
        return CategoryMapper.toDto(catRepository.save(oldCat));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findCats(Integer from, Integer size) {
        List<Category> cats = catRepository.getCats(from,size);
        return cats.stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findCatById(Long id) {
        return CategoryMapper.toDto(catRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Категория не найдена")));
    }
}