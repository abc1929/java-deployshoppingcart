package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.models.*;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WithMockUser(username = "admin",
        roles = {"USER", "ADMIN"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShoppingCartApplicationTest.class,
        properties = {
                "command.line.runner.enabled=false"})
@AutoConfigureMockMvc
public class CartControllerTestNoDB {

   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;

   @MockBean
   private CartItemService cartItemService;

   @MockBean
   private UserService userService;

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

      RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
              .apply(SecurityMockMvcConfigurers.springSecurity())
              .build();
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void listCartItemsByUserId() throws Exception {
      String apiUrl = "/carts/user/";


      Mockito.when(userService.findByName("admin"))
              .thenReturn(userList.get(0));

//      System.out.println(userList.get(0).toString());
      RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
              .accept(MediaType.APPLICATION_JSON);
      MvcResult r = mockMvc.perform(rb)
              .andReturn();
      String tr = r.getResponse()
              .getContentAsString();

      ObjectMapper mapper = new ObjectMapper();
      String er = mapper.writeValueAsString(userList.get(0));

      System.out.println(er);
      assertEquals(er,
              tr);
   }

   @Test
   public void addToCart() throws Exception {
      String apiUrl = "/carts/add/user/product/8";

      var item1 = new CartItem(userList.get(0), productList.get(2), 3, "");


      ObjectMapper mapper = new ObjectMapper();
      String restaurantString = mapper.writeValueAsString(item1);

      Mockito.when(userService.findByName("admin")).thenReturn(userList.get(0));

      Mockito.when(cartItemService.addToCart(userList.get(0).getUserid(),productList.get(2).getProductid(),"I am not working"))
              .thenReturn(item1);

      RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
              .accept(MediaType.APPLICATION_JSON)
              .contentType(MediaType.APPLICATION_JSON)
              .content(restaurantString);

      mockMvc.perform(rb)
              .andExpect(status().isOk())
              .andDo(MockMvcResultHandlers.print());

   }

   @Test
   public void removeFromCart() {
   }
}