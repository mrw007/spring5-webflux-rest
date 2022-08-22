package mrw007.springframework.spring5webfluxrest.controllers;

import mrw007.springframework.spring5webfluxrest.Repositories.VendorRepository;
import mrw007.springframework.spring5webfluxrest.models.Vendor;
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
import reactor.core.publisher.MonoOperator;

import static org.mockito.ArgumentMatchers.any;

class VendorControllerTest {
    public static final String VENDORS_BASE_URL = "/api/v1/vendors/";

    @Mock
    VendorRepository vendorRepository;
    @InjectMocks
    VendorController vendorController;
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getAllVendors() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(
                Flux.just(Vendor.builder().firstName("john").lastName("doe").build(),
                        Vendor.builder().firstName("vendor2").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        BDDMockito.given(vendorRepository.findById("someID")).willReturn(
                Mono.just(Vendor.builder().firstName("john").lastName("doe").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URL + "someID")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void createNewVendor() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().firstName("Joe").build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("joe").build());

        webTestClient.post().uri(VENDORS_BASE_URL)
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendor() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VENDORS_BASE_URL + "someID")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

    }
}