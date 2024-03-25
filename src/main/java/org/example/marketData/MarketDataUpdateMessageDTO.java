package org.example.marketData;

import java.util.List;

public class MarketDataUpdateMessageDTO {

    private Integer messageId;

    private List<MarketDataChangeDTO> changes;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public List<MarketDataChangeDTO> getChanges() {
        return changes;
    }

    public void setChanges(List<MarketDataChangeDTO> changes) {
        this.changes = changes;
    }
}
