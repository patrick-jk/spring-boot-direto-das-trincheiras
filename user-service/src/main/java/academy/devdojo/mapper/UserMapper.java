package academy.devdojo.mapper;

import academy.devdojo.domain.User;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toUser(UserPostRequest postRequest);

    User toUser(UserPutRequest request);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);
}