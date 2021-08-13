package cn.tucci.nio.socket;

/**
 * @author tucci.lee
 */
public class Message {
    private Byte type;
    private Integer from;
    private Integer to;
    private String content;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "type=" + type +
                ", from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                '}';
    }
}
