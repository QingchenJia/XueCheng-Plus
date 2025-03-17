package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.TeachPlan;
import com.xuecheng.content.model.po.TeachPlanMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeachPlanDto extends TeachPlan {
    private TeachPlanMedia teachPlanMedia;
}
