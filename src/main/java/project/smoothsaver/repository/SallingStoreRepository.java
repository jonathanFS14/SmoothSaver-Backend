package project.smoothsaver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.smoothsaver.entity.SallingStore;

public interface SallingStoreRepository extends JpaRepository<SallingStore,String> {
    Page<SallingStore> findSallingStoreById(String id,
                                            Pageable pageable);

}
