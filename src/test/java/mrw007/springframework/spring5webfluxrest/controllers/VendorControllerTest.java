package mrw007.springframework.spring5webfluxrest.controllers;

import mrw007.springframework.spring5webfluxrest.Repositories.VendorRepository;
import mrw007.springframework.spring5webfluxrest.models.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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
        given(vendorRepository.findAll()).willReturn(
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
        given(vendorRepository.findById(anyString())).willReturn(
                Mono.just(Vendor.builder().firstName("john").lastName("doe").build()));

        webTestClient.get()
                .uri(VENDORS_BASE_URL + "someID")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void createNewVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
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
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().build());

        webTestClient.put()
                .uri(VENDORS_BASE_URL + "someID")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    void patchVendor() {
        given(vendorRepository.findById(anyString())).willReturn(
                Mono.just(Vendor.builder().firstName("joe").lastName("newman").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().firstName("joey").lastName("newmen").build()));

        Mono<Vendor> vendorToPatch = Mono.just(Vendor.builder().firstName("joey").lastName("newmen").build());

        webTestClient.patch()
                .uri(VENDORS_BASE_URL + "someID")
                .body(vendorToPatch, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any(Vendor.class));
    }

    @Test
    void patchVendorNotFound() {
        given(vendorRepository.findById(anyString())).willReturn(
                Mono.empty());
        Mono<Vendor> vendorToPatch = Mono.just(Vendor.builder().firstName("joey").lastName("newmen").build());

        webTestClient.patch()
                .uri(VENDORS_BASE_URL + "someID")
                .body(vendorToPatch, Vendor.class)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void deleteVendorById() {
        webTestClient.delete()
                .uri(VENDORS_BASE_URL + "SomeId")
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).deleteById(anyString());
    }
}