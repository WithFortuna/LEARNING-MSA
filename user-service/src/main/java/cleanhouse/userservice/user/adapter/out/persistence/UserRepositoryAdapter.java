package cleanhouse.userservice.user.adapter.out.persistence;

import cleanhouse.userservice.user.domain.entity.User;
import cleanhouse.userservice.user.domain.application.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public Page<User> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return springDataRepository.findAll(pageRequest)
                .map(entity -> modelMapper.map(entity, User.class));
    }
}
