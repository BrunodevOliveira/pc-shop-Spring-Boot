package com.pcshop.services;

import com.pcshop.dto.CategoryDTO;
import com.pcshop.entities.Category;
import com.pcshop.repositories.CategoryRepository;
import com.pcshop.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list =  repository.findAll();

        //Converte a lista de categorias para uma Lista de Categorias do Tipo DTO e RETORNO
        return list.stream().map(cat -> new CategoryDTO(cat)).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalEntity = repository.findById(id); //Surgiu para evitar que se trabalhe com valor nulo. Assim dentro desse Optional pode existir ou NAO a categoria
        Category entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Entity not found")); //se o objeto não estiver presente no Optional, posso retornar uma msg de erro
        return new CategoryDTO(entity);
    }
}
