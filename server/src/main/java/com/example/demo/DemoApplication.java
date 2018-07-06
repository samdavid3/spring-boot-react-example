package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@Entity
class Book {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Book() {}

    public Book(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

interface BookRepository extends JpaRepository<Book, Long> {}

@Component
class BookCommandLineRunner implements CommandLineRunner {

    private final BookRepository repository;

    public BookCommandLineRunner(BookRepository repository) {
        this.repository = repository;
    }

	@Override
	public void run(String... strings) throws Exception {
		// Top beers from https://www.beeradvocate.com/lists/top/
		Stream.of("A Passage to India", "Moby Dick", "Go Set A Watchman", "To Kill a Mocking Bird",
				"Twenty Thousand Leagues Under the Sea", "Night", "Out of Africa").forEach(name ->
				repository.save(new Book(name))
		);
		repository.findAll().forEach(System.out::println);
	}

}

@RestController
class BookController {

    private BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

	@GetMapping("/good-books")
    @CrossOrigin(origins = "http://localhost:3000")
	public Collection<Book> goodBeers() {
		return repository.findAll().stream()
				.filter(this::isGreat)
				.collect(Collectors.toList());
	}

	private boolean isGreat(Book book) {
		return !book.getName().equals("Go Set A Watchman") &&
				!book.getName().equals("Out of Africa");
	}
}