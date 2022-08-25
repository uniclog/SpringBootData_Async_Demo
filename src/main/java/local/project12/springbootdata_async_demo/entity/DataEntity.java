package local.project12.springbootdata_async_demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class DataEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Integer randomData;
    private String randomString;
}
