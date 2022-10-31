package com.trendyol.kubernetesoperatorapi.adapter.rest.index;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@ApiIgnore
@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public RedirectView redirectToSwaggerUi(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping("_monitoring/health")
    public ResponseEntity<HttpStatus> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
