package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findall(){
		List<Category> list = repository.findAll();
							//I convert the object to stream and then convert it back to list
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
		
		
		
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
}
