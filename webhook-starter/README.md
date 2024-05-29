# webhook-starter

根据回调地址发送消息

- 支持[企业微信消息格式](https://developer.work.weixin.qq.com/document/path/99110)，普通文本、markdown

## 企业微信

### 发送markdown消息

```java
    public static void main(String[] args) {
        WechatWebhook webhook = new WechatWebhook();

        MarkdownMessage message = MarkdownMessage.builder()
                .content("实时新增用户反馈<font color=\"warning\">132例</font>")
                .build();

        String webhookUrl = "";
        webhook.send(webhookUrl, message);
    }
```

### 发送普通文本消息

```java
public static void main(String[] args) {
    WechatWebhook webhook = new WechatWebhook();
    TextMessage message = TextMessage.builder().content("文本测试")
            .mentionedMobileList(List.of("1****0"))
            .build();
    
    String webhookUrl = "";
    webhook.send(webhookUrl, message);
}
```

## 生成markdown

引用[mdkiller](https://github.com/elltor/mdkiller)格式化生成 Markdown 文本的工具

