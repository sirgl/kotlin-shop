package ru.nsu.rivanov.shop

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.SpringApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder


@SpringBootApplication
@EnableJpaRepositories
open class Application {
    @Bean
    open fun objectMapperBuilder(): Jackson2ObjectMapperBuilder =
            Jackson2ObjectMapperBuilder().modulesToInstall(KotlinModule())

    @Bean
    open fun init(categoryRepository: CategoryRepository) : CommandLineRunner {
        return CommandLineRunner {
            categoryRepository.save(listOf(
                    Category(null, null, "Товары для дома", "Товары для уютного гнездышка"),
                    Category(null, null, "Одежда для женщин", "Одежда для женщин"),
                    Category(null, null, "Одежда для мужчин", "Одежда для мужчин"),
                    Category(null, null, "Телефоны и аксессуары", "Компьютерная техника"),
                    Category(null, null, "Автотовары", "Автотовары")
            ))
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}