package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Role;
import by.kireenko.coursework.CarBooking.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;

    @Cacheable(value = "roles")
    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

    @Cacheable(value = "roles")
    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN").get();
    }
}
