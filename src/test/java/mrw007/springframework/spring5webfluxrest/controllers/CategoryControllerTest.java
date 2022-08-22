package mrw007.springframework.spring5webfluxrest.controllers;

import mrw007.springframework.spring5webfluxrest.Repositories.CategoryRepository;
import mrw007.springframework.spring5webfluxrest.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;


class CategoryControllerTest {
    public static final String CATEGORIES_BASE_URL = "/api/v1/categories/";

    WebTestClient webTestClient;
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void getAllCategories() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("cat1").build(),
                        Category.builder().description("cat2").build()));

        webTestClient.get()
                .uri(CATEGORIES_BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("cat").build()));

        webTestClient.get()
                .uri(CATEGORIES_BASE_URL + "someID")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void createNewCategory() {
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("desc").build()));

        Mono<Category> categoryToSave = Mono.just(Category.builder().description("desc").build());

        webTestClient.post().uri(CATEGORIES_BASE_URL)
                .body(categoryToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateCategory() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryToUpdate = Mono.just(Category.builder().build());

        webTestClient.put()
                .uri(CATEGORIES_BASE_URL + "someID")
                .body(categoryToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchCategory() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("desc").build()));

        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("description").build()));

        Mono<Category> categoryToPatch = Mono.just(Category.builder().description("description").build());

        webTestClient.patch()
                .uri(CATEGORIES_BASE_URL + "someID")
                .body(categoryToPatch, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void patchCategoryNotFound() {
        BDDMockito.given(categoryRepository.findById(anyString()))
                .willReturn(Mono.empty());

        Mono<Category> categoryToPatch = Mono.just(Category.builder().description("description").build());

        webTestClient.patch()
                .uri(CATEGORIES_BASE_URL + "someID")
                .body(categoryToPatch, Category.class)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void deleteCategoryById() {
        webTestClient.delete()
                .uri(CATEGORIES_BASE_URL + "SomeId")
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(categoryRepository).deleteById(anyString());
    }
}