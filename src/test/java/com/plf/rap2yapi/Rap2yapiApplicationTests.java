package com.plf.rap2yapi;

import com.plf.rap2yapi.service.YapiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@Slf4j
@SpringBootTest
class Rap2yapiApplicationTests {

    @Autowired
    private YapiService yapiService;

    @Test
    void translate(){
        File sourceRapJsonFile = new File("E:/rap/ex.json");
        File targetYapiJsonFile = new File("E:/rap/gen.json");
        yapiService.transJsonFile(sourceRapJsonFile,targetYapiJsonFile);
    }
}
