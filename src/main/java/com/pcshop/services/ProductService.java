package com.pcshop.services;

import com.pcshop.dto.CategoryDTO;
import com.pcshop.entities.Category;
import com.pcshop.repositories.CategoryRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) { //Recebo a page enviada do Controller | O método agr é tipado como Page
        Page<Category> list =  repository.findAll(pageRequest); //Faço uma busca com os valores enviados e salvo numa lista do tipo Page
        return list.map(cat -> new CategoryDTO(cat)); //TRasnformo cada elementos em um DTP e retornouma nova lista para o controller
    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> optionalEntity = repository.findById(id); //Surgiu para evitar que se trabalhe com valor nulo. Assim dentro desse Optional pode existir ou NAO a categoria
        Category entity = optionalEntity.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName()); //Transformo o DTO no meu obj com os dados (dessa forma teria que setar cada uma das informações trazidas no DTO)
        entity = repository.save(entity); //depois que salvar os dados retorna o entity já com o id
        return new CategoryDTO(entity);
    }
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found" + id);
        }
    }

    public void delete(Long id) { //Não utilizei o transactional pois preciso capturar uma exceção que com ele não conseguiria
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not fauld" + id);
        }
        catch(DataIntegrityViolationException e){ //Integridade Referêncial-> Caso tente deletar uma categoria que possui produtos relacionados a ela
            throw new DataBaseException("Integraty violation");
        }
    }
}
