package it.uniroma3.vestiti.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.vestiti.model.Taglia;

@Repository
public interface TagliaRepository extends CrudRepository<Taglia, Long>{

}
