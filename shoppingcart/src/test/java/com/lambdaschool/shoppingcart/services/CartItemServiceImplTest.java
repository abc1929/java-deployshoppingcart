package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.RoleRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplicationTest.class,
        properties = {
                "command.line.runner.enabled=false"})
public class CartItemServiceImplTest {


   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;


   @Autowired
   private CartItemService cartItemService;

   @MockBean
   private CartItemRepository cartItemRepository;

   @MockBean
   private RoleRepository roleRepository;

   @MockBean
   private ProductRepository productRepository;

   @MockBean
   private UserRepository userRepository;

   private List<Product> productList = new ArrayList<>();

   private List<Role> roleList = new ArrayList<>();

   private List<User> userList = new ArrayList<>();

   @Before
   public void setUp() throws Exception {


      Role r1 = new Role("ADMIN");
      Role r2 = new Role("USER");
      Role r3 = new Role("DATA");

      roleList.add(r1);
      roleList.add(r2);
      roleList.add(r3);

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
      u1.setUserid(777);

      userList.add(u1);


      var p1 = new Product(8, "PEN", "MAKES WORDS", 2.50, "");
      var p2 = new Product(9, "PENCIL", "DOES MATH", 1.50, "");
      var p3 = new Product(10, "COFFEE", "EVERYONE NEEDS COFFEE", 4.00, "");
      productList.add(p1);
      productList.add(p2);
      productList.add(p3);


      var item1 = new CartItem(u1, p2, 3, "");
      var item2 = new CartItem(u1, p3, 2, "");
      var item3 = new CartItem(u1, p3, 1, "");
      var item4 = new CartItem(u1, p3, 17, "");

      userList.get(0).getCarts().add(item1);
      userList.get(0).getCarts().add(item2);
      userList.get(0).getCarts().add(item3);
      userList.get(0).getCarts().add(item4);

      MockitoAnnotations.initMocks(this);
   }


   @After
   public void tearDown() throws Exception {
   }


   @Test
   public void addToCart(){


      Mockito.when(userRepository.findById(777L)).thenReturn(
              Optional.of(userList.get(0))
      );
      Mockito.when(productRepository.findById(9L)).thenReturn(Optional.of(productList.get(1)));

      Mockito.when(cartItemRepository.findById(any(CartItemId.class))).thenReturn(userList.get(0).getCarts().stream().findFirst());


      Mockito.when(cartItemRepository.save(any(CartItem.class))).thenReturn(userList.get(0).getCarts().stream().findFirst().get());


      CartItem test = cartItemService.addToCart(777,9,"");

      assertEquals(test.getQuantity(),4);



   }

   @Test
   public void removeFromCart(){


      Mockito.when(userRepository.findById(777L)).thenReturn(
              Optional.of(userList.get(0))
      );
      Mockito.when(productRepository.findById(9L)).thenReturn(Optional.of(productList.get(1)));

      Mockito.when(cartItemRepository.findById(any(CartItemId.class))).thenReturn(userList.get(0).getCarts().stream().findFirst());


      Mockito.when(cartItemRepository.save(any(CartItem.class))).thenReturn(userList.get(0).getCarts().stream().findFirst().get());


      CartItem test = cartItemService.removeFromCart(777,9,"");

      assertEquals(test.getQuantity(),2);


   }


}