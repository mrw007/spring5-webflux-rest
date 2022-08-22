package mrw007.springframework.spring5webfluxrest.controllers;

import mrw007.springframework.spring5webfluxrest.Repositories.VendorRepository;
import mrw007.springframework.spring5webfluxrest.models.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}