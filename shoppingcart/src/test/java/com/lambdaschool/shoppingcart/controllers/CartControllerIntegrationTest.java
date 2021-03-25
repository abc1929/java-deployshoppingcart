package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.ProductService;
import com.lambdaschool.shoppingcart.services.RoleService;
import com.lambdaschool.shoppingcart.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ShoppingCartApplicationTest.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "admin")
public class CartControllerIntegrationTest {

   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;

   @Autowired
   CartItemService cartItemService;

   @Autowired
   UserService userService;

   @Autowired
   RoleService roleService;

   @Autowired
   ProductService productService;


   @Before
   public void setUp() throws Exception {

      RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
//
//      List<CartItem> myList = ;
//      for (Restaurant r : myList)
//      {
//         System.out.println(r.getRestaurantid() + " " + r.getName());
//      }
//
//      List<Payment> myPay = paymentService.findAll();
//      for (Payment p : myPay)
//      {
//         System.out.println(p.getPaymentid() + " " + p.getType());
//      }

      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
              .apply(SecurityMockMvcConfigurers.springSecurity())
              .build();
   }

   @After
   public void tearDown() throws Exception {
   }

   @Test
   public void listCartItemsByUserId() {
      given().when()
              .get("/carts/user/")
              .then()
              .statusCode(200)
              .and()
              .body(containsString("admin"));
   }

   @Test
   public void addToCart() {



      given().when()
              .put("/carts/add/user/product/8")
              .then()
              .statusCode(200)
              .and()
              .body(containsString("PEN"));
   }

   @Test
   public void removeFromCart() {

      given().when()
              .put("/carts/add/user/product/8")
              .then()
              .statusCode(200)
              .and()
              .body(containsString("PEN"));
   }
}