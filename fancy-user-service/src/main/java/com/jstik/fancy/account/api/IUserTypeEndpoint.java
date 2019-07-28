package com.jstik.fancy.account.api;

import com.jstik.fancy.account.search.entity.elastic.UserType;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUserTypeEndpoint {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")})
    @GetMapping(value = "/users")
    public ResponseEntity<Page<UserType>> getAllUsers(@RequestParam(required = true) int page, @RequestParam(required = false,defaultValue = "25") int size);

}
