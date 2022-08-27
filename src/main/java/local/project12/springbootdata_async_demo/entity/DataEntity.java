package local.project12.springbootdata_async_demo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

import static java.lang.String.format;

@Entity
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer num1;
    private Integer num2;
    private Integer num3;
    private Integer num4;
    private Integer num5;
    private String randomString;

    public static DataEntity build() {
        Random random = new Random();
        DataEntity data = new DataEntity();
        data.num1 = random.nextInt(100);
        data.num2 = random.nextInt(100);
        data.num3 = random.nextInt(100);
        data.num4 = random.nextInt(100);
        data.num5 = random.nextInt(100);
        data.randomString = format("%d%d", random.nextInt(100), random.nextInt(100));
        return data;
    }
}
