package net.yorksolutions.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);

    Optional<Product> findById(Long id);

    // can use Iterable, Collection, or List
//    Iterable<Product> findAll();  // not necessary as long as I use Iterable in my service; findAll comes from CrudRepository

//    List<Product> findAll();

    void deleteById(Long id);

}
