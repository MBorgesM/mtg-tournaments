package br.com.mborgesm.mtgtournaments.repositories;

import br.com.mborgesm.mtgtournaments.models.ERole;
import br.com.mborgesm.mtgtournaments.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
