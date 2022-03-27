package fr.funixgaming.api.client.user.clients;


import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.core.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "user", url = "${app.domain.url}/user/")
public interface UserClient extends CrudClient<UserDTO> {

    @PostMapping("register")
    UserDTO register(@RequestBody @Valid UserCreationDTO request);

    @PostMapping("login")
    UserTokenDTO login(@RequestBody @Valid UserLoginDTO request);
}
