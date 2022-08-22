package mrw007.springframework.spring5webfluxrest.Repositories;

import com.sun.source.tree.CaseTree;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryRepository extends ReactiveMongoRepository<CaseTree, String> {
}
