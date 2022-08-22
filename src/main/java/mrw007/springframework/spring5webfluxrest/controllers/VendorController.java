package mrw007.springframework.spring5webfluxrest.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import mrw007.springframework.spring5webfluxrest.Repositories.VendorRepository;
import mrw007.springframework.spring5webfluxrest.models.Vendor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Tag(name = "Vendors")
@RestController
@RequestMapping(VendorController.VENDORS_BASE_URL)
public class VendorController {
    public static final String VENDORS_BASE_URL = "/api/v1/vendors";
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createNewVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/{id}")
    public Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/{id}")
    public Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Optional<Vendor> foundVendor = Optional.ofNullable(vendorRepository.findById(id).block());
        if (foundVendor.isPresent()) {
            if (!vendor.getFirstName().equals(foundVendor.get().getFirstName())) {
                foundVendor.get().setFirstName(vendor.getFirstName());
            }
            if (!vendor.getLastName().equals(foundVendor.get().getLastName())) {
                foundVendor.get().setLastName(vendor.getLastName());
            }
            return vendorRepository.save(foundVendor.get());
        }
        throw new RuntimeException("Not Found!");
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteVendorById(@PathVariable String id) {
        return vendorRepository.deleteById(id);
    }
}
