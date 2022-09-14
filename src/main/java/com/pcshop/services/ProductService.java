package com.pcshop.services;

import com.pcshop.dto.CategoryDTO;
import com.pcshop.dto.ProductDTO;
import com.pcshop.entities.Category;
import com.pcshop.entities.Product;
import com.pcshop.repositories.CategoryRepository;
import com.pcshop.repositories.ProductRepository;
import com.pcshop.services.exceptions.DataBaseException;
import com.pcshop.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
        //Retorno uma nova instância com os dados do produto e as categorias que ele pertence
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }

    //Método que trnasforma os dados vindos do DTO para Entidade
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        //o Id não é modificado no momento de criar ou de atualizar
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        //Copia somente as categorias que vieram do DTO
        entity.getCategories().clear();//limpa as categorias que existem na entity
        for (CategoryDTO catDto : dto.getCategories()) {//percorro todas as categorias do DTO
            Category category = categoryRepository.getOne(catDto.getId()); //armazeno cada categoria na variável category sem precisar "acessar" o BD
            entity.getCategories().add(category);
        }
    }
}
