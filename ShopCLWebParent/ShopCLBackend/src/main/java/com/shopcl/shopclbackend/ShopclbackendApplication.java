package com.shopcl.shopclbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan({"com.shopcl.common.entity"})
@ComponentScan(basePackages= "com.shopcl.shopclbackend.security")
public class ShopclbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopclbackendApplication.class, args);
	}

}
