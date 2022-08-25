package local.project12.springbootdata_async_demo.repository;

import local.project12.springbootdata_async_demo.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface DataRepository extends JpaRepository<DataEntity, Long> {
    @Async
    CompletableFuture<Set<DataEntity>> findAllByNum1AndNum2(Integer num1, Integer num2);

    @Async
    @Query(nativeQuery = true, value = """
                    SELECT count(*) FROM DATA_ENTITY WHERE
                    NUM1 = :num1 and
                    NUM2 = :num2
            """)
    CompletableFuture<Long> findCountAllByParamsNum1Num2(Integer num1, Integer num2);

    @Async
    @Query(nativeQuery = true, value = """
                    SELECT count(*) FROM DATA_ENTITY WHERE
                    NUM1 = :num1
            """)
    CompletableFuture<Long> findCountAllByParamsNum1(Integer num1);
}
