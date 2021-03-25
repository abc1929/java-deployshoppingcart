package com.lambdaschool.shoppingcart;


import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.ProductService;
import com.lambdaschool.shoppingcart.services.RoleService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@Component
public class SeedData
        implements CommandLineRunner {
   /**
    * Connects the Role Service to this process
    */
   @Autowired
   RoleService roleService;

   /**
    * Connects the user service to this process
    */
   @Autowired
   UserService userService;

   @Autowired
   CartItemRepository cartItemRepository;

   @Autowired
   ProductRepository productRepository;

   @Autowired
   ProductService productService;

   /**
    * Generates test, seed data for our application
    * First a set of known data is seeded into our database.
    * Second a random set of data using Java Faker is seeded into our database.
    * Note this process does not remove data from the database. So if data exists in the database
    * prior to running this process, that data remains in the database.
    *
    * @param args The parameter is required by the parent interface but is not used in this process.
    */
   @Transactional
   @Override
   public void run(String[] args) throws
           Exception {
      userService.deleteAll();
      roleService.deleteAll();
      Role r1 = new Role("ADMIN");
      Role r2 = new Role("USER");
      Role r3 = new Role("DATA");

      r1 = roleService.save(r1);
      r2 = roleService.save(r2);
      r3 = roleService.save(r3);

      // admin, data, user
      User u1 = new User("admin",
              "password",
              "admin@lambdaschool.local", "");
      u1.getRoles()
              .add(new UserRoles(u1,
                      r1));
      u1.getRoles()
              .add(new UserRoles(u1,
                      r2));
      u1.getRoles()
              .add(new UserRoles(u1,
                      r3));

      userService.save(u1);

      // data, user
      User u2 = new User("cinnamon",
              "LambdaLlama",
              "cinnamon@lambdaschool.local", "");
      u2.getRoles()
              .add(new UserRoles(u2,
                      r2));
      u2.getRoles()
              .add(new UserRoles(u2,
                      r3));
      userService.save(u2);

      // user
      User u3 = new User("barnbarn",
              "LambdaLlama",
              "barnbarn@lambdaschool.local", "");
      u3.getRoles()
              .add(new UserRoles(u3,
                      r2));
      userService.save(u3);

      User u4 = new User("puttat",
              "LambdaLlama",
              "puttat@school.lambda", "");
      u4.getRoles()
              .add(new UserRoles(u4,
                      r2));
      userService.save(u4);


      var p1 = new Product(8, "PEN", "MAKES WORDS", 2.50, "");
      var p2 = new Product(9, "PENCIL", "DOES MATH", 1.50, "");
      var p3 = new Product(10, "COFFEE", "EVERYONE NEEDS COFFEE", 4.00, "");
      productRepository.save(p1);
      productRepository.save(p2);
      productRepository.save(p3);




      cartItemRepository.deleteAll();
      cartItemRepository.save(new CartItem(userService.findByName("cinnamon"), p2, 3, ""));
      cartItemRepository.save(new CartItem(userService.findByName("cinnamon"), p3, 2, ""));
      cartItemRepository.save(new CartItem(userService.findByName("barnbarn"), p3, 1, ""));
      cartItemRepository.save(new CartItem(userService.findByName("puttat"), p3, 17, ""));


   }
}