package com.syswin.temail.gateway.service;

import com.google.gson.Gson;
import com.syswin.temail.ps.common.entity.CDTPPacketTrans;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
class DispatchService {

  private final WebClient webClient;
  private Gson gson = new Gson();

  DispatchService(WebClient webClient) {
    this.webClient = webClient;
  }

  void dispatch(String dispatchUrl, CDTPPacketTrans packet,
      Consumer<? super ClientResponse> consumer,
      Consumer<? super Throwable> errorConsumer) {
    webClient.post()
        .uri(dispatchUrl)
        .contentType(MediaType.APPLICATION_JSON)
        .syncBody(gson.toJson(packet))
        .exchange()
        .subscribe(consumer, errorConsumer);
  }
}