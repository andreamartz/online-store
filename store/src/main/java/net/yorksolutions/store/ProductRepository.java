package net.yorksolutions.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);

//    @Override
//    Optional<Product> findById(Long aLong);

    Optional<Product> findById(Long id);

    // can use Iterable, Collection, or List
    List<Product> findAll();

    void deleteById(Long id);

}
