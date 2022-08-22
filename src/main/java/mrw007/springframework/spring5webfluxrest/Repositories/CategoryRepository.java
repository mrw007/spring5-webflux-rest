package mrw007.springframework.spring5webfluxrest.Repositories;

import mrw007.springframework.spring5webfluxrest.models.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
