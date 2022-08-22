package mrw007.springframework.spring5webfluxrest.Repositories;

import mrw007.springframework.spring5webfluxrest.models.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
