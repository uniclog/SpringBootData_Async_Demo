package local.project12.springbootdata_async_demo.controller;

import local.project12.springbootdata_async_demo.entity.DataEntity;
import local.project12.springbootdata_async_demo.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class MainController {
    private final DataRepository repository;

    @GetMapping("/addRandomData/{count}")
    public ResponseEntity<DataEntity> addRandomData(@PathVariable("count") Integer count) {
        long startMain = System.currentTimeMillis();
        List<DataEntity> list = new ArrayList<>();
        var entity = new DataEntity();
        for (int index = 0; index < count; index++) {
            long start = System.currentTimeMillis();
            entity = DataEntity.build();
            list.add(entity);
            if (index % 100000 == 0) {
                repository.saveAll(list);
                list.clear();
                log.info("Last element id: {}, Time: current {}ms, total: {}min",
                        index,
                        System.currentTimeMillis() - start,
                        (System.currentTimeMillis() - startMain) / 1000 / 60);
            }
            if (index % 1000000 == 0) {
                System.gc();
            }
        }
        if (!list.isEmpty()) {
            repository.saveAll(list);
        }
        log.info("Last element: {}, Total time: {}min", entity,
                (System.currentTimeMillis() - startMain) / 1000 / 60);
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("/getData/{id}")
    public ResponseEntity<DataEntity> getData(@PathVariable Long id) {
        var entity = repository.findById(id).orElse(null);
        return (nonNull(entity)) ? ResponseEntity.ok().body(entity) : ResponseEntity.notFound().build();
    }

    @GetMapping("/findAllBy")
    public ResponseEntity<Set<DataEntity>> findAll(@PathParam("num1") Integer num1, @PathParam("num2") Integer num2) {
        var entities = repository
                .findAllByNum1AndNum2(num1, num2)
                .join();
        return (entities.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(entities);
    }

    @GetMapping("/findCountAllByParams")
    public ResponseEntity<Long> findCountAllByParams(@PathParam("num1") Integer num1, @PathParam("num1") Integer num2) {
        var count = repository
                .findCountAllByParamsNum1Num2(num1, num2)
                .join();
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/findCountAllByParamsNum1")
    public ResponseEntity<Long> findCountAllByParamsAsync(@PathParam("num1") Integer num1) {
        var count = getCount(num1);
        return ResponseEntity.ok().body(count);
    }

    public Long getCount(int num) {
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            futures.add(getByDatabase(num + 1));
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .count();
    }

    @Async
    public CompletableFuture<Long> getByDatabase(int param1) {
        return repository.findCountAllByParamsNum1(param1);
    }

}
