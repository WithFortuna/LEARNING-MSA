package cleanhouse.userservice.user.infrastructure.persistence;

import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserSpringDataRepository springDataRepository;
    @Qualifier("userModelMapper")
    private final ModelMapper modelMapper;

    @Override
    public User save(User user) {
        UserEntity entity = modelMapper.map(user, UserEntity.class);
        UserEntity savedEntity = springDataRepository.save(entity);
        return modelMapper.map(savedEntity, User.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataRepository.findByEmail(email)
                .map(entity -> modelMapper.map(entity, User.class));
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataRepository.findById(id)
                .map(entity -> modelMapper.map(entity, User.class));
    }
}
