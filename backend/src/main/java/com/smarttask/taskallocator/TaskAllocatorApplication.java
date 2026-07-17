package com.smarttask.taskallocator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Smart Task Allocator.
 *
 * <p>La capa {@code infrastructure} arranca aquí; el {@code domain} permanece
 * libre de cualquier dependencia de Spring.
 */
@SpringBootApplication
public class TaskAllocatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskAllocatorApplication.class, args);
    }
}
