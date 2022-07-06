package com.epic.brokerservice.services;

import com.epic.brokerservice.bean.TokenBean;
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

    /*public Mono<TokenResponseBean> getToken(String name) {
        return this.webClient.get().uri("/ezsavings/api/uaa/oauth/token", name)
                .retrieve().bodyToMono(TokenResponseBean.class);
    }*/

    public String getToken(TokenBean bean) {

        TokenResponseBean tokenResponseBean = null;
        Mono<TokenResponseBean> responseBeanMono = webClient.post()
                .uri("/ezsavings/api/uaa/oauth/token")
                .body(Mono.just(bean), TokenBean.class)
                .retrieve()
                .bodyToMono(TokenResponseBean.class);

        tokenResponseBean = responseBeanMono.block();

        return tokenResponseBean.getAccess_token();
    }
}
