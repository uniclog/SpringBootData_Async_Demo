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
    private ResponseEntity<DataEntity> addRandomData(@PathVariable String count) {
        List<DataEntity> list = new ArrayList<>();
        var entity = new DataEntity();
        for (int i = 0; i < Integer.parseInt(count); i++) {
            entity = DataEntity.build();
            list.add(entity);
            if (i % 1000000 == 0) {
                repository.saveAll(list);
                list = new ArrayList<>();
            }
        }
        if (!list.isEmpty()) {
            repository.saveAll(list);
        }
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("/getData/{id}")
    private ResponseEntity<DataEntity> getData(@PathVariable String id) {
        var entity = repository.findById(Long.valueOf(id)).orElse(null);
        return (nonNull(entity)) ? ResponseEntity.ok().body(entity) : ResponseEntity.notFound().build();
    }

    @GetMapping("/findAllBy")
    private ResponseEntity<Set<DataEntity>> findAll(@PathParam("param1") String param1, @PathParam("param2") String param2) {
        var entities = repository
                .findAllByNum1AndNum2(Integer.valueOf(param1), Integer.valueOf(param2))
                .join();
        return (entities.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(entities);
    }

    @GetMapping("/findCountAllByParams")
    public ResponseEntity<Long> findCountAllByParams(@PathParam("param1") String param1, @PathParam("param2") String param2) {
        var count = repository
                .findCountAllByParamsNum1Num2(Integer.valueOf(param1), Integer.valueOf(param2))
                .join();
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/findCountAllByParamsNum1")
    public ResponseEntity<Long> findCountAllByParams(@PathParam("param1") String param1) {
        var count = getCount(Integer.parseInt(param1));
        return ResponseEntity.ok().body(count);
    }

    public Long getCount(int num) {
        List<CompletableFuture<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            futures.add(getByDatabase(num));
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .reduce(0L, Long::sum);
    }

    @Async
    public CompletableFuture<Long> getByDatabase(int param1) {
        return repository.findCountAllByParamsNum1(param1);
    }

}
