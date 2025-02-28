package org.unhcr.osmosys.model.cubeDTOs;

public class TagIndicatorValuesDTO {

    private Long iv_id;
    private Long tag_id;

    public TagIndicatorValuesDTO(Long iv_id, Long tag_id) {
        this.iv_id = iv_id;
        this.tag_id = tag_id;
    }

    public Long getIv_id() {
        return iv_id;
    }

    public void setIv_id(Long iv_id) {
        this.iv_id = iv_id;
    }

    public Long getTag_id() {
        return tag_id;
    }

    public void setTag_id(Long tag_id) {
        this.tag_id = tag_id;
    }
}
