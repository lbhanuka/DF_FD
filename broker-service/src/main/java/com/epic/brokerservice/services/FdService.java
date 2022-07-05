package com.epic.brokerservice.services;

import com.epic.brokerservice.bean.TokenResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FdService {

    private final WebClient webClient;

    public FdService() {
        this.webClient = WebClient.create("https://stgezsavings.ezloan.lk");
    }

    public Mono<TokenResponseBean> someRestCall(String name) {
        return this.webClient.get().uri("/ezsavings/api/uaa/oauth/token", name)
                .retrieve().bodyToMono(TokenResponseBean.class);
    }
}
