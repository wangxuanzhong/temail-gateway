package com.syswin.temail.gateway.service;

import static com.syswin.temail.gateway.entity.CommandSpaceType.CHANNEL;
import static com.syswin.temail.gateway.entity.CommandType.INTERNAL_ERROR;

import com.syswin.temail.gateway.TemailGatewayProperties;
import com.syswin.temail.gateway.entity.CDTPPacket;
import com.syswin.temail.gateway.entity.CDTPPacketTrans;
import com.syswin.temail.gateway.entity.CDTPProtoBuf.CDTPServerError;
import java.util.function.Consumer;
import org.springframework.web.reactive.function.client.WebClient;

public class RequestService implements RequestHandler {

  private final DispatchService dispatchService;
  private final TemailGatewayProperties properties;

  public RequestService(WebClient dispatcherWebClient,
      TemailGatewayProperties properties) {
    dispatchService = new DispatchService(dispatcherWebClient);
    this.properties = properties;
  }

  @Override
  public void handleRequest(CDTPPacket packet, Consumer<CDTPPacket> responseHandler) {
    dispatchService.dispatch(properties.getDispatchUrl(), new CDTPPacketTrans(packet),
        clientResponse -> clientResponse
            .bodyToMono(String.class)
            .subscribe(response -> {
              CDTPPacket respPacket;
              if (response != null) {
                // 后台正常返回
                respPacket = packet;
                respPacket.setData(response.getBytes());
              } else {
                respPacket =
                    errorPacket(packet, INTERNAL_ERROR.getCode(), "dispatcher请求没有从服务器端返回结果对象：");
              }
              // 请求的数据可能加密，而返回的数据没有加密，需要设置加密标识
              respPacket.getHeader().setDataEncryptionMethod(0);
              responseHandler.accept(respPacket);
            }), t -> responseHandler.accept(
            errorPacket(packet, INTERNAL_ERROR.getCode(), t.getMessage())));
  }

  private CDTPPacket errorPacket(CDTPPacket packet, int code, String message) {
    packet.setCommandSpace(CHANNEL.getCode());
    packet.setCommand(INTERNAL_ERROR.getCode());

    CDTPServerError.Builder builder = CDTPServerError.newBuilder();
    builder.setCode(code);
    builder.setDesc(message);
    packet.setData(builder.build().toByteArray());
    return packet;
  }
}
