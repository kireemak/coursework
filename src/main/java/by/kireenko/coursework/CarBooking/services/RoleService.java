package by.kireenko.coursework.CarBooking.services;

import by.kireenko.coursework.CarBooking.error.ResourceNotFoundException;
import by.kireenko.coursework.CarBooking.models.Role;
import by.kireenko.coursework.CarBooking.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }

    public Role getAdminRole() {
        return roleRepository.findByName("ROLE_ADMIN").get();
    }
}
