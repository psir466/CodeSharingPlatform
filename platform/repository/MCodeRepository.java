package platform.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.logic.MCode;

import java.util.Optional;


public interface MCodeRepository extends CrudRepository<MCode, Long> {

    public Optional<MCode> findByUuidCode(String uuidCode);

    public void deleteByUuidCode(String uuidCode);

}
