package port.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import port.management.system.dto.ProductResponse3;
import port.management.system.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/public/users/products")
    public ResponseEntity<List<ProductResponse3>> getAllProductsByUser() {

        return new ResponseEntity<>(userService.getAllProductsByUser(), HttpStatus.FOUND);

    }

}
