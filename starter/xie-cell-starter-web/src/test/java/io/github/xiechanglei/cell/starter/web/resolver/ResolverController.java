package io.github.xiechanglei.cell.starter.web.resolver;

import io.github.xiechanglei.cell.common.lang.date.DateHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/resolver")
public class ResolverController {
    @RequestMapping("/date")
    public String testDate(Date date) {
        return DateHelper.convertor.format(date);
    }
}
