package net.digitallogic.RestUser.fixtures;

import net.digitallogic.RestUser.persistence.model.UserEntity;
import net.digitallogic.RestUser.web.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UserFixtures {

    private static Random random = new Random();

    private static List<UserEntity> userDtoData = Arrays.asList(
        UserEntity.builder().firstName("Joe").lastName("Exotic").email("joe@exotic.com")
            .encryptedPassword("123456789")
            .build(),
        UserEntity.builder().firstName("John").lastName("Wick").email("JohnWick@BadAss.com")
            .encryptedPassword("asdfasdf")
            .build(),
        UserEntity.builder().firstName("Sarah").lastName("Conner").email("sarah@conner.com")
            .encryptedPassword("myPassword")
            .build(),
        UserEntity.builder().firstName("Bob").lastName("Barker").email("bob@barker.com")
            .encryptedPassword("password")
            .build(),
        UserEntity.builder().firstName("Howard").lastName("TheDuck").email("howard@theDuck.com")
            .encryptedPassword("duckduck")
            .build()
    );

    // Method used to send a copy of the user object to calling method,
    // which prevents mutations to the static dataset.
    private static UserDto copy(UserDto user) {
        return user.toBuilder()
                .build();
    }

    private static UserEntity copy(UserEntity user) {
        return user.toBuilder()
                .build();
    }
    private static UserDto toDto(UserEntity user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getEncryptedPassword())
                .build();
    }

    private static UserEntity toEntity(UserDto user) {
        return UserEntity.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .encryptedPassword(user.getPassword())
                .build();
    }

    public static UserEntity getEntity() {
        return copy(userDtoData.get(random.nextInt(userDtoData.size())));
    }

    public static UserDto getDto() {
        return toDto(getEntity());
    }

    public static List<UserDto> getDtoList() {
        return getDtoList(userDtoData.size());
    }
    public static List<UserDto> getDtoList(int size) {
        return userDtoData.stream()
                .limit(size)
                .map(UserFixtures::toDto)
                .collect(Collectors.toList());
    }

    public static List<UserEntity> getEntityList() {
        return getEntityList(userDtoData.size());
    }
    public static List<UserEntity> getEntityList(int size) {
        return userDtoData.stream()
                .limit(size)
                .map(UserFixtures::copy)
                .collect(Collectors.toList());
    }

    @Test
    public void userDtoCopyTest() {
        UserDto user = getDto();

        UserDto copy = copy(user);

        assertThat(user).isNotSameAs(copy);

        copy.setId(UUID.randomUUID());
        assertThat(user.getId()).isNotEqualTo(copy.getId());
    }
}
