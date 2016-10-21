package com.dhcc.yangmingci.event;


/**
 * 定义一个消息类
 */
public class EventBusEvents {
    /**
     * 定义Activity从其他页面获取数据
     */
    public static class Event{
        private String value;

        public Event(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
