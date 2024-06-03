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

由于[企业微信对markdown的支持阉割](https://developer.work.weixin.qq.com/document/path/99110#markdown%E7%B1%BB%E5%9E%8B) ，单独封装了一个简易版的markdown生成器

```java
WechatWebhook wechatWebhook = new WechatWebhook();
String url="https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=";
String build = MDSpirit.builder()
        .title("新订单提醒", TitleEnum.H1)
        .br()
        .text("您有一个新的订单")
        .br()
        .quote("客户名称：")
        .text("上海",new Style(Style.Color.comment))
        .build();
```

### 参考

[mdkiller](https://github.com/elltor/mdkiller)

[markdowngenerator](https://github.com/Steppschuh/Java-Markdown-Generator/tree/master)

