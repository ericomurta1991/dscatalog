 package com.devsuperior.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findallPage(PageRequest pageRequest) {
		Page<Category> list = repository.findAll(pageRequest);
							
		return list.map(x -> new CategoryDTO(x));
	
		//the page is already a java8 stream, so there is no need to use the stream or .collect to do the conversions	
	
	/*
	 public List<CategoryDTO> findall(){
		List<Category> list = repository.findAll();
							//I convert the object to stream and then convert it back to list
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	 
	 */
	
	
		/* using for structure
		 * 
		 	List<CategoryDTO> listDto = new ArrayList<>();
		for(Category cat: list) {
			listDto.add(new CategoryDTO(cat));
		}
		  lambda
		  List<CategoryDTO> listDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		 */
		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findBydId(Long id) {
		Optional<Category> obj = repository.findById(id);
		//Category entity  = obj.get();
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = repository.getReferenceById(id);
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		
		}
		catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("Id not found " + id);
		}
		
		
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
		
	}
	
}
