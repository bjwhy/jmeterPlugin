package com.wm.constant;

/**
 * Created by peekaboo on 2016/3/30.
 */
public class MessageConstant {

    public enum MessageType{
        HEARTBEAT(1,"heartbeat"),MESSAGE(2,"info");
        private int msgCode;
        private String msgInfo;
        MessageType(int msgCode,String msgInfo){
            this.msgCode = msgCode;
            this.msgInfo = msgInfo;
        }

        public int getMsgCode() {
            return msgCode;
        }

        public void setMsgCode(int msgCode) {
            this.msgCode = msgCode;
        }

        public String getMsgInfo() {
            return msgInfo;
        }

        public void setMsgInfo(String msgInfo) {
            this.msgInfo = msgInfo;
        }
    }

    public enum MessageReply{
        MESSAGE("recived"),HEARTBEAT("heartbeat");
        private String reply;
        MessageReply(String reply){
            this.reply = reply;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }
    }
}
