package project.smoothsaver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.smoothsaver.entity.SallingStore;

import java.util.List;

public interface SallingStoreRepository extends JpaRepository<SallingStore,String> {
    SallingStore findSallingStoreById(String id);
    List<SallingStore> findSallingStoreByZip(String zip);

}
