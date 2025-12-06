package com.example.Hack2025;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.Hack2025.Entities.User;
import com.example.Hack2025.Repositories.UserRepository;

@SpringBootApplication
public class Hack2025Application {

	public static void main(String[] args) {
		SpringApplication.run(Hack2025Application.class, args);
	}
}
