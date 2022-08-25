package local.project12.springbootdata_async_demo.repository;

import local.project12.springbootdata_async_demo.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface DataRepository extends JpaRepository<DataEntity, Long> {
    @Async
    CompletableFuture<Set<DataEntity>> findAllByRandomDataAndRandomString(Integer num, String str);

    @Async
    @Query(nativeQuery = true, value = """
                    SELECT count(*) FROM DATA_ENTITY WHERE
                    RANDOM_DATA = :num and
                    RANDOM_STRING = :str
            """)
    CompletableFuture<Long> findCountAllByParams(Integer num, String str);
}
