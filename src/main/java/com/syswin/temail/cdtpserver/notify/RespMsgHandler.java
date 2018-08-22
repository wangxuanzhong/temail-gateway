package com.syswin.temail.cdtpserver.notify;

import com.google.protobuf.ByteString;
import com.syswin.temail.cdtpserver.connection.ActiveTemailManager;
import com.syswin.temail.cdtpserver.entity.CDTPPackageProto.CDTPPackage;
import com.syswin.temail.cdtpserver.entity.TemailInfo;
import com.syswin.temail.cdtpserver.entity.TransferCDTPPackage;
import com.syswin.temail.cdtpserver.handler.SendMsg;
import com.syswin.temail.cdtpserver.utils.CdtpPackageUtil;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RespMsgHandler {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  public static void sendMsg(TransferCDTPPackage transferCDTPPackage) {
    try {
      CDTPPackage.Builder builder = CDTPPackage.newBuilder();
      CdtpPackageUtil.copyBeanProperties(transferCDTPPackage, builder);
      CDTPPackage ctPackage = builder.build();

      String to = transferCDTPPackage.getTo();
      Map<String, TemailInfo> temailInfoMap = ActiveTemailManager.getAll(to);
      if (null != temailInfoMap && !temailInfoMap.isEmpty()) {
        Iterator iter = temailInfoMap.entrySet().iterator();
        while (iter.hasNext()) {
          Map.Entry<String, TemailInfo> entry = (Map.Entry<String, TemailInfo>) iter.next();
          String temail = entry.getKey();
          TemailInfo temailInfo = entry.getValue();
          LOGGER.info("在线把消息推送给:{}, 具体推送消息为:{}", temail, ctPackage);
          SendMsg.sendToTemail(ctPackage, temailInfo.getSocketChannel());
        }
      } else {
        LOGGER.info("no find temail:{}  related sockchannel in  ActiveTemailManager.", to);
      }

    } catch (Exception ex) {
      LOGGER.error("send tomail msg  error", ex);
    }
  }

}
