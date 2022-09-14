package com.pcshop.resources;

import java.net.URI;
import java.util.List;

import com.pcshop.dto.CategoryDTO;
import com.pcshop.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        dto = service.insert(dto); //aproveito a mesma variável que carrega o obj com os dados para cadastro e armazeno o obj cadastrado ja com ID
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}") //path -> diz qual a estrutura do recurso criado
                .buildAndExpand(dto.getId()).toUri(); //buildAndExpand-> Valor que será incluido na estrutura | toUri->  é o padrão
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto){
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }
}
